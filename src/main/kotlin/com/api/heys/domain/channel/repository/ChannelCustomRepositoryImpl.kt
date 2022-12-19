package com.api.heys.domain.channel.repository

import com.api.heys.entity.*
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ChannelCustomRepositoryImpl(
        private val jpaQueryFactory: JPAQueryFactory,
) : ChannelCustomRepository {
    val qUsers: QUsers = QUsers.users
    val qChannels: QChannels = QChannels.channels
    val qChannelUsers: QChannelUsers = QChannelUsers.channelUsers
    val qChannelUserRelations: QChannelUserRelations = QChannelUserRelations.channelUserRelations

    override fun getJoinAndWaitingChannelCounts(userId: Long): HashMap<String, Long> {
        val map = HashMap<String, Long>()
        val rel = jpaQueryFactory.select(
                qChannelUserRelations.joinedChannel.count(),
                qChannelUserRelations.waitingChannel.count()
        )
                .from(qChannelUserRelations)
                .leftJoin(qChannelUserRelations.joinedChannel)
                .leftJoin(qChannelUserRelations.waitingChannel)
                .join(qChannelUserRelations.channelUser, qChannelUsers)
                .join(qChannelUsers.user, qUsers).on(qUsers.id.eq(userId))
                .fetchOne() ?: return map

        map["joined"] = rel.get(qChannelUserRelations.joinedChannel.count()) ?: 0
        map["waiting"] = rel.get(qChannelUserRelations.waitingChannel.count()) ?: 0

        return map
    }
}