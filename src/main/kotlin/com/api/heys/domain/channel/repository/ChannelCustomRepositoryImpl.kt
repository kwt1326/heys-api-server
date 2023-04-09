package com.api.heys.domain.channel.repository

import com.api.heys.constants.DefaultString
import com.api.heys.constants.MessageString
import com.api.heys.constants.enums.*
import com.api.heys.domain.channel.dto.*
import com.api.heys.entity.*
import com.api.heys.utils.CommonUtil
import com.api.heys.utils.UserDetailPercentUtils
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.io.InvalidObjectException
import java.time.LocalDateTime

@Repository
class ChannelCustomRepositoryImpl(
    private val commonUtil: CommonUtil,
    private val jpaQueryFactory: JPAQueryFactory,
) : ChannelCustomRepository {
    val qUsers: QUsers = QUsers.users
    val qUserDetail: QUserDetail = QUserDetail.userDetail
    val qInterest: QInterest = QInterest.interest
    val qInterestRelations: QInterestRelations = QInterestRelations.interestRelations
    val qContent: QContents = QContents.contents
    val qExtraContentDetail: QExtraContentDetail = QExtraContentDetail.extraContentDetail
    val qChannels: QChannels = QChannels.channels
    val qChannelBookMark: QChannelBookMark = QChannelBookMark.channelBookMark
    val qChannelView: QChannelView = QChannelView.channelView
    val qChannelLink: QChannelLink = QChannelLink.channelLink
    val qChannelPurpose: QChannelPurpose = QChannelPurpose.channelPurpose
    val qChannelDetail: QChannelDetail = QChannelDetail.channelDetail
    val qChannelUserRelations: QChannelUserRelations = QChannelUserRelations.channelUserRelations

    fun channelFilterQuery(
        queryBase: JPAQuery<Channels>,
        params: GetChannelsParam,
        type: ChannelType,
        contentId: Long?
    ): List<Channels> {
        var query = queryBase
            .join(qChannels.detail, qChannelDetail).fetchJoin()
            .leftJoin(qChannels.channelViews, qChannelView).fetchJoin()
            .leftJoin(qChannelDetail.interestRelations, qInterestRelations).fetchJoin()
            .leftJoin(qInterestRelations.interest, qInterest).fetchJoin()
            .leftJoin(qChannelDetail.purposes, qChannelPurpose).fetchJoin()
            .leftJoin(qChannels.channelUserRelations, qChannelUserRelations).fetchJoin()
            .where(qChannels.removedAt.isNull)
            .where(qChannelUserRelations.removedAt.isNull)
            .where(qChannels.type.eq(type))

        if (contentId != null) {
            query = query
                .join(qChannels.contents, qContent).fetchJoin()
                .where(qContent.id.eq(contentId))
        }

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
            if (!params.lastRecruitDate.isNullOrBlank()) {
                query = query.where(
                    qChannelDetail.lastRecruitDate.before(LocalDateTime.parse(params.lastRecruitDate))
                )
            }
        }

        // 관심분야 파라미터 배열 요소중 하나라도 맞는게 있으면 쿼리 대상
        if (!params.interests.isNullOrEmpty()) {
            query = query.where(qInterest.name.`in`(params.interests))
        }

        // 참여 목적 쿼리
        if (!params.purposes.isNullOrEmpty()) {
            query = query.where(qChannelPurpose.purpose.`in`(params.purposes))
        }

        if (params.online != null) {
            query = query.where(qChannelDetail.online.eq(params.online))

            // 활동 형태가 온*오프라인 혹은 오프라인 일 경우 위치 쿼리
            if (!params.location.isNullOrBlank() && (params.online == Online.Offline || params.online == Online.OnOffLine)) {
                query = query.where(qChannelDetail.location.eq(params.location))
            }
        }

        return query
            .limit(params.limit)
            .offset((params.page - 1) * params.limit)
            .distinct()
            .fetch() ?: listOf()
    }

    fun channelFilterCountQuery(
        queryBase: JPAQuery<Long>,
        params: GetChannelsParam,
        type: ChannelType,
        contentId: Long?
    ): Long {
        var query = queryBase
            .join(qChannels.detail, qChannelDetail)
            .leftJoin(qChannels.channelViews, qChannelView)
            .leftJoin(qChannelDetail.interestRelations, qInterestRelations)
            .leftJoin(qInterestRelations.interest, qInterest)
            .leftJoin(qChannelDetail.purposes, qChannelPurpose)
            .leftJoin(qChannels.channelUserRelations, qChannelUserRelations)
            .where(qChannels.removedAt.isNull)
            .where(qChannelUserRelations.removedAt.isNull)
            .where(qChannels.type.eq(type))

        if (contentId != null) {
            query = query
                .join(qChannels.contents, qContent)
                .where(qContent.id.eq(contentId))
        }

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
            if (!params.lastRecruitDate.isNullOrBlank()) {
                query = query.where(
                    qChannelDetail.lastRecruitDate.before(LocalDateTime.parse(params.lastRecruitDate))
                )
            }
        }

        // 관심분야 파라미터 배열 요소중 하나라도 맞는게 있으면 쿼리 대상
        if (!params.interests.isNullOrEmpty()) {
            query = query.where(qInterest.name.`in`(params.interests))
        }

        // 참여 목적 쿼리
        if (!params.purposes.isNullOrEmpty()) {
            query = query.where(qChannelPurpose.purpose.`in`(params.purposes))
        }

        if (params.online != null) {
            query = query.where(qChannelDetail.online.eq(params.online))

            // 활동 형태가 온*오프라인 혹은 오프라인 일 경우 위치 쿼리
            if (!params.location.isNullOrBlank() && (params.online == Online.Offline || params.online == Online.OnOffLine)) {
                query = query.where(qChannelDetail.location.eq(params.location))
            }
        }

        return query.fetchOne() ?: 0
    }

    override fun getJoinAndWaitingChannelCounts(userId: Long): HashMap<String, Long> {
        val map = HashMap<String, Long>()
        val result = jpaQueryFactory
            .select(qChannelUserRelations.status, qChannelUserRelations.count())
            .from(qChannelUserRelations)
            .join(qChannelUserRelations.user, qUsers).on(qUsers.id.eq(userId))
            .where(qChannelUserRelations.removedAt.isNull)
            .groupBy(qChannelUserRelations.status)
            .fetch()

        result.map {
            map[it.toArray().first().toString()] = (it.toArray().last() ?: 0) as Long
        }

        map[DefaultString.joinChannelKey] = map[DefaultString.joinChannelKey] ?: 0
        map[DefaultString.waitChannelKey] = map[DefaultString.waitChannelKey] ?: 0

        return map
    }

    override fun getChannelFollowers(channelId: Long, status: ChannelMemberStatus): List<ChannelUserData> {
        val queryResult = jpaQueryFactory
            .selectFrom(qChannelUserRelations)
            .join(qChannelUserRelations.channel, qChannels).fetchJoin()
            .join(qChannelUserRelations.user, qUsers).fetchJoin()
            .join(qUsers.detail, qUserDetail).fetchJoin()
            .where(qChannelUserRelations.removedAt.isNull)
            .where(qChannelUserRelations.status.eq(status))
            .where(qChannels.removedAt.isNull)
            .where(qChannels.id.eq(channelId))
            .distinct()
            .fetch()

        return queryResult.map {
            val user = it.user
            val userDetail = user.detail
                ?: throw InvalidObjectException("UserDetail must exist in user")

            ChannelUserData(
                id = user.id,
                gender = userDetail.gender,
                username = userDetail.username,
                requestedAt = if (it.createdAt != null) it.createdAt.toString() else "",
            )
        }
    }

    override fun getChannels(type: ChannelType, params: GetChannelsParam, contentId: Long?): GetChannelsResponse {
        val query = jpaQueryFactory.selectFrom(qChannels)
        val totalCountQuery = jpaQueryFactory.select(qChannels.countDistinct()).from(qChannels)

        val data = channelFilterQuery(query, params, type, contentId)
            .filter { it.detail != null }
            .map {
                val detail: ChannelDetail = it.detail!!
                val view = it.channelViews
                ChannelListItemData(
                    id = it.id,
                    name = detail.name,
                    viewCount = view.count().toLong(),
                    joinRemainCount = detail.limitPeople.toLong().minus(it.channelUserRelations.count()),
                    pastDay = commonUtil.diffDay(it.createdAt, LocalDateTime.now()),
                    dDay = commonUtil.calculateDday(detail.lastRecruitDate),
                    thumbnailUri = detail.thumbnailUri
                )
            }

        val totalCount = channelFilterCountQuery(totalCountQuery, params, type, contentId)
        val totalPage =
            commonUtil.calcTotalPage(totalCount, params.limit)

        return GetChannelsResponse(data, totalPage, MessageString.SUCCESS_EN)
    }

    override fun getChannelDetail(channelId: Long, userId: Long): GetChannelDetailData? {
        val query = jpaQueryFactory
            .selectFrom(qChannels)
            .join(qChannels.leader, qUsers).fetchJoin()
            .join(qChannels.detail, qChannelDetail).fetchJoin()
            .leftJoin(qChannels.channelBookMarks, qChannelBookMark).fetchJoin()
            .leftJoin(qChannels.contents, qContent).fetchJoin()
            .leftJoin(qContent.extraDetail, qExtraContentDetail).fetchJoin()
            .leftJoin(qChannelDetail.purposes, qChannelPurpose).fetchJoin()
            .leftJoin(qChannelDetail.links, qChannelLink).fetchJoin()
            .leftJoin(qChannelDetail.interestRelations, qInterestRelations).fetchJoin()
            .leftJoin(qInterestRelations.interest, qInterest).fetchJoin()
            .leftJoin(qChannels.channelUserRelations, qChannelUserRelations).fetchJoin()
            .where(qChannels.removedAt.isNull)
            .where(qChannelUserRelations.removedAt.isNull)
            .where(qChannels.id.eq(channelId))

        val channel = query.fetchOne() ?: return null

        val channelDetail = channel.detail ?: return null
        val channelLeader = channel.leader
        val channelLeaderDetail = channelLeader.detail ?: return null

        val leaderData = GetChannelDetailLeaderData(
            id = channelLeader.id,
            username = channelLeaderDetail.username,
            introduceText = channelLeaderDetail.introduceText,
            percentage = UserDetailPercentUtils.calculateUserDetailPercentage(channelLeaderDetail),
        )

        val purposes = channelDetail.purposes.map { GetChannelDetailPurposeData(id = it.id, purpose = it.purpose) }
        val links = channelDetail.links.map { GetChannelDetailLinkData(id = it.id, link = it.link) }
        val interests = channelDetail.interestRelations.filter { it.interest != null }.map { it.interest!!.name }
        val waitingUserList = channel.channelUserRelations.filter { it.status == ChannelMemberStatus.Waiting }.map {
            GetChannelDetailUserData(
                id = it.user.id,
                percentage = UserDetailPercentUtils.calculateUserDetailPercentage(it.user.detail),
                gender = if (it.user.detail != null) it.user.detail!!.gender else Gender.NonBinary,
                date = it.createdAt
            )
        }
        val approvedUserList = channel.channelUserRelations.filter { it.status == ChannelMemberStatus.Approved }.map {
            GetChannelDetailUserData(
                id = it.user.id,
                percentage = UserDetailPercentUtils.calculateUserDetailPercentage(it.user.detail),
                gender = if (it.user.detail != null) it.user.detail!!.gender else Gender.NonBinary,
                date = it.approveRequestAt
            )
        }

        var contentData: GetChannelDetailContentData? = null

        if (channel.type == ChannelType.Content) {
            val content = channel.contents ?: return null

            if (listOf(ContentType.Contest, ContentType.Extracurricular).contains(content.contentType)) {
                val contentDetail = content.extraDetail ?: return null
                contentData = GetChannelDetailContentData(
                    id = content.id,
                    title = contentDetail.title,
                    previewImgUrl = contentDetail.previewImgUri,
                    company = contentDetail.company,
                    type = content.contentType
                )
            }
        }

        var relationship = ChannelRelationship.Visiter

        if (channelLeader.id == userId) relationship = ChannelRelationship.Leader
        else if (waitingUserList.find { it.id == userId } != null) relationship = ChannelRelationship.Applicant
        else if (approvedUserList.find { it.id == userId } != null) relationship = ChannelRelationship.Member

        val isBookMarked = channel.channelBookMarks.find { it.users!!.id == userId } != null

        return GetChannelDetailData(
            id = channel.id,
            thumbnailUri = channelDetail.thumbnailUri,
            title = channelDetail.name,
            online = channelDetail.online,
            location = channelDetail.location,
            limitPeople = channelDetail.limitPeople,
            recruitMethod = channelDetail.recruitMethod,
            lastRecruitDate = channelDetail.lastRecruitDate,
            contentText = channelDetail.contentText,
            recruitText = channelDetail.recruitText,
            links = links,
            purposes = purposes,
            interests = interests,
            WaitingUserList = waitingUserList,
            ApprovedUserList = approvedUserList,
            leader = leaderData,
            contentData = contentData,
            relationshipWithMe = relationship,
            isBookMarked = isBookMarked,
        )
    }

    override fun getChannelView(channelId: Long, userId: Long): ChannelView? {
        val query = jpaQueryFactory
            .selectFrom(qChannelView)
            .join(qChannelView.channel, qChannels).fetchJoin()
            .join(qChannelView.users, qUsers).fetchJoin()
            .where(qChannels.id.eq(channelId))
            .where(qUsers.id.eq(userId))

        return query.fetchOne()
    }

    override fun getChannelBookMark(channelId: Long, userId: Long): ChannelBookMark? {
        val query = jpaQueryFactory
            .selectFrom(qChannelBookMark)
            .join(qChannelBookMark.channel, qChannels).fetchJoin()
            .join(qChannelBookMark.users, qUsers).fetchJoin()
            .where(qChannels.id.eq(channelId))
            .where(qUsers.id.eq(userId))

        return query.fetchOne()
    }
}