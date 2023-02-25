package com.api.heys.domain.content.repository

import com.api.heys.constants.enums.ContentType
import com.api.heys.domain.content.dto.ExtraContentListItemData
import com.api.heys.domain.content.dto.GetExtraContentsParam
import com.api.heys.entity.*
import com.api.heys.utils.CommonUtil
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ContentCustomRepositoryImpl(
    private val commonUtil: CommonUtil,
    private val jpaQueryFactory: JPAQueryFactory,
) : ContentCustomRepository {
    val qContents: QContents = QContents.contents
    val qExtraContentDetail: QExtraContentDetail = QExtraContentDetail.extraContentDetail
    val qContentView: QContentView = QContentView.contentView
    val qInterest: QInterest = QInterest.interest
    val qInterestRelations: QInterestRelations = QInterestRelations.interestRelations
    val qChannels: QChannels = QChannels.channels

    fun extraContentFilterQuery(queryBase: JPAQuery<Contents>, params: GetExtraContentsParam): List<Contents> {
        var query = queryBase

        // includeClosed filter (마감된 컨텐츠 포함 여부 - 조건 : DDay 남은것, 정원 안찬것)
        // includeClosed == true 이면 '마감 일자' 및 '제한 인원' 쿼리 무시
        // includeClosed == false 이거나 null 이면
        // '오늘 이후에 마감일자' 혹은 '오늘 이후 & 모집 기간 이내 마감일자' 및 '제한 인원수 보다 작은 것'만 쿼리
        if (params.includeClosed == null || params.includeClosed == false) {
            query = query.where(
                qExtraContentDetail.endDate.after(LocalDateTime.now())
            )

            // 마감 일자 값이 존재할 경우 해당 값 이전 요소만 쿼리
            if (params.lastRecruitDate != null) {
                query = query.where(
                    qExtraContentDetail.endDate.before(LocalDateTime.parse(params.lastRecruitDate))
                )
            }
        }

        // 관심분야 파라미터 배열 요소중 하나라도 맞는게 있으면 쿼리 대상
        if (params.interests != null) {
            query = query.where(qInterest.name.`in`(params.interests))
        }

        return query
            .limit(params.limit)
            .offset((params.page - 1) * params.limit)
            .distinct()
            .fetch() ?: listOf()
    }

    /**
     * Filter conditions
     *
     * interest - 관심분야 리스트
     * lastRecruitDate - 마감 일자(모집기간) 프론트엔드 에서 날짜를 보내주면, 현재 날짜에서 diff 연산하여 탐색.
     * */
    override fun findExtraContents(params: GetExtraContentsParam): List<ExtraContentListItemData> {
        val query = jpaQueryFactory
            .selectFrom(qContents)
            .join(qContents.extraDetail, qExtraContentDetail).fetchJoin()
            .leftJoin(qContents.channels, qChannels).fetchJoin()
            .leftJoin(qExtraContentDetail.interestRelations, qInterestRelations).fetchJoin()
            .leftJoin(qInterestRelations.interest, qInterest).fetchJoin()
            .join(qContents.contentView, qContentView).fetchJoin()
            .where(qContents.contentType.eq(ContentType.Extra))

        return extraContentFilterQuery(query, params).map {
            val detail = it.extraDetail!!
            val view = it.contentView!!
            val channels = it.channels
            ExtraContentListItemData(
                id = it.id,
                title = detail.title,
                company = detail.company,
                viewCount = view.count,
                channelCount = channels.count(),
                dDay = commonUtil.diffDay(detail.endDate, LocalDateTime.now()),
                previewImgUri = detail.previewImgUri
            )
        }
    }

    override fun getExtraContent(contentId: Long): Contents? {
        return jpaQueryFactory
            .selectFrom(qContents)
            .join(qContents.contentView, qContentView).fetchJoin()
            .join(qContents.extraDetail, qExtraContentDetail).fetchJoin()
            .leftJoin(qContents.channels, qChannels).fetchJoin()
            .leftJoin(qExtraContentDetail.interestRelations, qInterestRelations).fetchJoin()
            .fetchOne()
    }

    override fun getContentView(contentId: Long): ContentView? {
        val result = jpaQueryFactory
            .selectFrom(qContents)
            .join(qContents.contentView, qContentView).fetchJoin()
            .fetchOne() ?: return null

        return result.contentView
    }
}