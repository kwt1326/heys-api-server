package com.api.heys.domain.channel

import com.api.heys.constants.DefaultString
import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.domain.channel.dto.ChannelListItemData
import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.domain.channel.dto.GetChannelFollowersResponse
import com.api.heys.domain.channel.repository.IChannelsRepository
import com.api.heys.helpers.findUserByToken
import com.api.heys.utils.JwtUtil
import com.api.heys.entity.*
import com.api.heys.utils.ChannelUtil
import com.api.heys.utils.CommonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.HashMap
import java.util.*

@Service
class ChannelService(
        @Autowired private val IChannelsRepository: IChannelsRepository,
        @Autowired private val IContentsRepository: IContentsRepository,
        @Autowired private val channelUsersRepository: IChannelUserRepository,
        @Autowired private val userRepository: IUserRepository,
        @Autowired private val channelUtil: ChannelUtil,
        @Autowired private val commonUtil: CommonUtil,
        @Autowired private val jwtUtil: JwtUtil,
) : IChannelService {
    @Transactional
    override fun createChannel(dto: CreateChannelData, token: String): Channels? {
        val content: Optional<Contents> = IContentsRepository.findById(dto.contentId)
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return null

        if (!content.isPresent) return null

        val contentEntity = content.get()
        val newChannel = Channels(
                content = contentEntity,
                leader = user,
        )
        val newChannelView = ChannelView(newChannel)

        newChannel.channelView = newChannelView
        contentEntity.channels.add(newChannel)

        IContentsRepository.save(contentEntity)

        return IChannelsRepository.save(newChannel)
    }

    @Transactional
    override fun joinChannel(channelId: Long, token: String): Boolean {
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return false

        if (!channel.isPresent) return false

        val channelEntity = channel.get()
        val channelUserRelations = ChannelUserRelations()

        // 관계 테이블 매핑 - 채널 참가 신청시, 우선 대기 채널 리스트로 보낸다.
        val channelUser = channelUsersRepository.save(
                ChannelUsers(user, status = ChannelMemberStatus.Waiting)
        )
        channelUserRelations.channelUser = channelUser
        channelUserRelations.waitingChannel = channelEntity
        channelEntity.waitingChannelRelationUsers.add(channelUserRelations)

        IChannelsRepository.save(channelEntity)
        return true
    }

    @Transactional(readOnly = true)
    override fun getChannels(contentId: Long): List<ChannelListItemData>? {
        val content: Optional<Contents> = IContentsRepository.findById(contentId)

        if (!content.isPresent) return null

        val contentEntity = content.get()

        if (contentEntity.detail == null) return null

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

    @Transactional(readOnly = true)
    override fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Long>? {
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return null
        return IChannelsRepository.getJoinAndWaitingChannelCounts(user.id)
    }

    @Transactional(readOnly = true)
    override fun getChannelFollowers(channelId: Long, token: String): GetChannelFollowersResponse {
        /* only leader user usage */
        val response = GetChannelFollowersResponse(joined = listOf(), waiting = listOf(), message = "failure")
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return response

        if (!channel.isPresent) return response

        val channelEntity = channel.get()

        // check equal leader user to current user
        if (user.id != channelEntity.leader.id) return response

        response.joined = channelUtil.relationsToChannelUsersData(channelEntity.joinedChannelRelationUsers)
        response.waiting = channelUtil.relationsToChannelUsersData(channelEntity.waitingChannelRelationUsers)
        response.activeNotify = channelEntity.activeNotify
        response.message = "success"

        return response
    }

    @Transactional
    override fun toggleActiveNotify(channelId: Long, token: String): Boolean {
        /* only leader user usage */
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return false

        if (!channel.isPresent) return false

        val channelEntity = channel.get()

        // check equal leader user to current user
        if (user.id != channelEntity.leader.id) return false

        channelEntity.activeNotify = !channelEntity.activeNotify

        IChannelsRepository.save(channelEntity)

        return true
    }

    @Transactional
    override fun requestAllowReject(isAllow: Boolean, channelUserId: Long, channelId: Long, token: String): Boolean {
        /* only leader user usage */
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return false

        if (!channel.isPresent) return false

        val channelEntity = channel.get()

        // check equal leader user to current user
        if (user.id != channelEntity.leader.id) return false

        val channelUserRelations: ChannelUserRelations = channelEntity.waitingChannelRelationUsers
                .find { it.channelUser?.id == channelUserId } ?: return false

        channelUserRelations.waitingChannel = null
        channelEntity.waitingChannelRelationUsers.remove(channelUserRelations)

        if (isAllow) {
            channelUserRelations.joinedChannel = channelEntity
            channelEntity.joinedChannelRelationUsers.add(channelUserRelations)
        }

        channelUserRelations.channelUser?.status =
                if (isAllow) ChannelMemberStatus.Approved
                else ChannelMemberStatus.Refused

        IChannelsRepository.save(channelEntity)

        return true
    }

    @Transactional
    override fun memberAbortRequest(channelUserId: Long, channelId: Long, token: String): Boolean {
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return false

        if (!channel.isPresent) return false

        val channelEntity = channel.get()

        // check not leader (member only)
        if (user.id == channelEntity.leader.id) return false

        channelEntity.waitingChannelRelationUsers.find { it.channelUser?.id == channelId }

        // TODO

        return true
    }

    @Transactional
    override fun memberExitChannel(channelUserId: Long, channelId: Long, token: String): Boolean {
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return false

        if (!channel.isPresent) return false

        val channelEntity = channel.get()

        // check not leader (member only)
        if (user.id == channelEntity.leader.id) return false

        // TODO

        return true
    }
}