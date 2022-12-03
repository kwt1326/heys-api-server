package com.api.heys.domain.channel

import com.api.heys.constants.DefaultString
import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.domain.channel.dto.ChannelListItemData
import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.domain.channel.dto.GetChannelFollowersResponse
import com.api.heys.helpers.findUserByToken
import com.api.heys.utils.JwtUtil
import com.api.heys.entity.*
import com.api.heys.utils.ChannelUtil
import com.api.heys.utils.CommonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import kotlin.collections.HashMap
import java.util.*

@Service
class ChannelService(
        @Autowired private val channelsRepository: IChannelsRepository,
        @Autowired private val contentsRepository: IContentsRepository,
        @Autowired private val channelUsersRepository: IChannelUserRepository,
        @Autowired private val userRepository: IUserRepository,
        @Autowired private val channelUtil: ChannelUtil,
        @Autowired private val commonUtil: CommonUtil,
        @Autowired private val jwtUtil: JwtUtil,
): IChannelService {
    @Transactional
    override fun createChannel(dto: CreateChannelData, token: String): Channels? {
        val content: Optional<Contents> = contentsRepository.findById(dto.contentId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user != null && content.isPresent) {
            val contentEntity = content.get()
            val newChannel = Channels(
                    content = contentEntity,
                    leader = user,
            )
            val newChannelView = ChannelView(newChannel)

            newChannel.channelView = newChannelView
            contentEntity.channels.add(newChannel)

            contentsRepository.save(contentEntity)

            return channelsRepository.save(newChannel)
        }
        return null
    }

    @Transactional
    override fun joinChannel(channelId: Long, token: String): Boolean {
        val channel: Optional<Channels> = channelsRepository.findById(channelId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user != null && channel.isPresent) {
            val channelEntity = channel.get()
            val channelUserRelations = ChannelUserRelations()

            // 관계 테이블 매핑 - 채널 참가 신청시, 우선 대기 채널 리스트로 보낸다.
            val channelUser = channelUsersRepository.save(
                    ChannelUsers(user, status = ChannelMemberStatus.Waiting)
            )
            channelUserRelations.channelUser = channelUser
            channelUserRelations.waitingChannel = channelEntity
            channelEntity.waitingChannelRelationUsers.add(channelUserRelations)

            channelsRepository.save(channelEntity)
            return true
        }

        return false
    }

    @Transactional(readOnly = true)
    override fun getChannels(contentId: Long): List<ChannelListItemData>? {
        val content: Optional<Contents> = contentsRepository.findById(contentId)

        if (content.isPresent) {
            val contentEntity = content.get()

            if (contentEntity.detail != null) {
                val contentDetail = contentEntity.detail!!

                return contentEntity.channels.map {
                    ChannelListItemData(
                            id = it.id,
                            name = contentDetail.name,
                            dDay = commonUtil.calculateDday(contentDetail.lastRecruitDate),
                            thumbnailUri = contentDetail.thumbnailUri ?: DefaultString.defaultThumbnailUri,
                            viewCount = it.channelView?.count ?: -1,
                            joinRemainCount = contentDetail.limitPeople.toLong() - it.joinedChannelRelationUsers.size,
                    )
                }
            }
        }
        return null
    }

    @Transactional(readOnly = true)
    override fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Int>? {
        val map = HashMap<String, Int>()
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user != null) {
            val channelUser = channelUsersRepository.getChannelUserByUserId(user.id)

            if (channelUser != null) {
                val channelUserRelations = channelUser.channelUserRelations

                map["joined"] = channelUserRelations.mapNotNull { it.joinedChannel }.size
                map["waiting"] = channelUserRelations.mapNotNull { it.joinedChannel }.size
            } else return null
        } else return null

        return map
    }

    @Transactional(readOnly = true)
    override fun getChannelFollowers(channelId: Long, token: String): GetChannelFollowersResponse {
        /* only leader user usage */
        val response = GetChannelFollowersResponse(joined = listOf(), waiting = listOf(), message = "failure")
        val channel: Optional<Channels> = channelsRepository.findById(channelId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user != null && channel.isPresent) {
            val channelEntity = channel.get()

            // check equal leader user to current user
            if (user.id == channelEntity.leader.id) {
                response.joined = channelUtil.relationsToChannelUsersData(channelEntity.joinedChannelRelationUsers)
                response.waiting = channelUtil.relationsToChannelUsersData(channelEntity.waitingChannelRelationUsers)
                response.message = "success"
            }
        }

        return response
    }
}