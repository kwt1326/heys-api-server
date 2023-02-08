package com.api.heys.domain.channel

import com.api.heys.constants.DefaultString
import com.api.heys.constants.MessageString
import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.domain.channel.dto.*
import com.api.heys.domain.channel.repository.IChannelsRepository
import com.api.heys.domain.content.repository.IContentsRepository
import com.api.heys.helpers.findUserByToken
import com.api.heys.utils.JwtUtil
import com.api.heys.entity.*
import com.api.heys.utils.ChannelUtil
import com.api.heys.utils.CommonUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.collections.HashMap
import java.util.*

@Service
class ChannelService(
    @Autowired private val IChannelsRepository: IChannelsRepository,
    @Autowired private val IContentsRepository: IContentsRepository,
    @Autowired private val IChannelUserRelationRepository: IChannelUserRelationsRepository,
    @Autowired private val userRepository: IUserRepository,
    @Autowired private val channelUtil: ChannelUtil,
    @Autowired private val commonUtil: CommonUtil,
    @Autowired private val jwtUtil: JwtUtil,
) : IChannelService {
    @Transactional
    override fun createChannel(dto: CreateChannelData, token: String): CreateChannelResponse {
        val result = CreateChannelResponse(message = "success")
        val content: Optional<Contents> = IContentsRepository.findById(dto.contentId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            result.message = MessageString.INVALID_USER
            result.statusCode = HttpStatus.NOT_FOUND
            return result
        }

        if (!content.isPresent) {
            result.message = "컨텐츠가 존재하지 않습니다."
            result.statusCode = HttpStatus.NOT_FOUND
            return result
        }

        val contentEntity = content.get()
        val newChannel = Channels(
            content = contentEntity,
            leader = user,
        )
        val newChannelView = ChannelView(newChannel)

        newChannel.channelView = newChannelView
        contentEntity.channels.add(newChannel)

        IContentsRepository.save(contentEntity)
        IChannelsRepository.save(newChannel)

        return result
    }

    @Transactional
    override fun joinChannel(channelId: Long, token: String): ResponseEntity<JoinChannelResponse> {
        val response = JoinChannelResponse()

        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()

        if (user.id == channelEntity.leader.id) {
            response.message = "리더는 채널 참여 요청을 할 수 없습니다. (이미 참여중이기 때문)"
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        val existEntity = IChannelUserRelationRepository.getChannelUserRelByChannelUserId(user.id)

        if (existEntity != null) {
            if (existEntity.status == ChannelMemberStatus.Waiting) {
                response.message = "이미 채널에 참여 신청하셨습니다."
                return ResponseEntity.status(HttpStatus.OK).body(response)
            }
            if (existEntity.status == ChannelMemberStatus.Approved) {
                response.message = "이미 채널에 가입되어 있습니다."
                return ResponseEntity.status(HttpStatus.OK).body(response)
            }
        }

        // 관계 테이블 매핑 - 채널 참가 신청시, 우선 대기 채널 리스트로 보낸다.
        val channelUserRelations = ChannelUserRelations(user)

        channelUserRelations.status = ChannelMemberStatus.Waiting
        channelUserRelations.channel = channelEntity
        channelEntity.channelUserRelations.add(channelUserRelations)

        IChannelsRepository.save(channelEntity)

        return ResponseEntity.ok(response)
    }

    @Transactional(readOnly = true)
    override fun getChannels(contentId: Long): List<ChannelListItemData>? {
        return IChannelsRepository.getChannels(contentId).map {
            val contentDetail = it.contents.detail

            ChannelListItemData(
                id = it.id,
                name = contentDetail?.name ?: "",
                dDay = if (contentDetail?.lastRecruitDate != null) commonUtil.calculateDday(contentDetail.lastRecruitDate) else -999,
                thumbnailUri = contentDetail?.thumbnailUri ?: DefaultString.defaultThumbnailUri,
                viewCount = it.channelView?.count ?: -1,
                joinRemainCount = contentDetail?.limitPeople?.toLong()?.minus(
                    it.channelUserRelations.map {
                            itt -> itt.status == ChannelMemberStatus.Approved && itt.removedAt == null
                    }.size
                ) ?: 0,
            )
        }
    }

    @Transactional(readOnly = true)
    override fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Long>? {
        val user: Users = findUserByToken(token, jwtUtil, userRepository) ?: return null
        return IChannelsRepository.getJoinAndWaitingChannelCounts(user.id)
    }

    @Transactional(readOnly = true)
    override fun getChannelFollowers(channelId: Long, token: String): ResponseEntity<GetChannelFollowersResponse> {
        /* only leader user usage */
        val response = GetChannelFollowersResponse(joined = listOf(), waiting = listOf(), message = "failure")
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }
        
        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()

        // check equal leader user to current user
        if (user.id != channelEntity.leader.id) {
            response.message = MessageString.ONLY_USABLE_LEADER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        response.joined = channelUtil.relationsToChannelUsersData(
            channelEntity.channelUserRelations.filter {
                it.status == ChannelMemberStatus.Approved && it.removedAt == null
            }
        )
        response.waiting = channelUtil.relationsToChannelUsersData(
            channelEntity.channelUserRelations.filter {
                it.status == ChannelMemberStatus.Waiting && it.removedAt == null
            }
        )
        response.activeNotify = channelEntity.activeNotify
        response.message = "success"

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun toggleActiveNotify(channelId: Long, token: String): ResponseEntity<ChannelPutResponse> {
        val response = ChannelPutResponse(message = "success")

        /* only leader user usage */
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()

        // check equal leader user to current user
        if (user.id != channelEntity.leader.id) {
            response.message = MessageString.ONLY_USABLE_LEADER
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        channelEntity.activeNotify = !channelEntity.activeNotify

        IChannelsRepository.save(channelEntity)

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun requestAllowReject(isAllow: Boolean, userId: Long, channelId: Long, msg: String, token: String): ResponseEntity<ChannelPutResponse> {
        val response = ChannelPutResponse(message = "success")

        /* only leader user usage */
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()

        // check equal leader user to current user
        if (user.id != channelEntity.leader.id) {
            response.message = MessageString.ONLY_USABLE_LEADER
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        val channelUserRelation: ChannelUserRelations? = channelEntity.channelUserRelations
            .find { it.user.id == userId && it.removedAt == null }

        if (channelUserRelation == null) {
            response.message = "채널 승인 대기열에 유저가 없습니다."
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        // 이 서비스의 처리는 Waiting 상태만 처리 허용한다.
        if (channelUserRelation.status != ChannelMemberStatus.Waiting) {
            response.message = "대기 중인 상태의 유저만 승인/거부 수행이 가능합니다."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        if (isAllow) {
            channelUserRelation.status = ChannelMemberStatus.Approved
            channelUserRelation.channel = channelEntity
            channelUserRelation.approveRequestAt = LocalDateTime.now()
            channelEntity.channelUserRelations.add(channelUserRelation)
        } else {
            channelUserRelation.status = ChannelMemberStatus.Refused
            channelUserRelation.refuseMessage = msg
            channelUserRelation.removedAt = LocalDateTime.now()
        }

        IChannelsRepository.save(channelEntity)

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun memberAbortRequest(msg: String, userId: Long, channelId: Long, token: String): ResponseEntity<ChannelPutResponse> {
        val response = ChannelPutResponse(message = "success")
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()

        // check not leader (member only)
        if (user.id == channelEntity.leader.id) {
            response.message = "해당 기능은 채널에 접근한 멤버만 사용할 수 있습니다."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        val channelUserRelation = channelEntity.channelUserRelations.find { it.user.id == userId && it.removedAt == null }

        if (channelUserRelation == null) {
            response.message = "채널 가입 요청이 존재하지 않습니다."
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (channelUserRelation.status != ChannelMemberStatus.Waiting) {
            response.message = "채널 참가 요청 '대기(Waiting)' 상태일 경우에만 취소할 수 있습니다."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        channelUserRelation.exitMessage = msg
        channelUserRelation.status = ChannelMemberStatus.Canceled
        channelUserRelation.removedAt = LocalDateTime.now()

        IChannelsRepository.save(channelEntity)

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun memberExitChannel(msg: String, userId: Long, channelId: Long, token: String): ResponseEntity<ChannelPutResponse> {
        val response = ChannelPutResponse(message = "success")
        val channel: Optional<Channels> = IChannelsRepository.findById(channelId)
        val user: Users? = findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()

        // check not leader (member only)
        if (user.id == channelEntity.leader.id) {
            response.message = "해당 기능은 채널에 가입된 멤버만 사용할 수 있습니다."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        // 승인된 유저 대상으로 나가기 동작 수행
        val channelUserRelation = channelEntity.channelUserRelations.find { it.user.id == userId && it.removedAt == null }

        if (channelUserRelation == null) {
            response.message = "채널에 가입되어 있지 않습니다."
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (channelUserRelation.status != ChannelMemberStatus.Approved) {
            response.message = "채널 참가 '승인(Approved)' 상태일 경우에만 취소할 수 있습니다."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        channelUserRelation.exitMessage = msg
        channelUserRelation.status = ChannelMemberStatus.Exited
        channelUserRelation.removedAt = LocalDateTime.now()

        IChannelsRepository.save(channelEntity)

        return ResponseEntity.ok(response)
    }
}