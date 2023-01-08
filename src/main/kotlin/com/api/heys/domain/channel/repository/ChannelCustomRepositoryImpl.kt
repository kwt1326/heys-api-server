package com.api.heys.domain.channel.repository

import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.entity.*
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ChannelCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : ChannelCustomRepository {
    val qUsers: QUsers = QUsers.users
    val qContents: QContents = QContents.contents
    val qContentDetail: QContentDetail = QContentDetail.contentDetail
    val qChannels: QChannels = QChannels.channels
    val qChannelUserRelations: QChannelUserRelations = QChannelUserRelations.channelUserRelations

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

    override fun getChannels(contentId: Long): List<Channels> {
        return jpaQueryFactory.select(qChannels)
            .from(qChannels)
            .join(qChannels.contents, qContents).fetchJoin()
            .join(qContents.detail, qContentDetail).fetchJoin()
            .where(qContents.id.eq(contentId))
            .fetch() ?: return listOf()
    }
}