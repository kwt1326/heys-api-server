package com.api.heys.domain.channel.repository

import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.constants.enums.ChannelType
import com.api.heys.constants.enums.Online
import com.api.heys.domain.channel.dto.ChannelListItemData
import com.api.heys.domain.channel.dto.GetChannelsParam
import com.api.heys.entity.*
import com.api.heys.utils.CommonUtil
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ChannelCustomRepositoryImpl(
    private val commonUtil: CommonUtil,
    private val jpaQueryFactory: JPAQueryFactory,
) : ChannelCustomRepository {
    val qUsers: QUsers = QUsers.users
    val qInterest: QInterest = QInterest.interest
    val qInterestRelations: QInterestRelations = QInterestRelations.interestRelations
    val qContent: QContents = QContents.contents
    val qChannels: QChannels = QChannels.channels
    val qChannelView: QChannelView = QChannelView.channelView
    val qChannelDetail: QChannelDetail = QChannelDetail.channelDetail
    val qChannelUserRelations: QChannelUserRelations = QChannelUserRelations.channelUserRelations

    fun channelFilterQuery(queryBase: JPAQuery<Channels>, params: GetChannelsParam, type: ChannelType): List<Channels> {
        var query = queryBase.where(qChannels.type.eq(type))

        // includeClosed filter (마감된 채널 포함 여부 - 조건 : DDay 남은것, 정원 안찬것)
        // includeClosed == true 이면 '마감 일자' 및 '제한 인원' 쿼리 무시
        // includeClosed == false 이거나 null 이면
        // '오늘 이후에 마감일자' 혹은 '오늘 이후 & 모집 기간 이내 마감일자' 및 '제한 인원수 보다 작은 것'만 쿼리
        if (params.includeClosed == null || params.includeClosed == false) {
            query = query.where(
                qChannelDetail.lastRecruitDate.after(LocalDateTime.now()),
                qChannelDetail.limitPeople.gt(qChannels.channelUserRelations.size())
            )

            // 마감 일자 값이 존재할 경우 해당 값 이전 요소만 쿼리
            if (params.lastRecruitDate != null) {
                query = query.where(
                    qChannelDetail.lastRecruitDate.before(LocalDateTime.parse(params.lastRecruitDate))
                )
            }
        }

        // 관심분야 파라미터 배열 요소중 하나라도 맞는게 있으면 쿼리 대상
        if (params.interests != null) {
            query = query.where(qInterest.name.`in`(params.interests))
        }

        if (params.online != null) {
            query = query.where(qChannelDetail.online.eq(params.online))

            // 활동 형태가 온*오프라인 혹은 오프라인 일 경우 위치 쿼리
            if (params.location != null && (params.online == Online.Offline || params.online == Online.OnOffLine)) {
                query = query.where(qChannelDetail.location.eq(params.location))
            }
        }

        return query
            .limit(params.limit)
            .offset((params.page - 1) * params.limit)
            .distinct()
            .fetch() ?: listOf()
    }

    override fun getJoinAndWaitingChannelCounts(userId: Long): HashMap<String, Long> {
        val map = HashMap<String, Long>()
        val query = jpaQueryFactory.select(qChannelUserRelations.channel.count())
            .from(qChannelUserRelations)
            .leftJoin(qChannelUserRelations.channel)
            .join(qChannelUserRelations.user, qUsers)
            .on(qUsers.id.eq(userId))
            .on(qChannelUserRelations.removedAt.isNull)

        map["joined"] = query.on(qChannelUserRelations.status.eq(ChannelMemberStatus.Approved)).fetchOne() ?: 0
        map["waiting"] = query.on(qChannelUserRelations.status.eq(ChannelMemberStatus.Waiting)).fetchOne() ?: 0

        return map
    }

    override fun getJoinAndWaitingChannels(userId: Long): HashMap<String, List<Channels>>? {
        val map = HashMap<String, List<Channels>>()
        val query = jpaQueryFactory.select(qChannelUserRelations.channel)
            .from(qChannelUserRelations)
            .leftJoin(qChannelUserRelations.channel)
            .join(qChannelUserRelations.user, qUsers)
            .on(qUsers.id.eq(userId))
            .on(qChannelUserRelations.removedAt.isNull)

        map["joined"] = query.on(qChannelUserRelations.status.eq(ChannelMemberStatus.Approved)).fetch()
        map["waiting"] = query.on(qChannelUserRelations.status.eq(ChannelMemberStatus.Waiting)).fetch()

        return map
    }

    override fun getChannels(type: ChannelType, params: GetChannelsParam, contentId: Long?): List<ChannelListItemData> {
        var query = jpaQueryFactory
            .selectFrom(qChannels)
            .join(qChannels.detail, qChannelDetail).fetchJoin()
            .join(qChannels.channelView, qChannelView).fetchJoin()
            .leftJoin(qChannels.channelUserRelations, qChannelUserRelations).fetchJoin()
            .leftJoin(qChannelDetail.interestRelations, qInterestRelations).fetchJoin()
            .leftJoin(qInterestRelations.interest, qInterest).fetchJoin()

        if (contentId != null) {
            query = query.leftJoin(qChannels.contents, qContent)
                .on(qContent.id.eq(contentId))
                .fetchJoin()
        }

        return channelFilterQuery(query, params, type)
            .filter { it.detail != null && it.channelView != null }
            .map {
                val detail: ChannelDetail = it.detail!!
                val view: ChannelView = it.channelView!!
                ChannelListItemData(
                    id = it.id,
                    name = detail.name,
                    viewCount = view.count,
                    joinRemainCount = detail.limitPeople.toLong().minus(it.channelUserRelations.count()),
                    pastDay = commonUtil.calculateDday(detail.lastRecruitDate),
                    dDay = commonUtil.diffDay(it.createdAt, LocalDateTime.now()),
                    thumbnailUri = detail.thumbnailUri
                )
            }
    }
}