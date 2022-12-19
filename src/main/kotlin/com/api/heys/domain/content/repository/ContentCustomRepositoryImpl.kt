package com.api.heys.domain.content.repository

import com.api.heys.domain.content.dto.GetContentsParam
import com.api.heys.entity.*
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class ContentCustomRepositoryImpl(
        private val jpaQueryFactory: JPAQueryFactory,
) : ContentCustomRepository {
    val qContents: QContents = QContents.contents
    val qContentDetail: QContentDetail = QContentDetail.contentDetail
    val qContentView: QContentView = QContentView.contentView
    val qInterest: QInterest = QInterest.interest
    val qInterestRelations: QInterestRelations = QInterestRelations.interestRelations
    val qChannels: QChannels = QChannels.channels

    /**
     * Filter conditions
     *
     * type - 스터디, 대외활동 등 타입 (페이지 구분)
     * interest - 관심분야 리스트
     * online - 활동 형태
     * lastRecruitDate - 마감 일자(모집기간) 프론트엔드 에서 날짜를 보내주면, 현재 날짜에서 diff 연산하여 탐색.
     * */
    override fun findContents(params: GetContentsParam): List<Contents> {
        var query = jpaQueryFactory.select(qContents)
                .from(qContents)
                .where(qContents.contentType.eq(params.type))
                .join(qContents.detail, qContentDetail).fetchJoin()
                .join(qContents.channels, qChannels).fetchJoin()
                .join(qContents.contentView, qContentView).fetchJoin()
                .join(qContentDetail.interestRelations, qInterestRelations).fetchJoin()
                .join(qInterestRelations.interest, qInterest).fetchJoin()

        // includeClosed filter (조건 : DDay 남은것, 정원 안찬것)
        if (params.includeClosed == null || params.includeClosed == false) {
            query = query.where(
                    qContentDetail.lastRecruitDate.after(LocalDateTime.now()),
                    qContentDetail.limitPeople.gt(qContents.channels.size())
            )
        }

        if (params.interests != null) {
            query = query.where(qInterest.name.`in`(params.interests))
        }

        if (params.online != null) {
            query = query.where(qContentDetail.online.eq(params.online))
        }

        if (params.lastRecruitDate != null) {
            val date = LocalDateTime.parse(params.lastRecruitDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            query = query.where(
                    qContentDetail.lastRecruitDate.between(date, LocalDateTime.now())
            )
        }

        return query
                .limit(params.limit)
                .offset((params.page - 1) * params.limit)
                .distinct()
                .fetch() ?: listOf()
    }
}