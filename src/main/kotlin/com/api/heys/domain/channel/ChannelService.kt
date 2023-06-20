package com.api.heys.domain.channel

import com.api.heys.constants.DefaultString
import com.api.heys.constants.MessageString
import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.constants.enums.ChannelType
import com.api.heys.constants.enums.MessageType
import com.api.heys.constants.enums.RecruitMethod
import com.api.heys.domain.channel.dto.*
import com.api.heys.domain.channel.repository.IChannelUserRelationsRepository
import com.api.heys.domain.channel.repository.IChannelsRepository
import com.api.heys.domain.content.repository.IContentsRepository
import com.api.heys.domain.interest.repository.InterestRelationRepository
import com.api.heys.domain.interest.repository.InterestRepository
import com.api.heys.domain.notification.service.NotificationService
import com.api.heys.domain.notification.vo.NotificationRequestVo
import com.api.heys.domain.user.repository.UserRepository
import com.api.heys.utils.JwtUtil
import com.api.heys.entity.*
import com.api.heys.utils.UserUtil
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
    @Autowired private val channelsRepository: IChannelsRepository,
    @Autowired private val contentsRepository: IContentsRepository,
    @Autowired private val channelUserRelRepository: IChannelUserRelationsRepository,
    @Autowired private val interestRepository: InterestRepository,
    @Autowired private val interestRelationRepository: InterestRelationRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val userUtil: UserUtil,
    @Autowired private val jwtUtil: JwtUtil,
    private val notificationService: NotificationService
) : IChannelService {
    /**
     * 컨텐츠 기반 채널 생성
     * 공모전, 대외활동 등 컨텐츠 기반 채널은 반드시 컨텐츠 엔티티와 linking 시켜야 한다.
     * */
    @Transactional
    override fun createChannel(
        dto: CreateChannelData,
        token: String,
        contentId: Long
    ): ResponseEntity<CreateChannelResponse> {
        val result = CreateChannelResponse(message = MessageString.SUCCESS_EN)
        val user: Users? = userUtil.findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            result.message = MessageString.INVALID_USER
            result.channelId = null

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }

        val content: Optional<Contents> = contentsRepository.findById(contentId)

        if (!content.isPresent) {
            result.message = MessageString.NOT_FOUND_CONTENT
            result.channelId = null

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }

        val newChannel = Channels(leader = user, type = ChannelType.Content)
        val newChannelDetail = ChannelDetail(
            channel = newChannel,
            name = dto.name,
            location = dto.location ?: "",
            contentText = dto.contentText,
            recruitText = dto.recruitText,
            online = dto.online,
            limitPeople = dto.limitPeople,
            lastRecruitDate = dto.lastRecruitDate,
            recruitMethod = dto.recruitMethod,
        )

        dto.purposes.map {
            newChannelDetail.purposes.add(ChannelPurpose(newChannelDetail, it))
        }

        dto.linkUri.map {
            newChannelDetail.links.add(ChannelLink(newChannelDetail, it))
        }

        dto.interests.map {
            // Create Interest Categories
            var interest: Interest? = interestRepository.findByName(it)
            if (interest == null) {
                interest = Interest(name = it)
            }

            // InterestRelation Linking
            val rel = InterestRelations()
            rel.interest = interest
            rel.channelDetail = newChannelDetail
            newChannelDetail.interestRelations.add(rel)
            interest.interestRelations.add(rel)
        }

        newChannel.detail = newChannelDetail

        val contentEntity = content.get()

        newChannel.contents = contentEntity
        contentEntity.channels.add(newChannel)

        val entity = contentsRepository.save(contentEntity)
        val channel = entity.channels.find { it.leader.id == user.id }

        if (channel == null) {
            result.message = MessageString.NOT_CREATED_CHANNEL
            result.channelId = null

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }

        result.channelId = channel.id
        return ResponseEntity.ok(result)
    }

    /**
     * 스터디 채널 생성
     * 스터디 채널은 컨텐츠 엔티티와 관계가 없다. 따라서 단독 채널로 생성 및 관리
     * */
    @Transactional
    override fun createChannel(dto: CreateChannelData, token: String): ResponseEntity<CreateChannelResponse> {
        val result = CreateChannelResponse(message = MessageString.SUCCESS_EN)
        val user: Users? = userUtil.findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            result.message = MessageString.INVALID_USER
            result.channelId = null

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result)
        }

        val newChannel = Channels(leader = user, type = ChannelType.Study)
        val newChannelDetail = ChannelDetail(
            channel = newChannel,
            name = dto.name,
            location = dto.location ?: "",
            contentText = dto.contentText,
            recruitText = dto.recruitText,
            online = dto.online,
            limitPeople = dto.limitPeople,
            lastRecruitDate = dto.lastRecruitDate,
            recruitMethod = dto.recruitMethod,
        )

        dto.purposes.map {
            newChannelDetail.purposes.add(ChannelPurpose(newChannelDetail, it))
        }

        dto.linkUri.map {
            newChannelDetail.links.add(ChannelLink(newChannelDetail, it))
        }

        dto.interests.map {
            // Create Interest Categories
            var interest: Interest? = interestRepository.findByName(it)
            if (interest == null) {
                interest = Interest(name = it)
            }

            // InterestRelation Linking
            val rel = InterestRelations()
            rel.interest = interest
            rel.channelDetail = newChannelDetail
            newChannelDetail.interestRelations.add(rel)
            interest.interestRelations.add(rel)
        }

        newChannel.detail = newChannelDetail

        val entity = channelsRepository.save(newChannel)

        result.channelId = entity.id
        return ResponseEntity.ok(result)
    }

    @Transactional(readOnly = true)
    override fun getChannels(
        type: ChannelType,
        params: GetChannelsParam,
        contentId: Long?
    ): ResponseEntity<GetChannelsResponse> {
        return ResponseEntity.ok(GetChannelsResponse(
            data = channelsRepository.getChannels(params, contentId, type),
            totalPage = channelsRepository.getChannelCount(params, contentId, type),
            MessageString.SUCCESS_EN
        ))
    }

    @Transactional(readOnly = true)
    override fun getChannelsAllType(params: GetChannelsParam): ResponseEntity<GetChannelsResponse> {
        return ResponseEntity.ok(GetChannelsResponse(
            data = channelsRepository.getChannels(params, null, null),
            totalPage = channelsRepository.getChannelCount(params, null, null),
            MessageString.SUCCESS_EN
        ))
    }

    @Transactional
    override fun joinChannel(channelId: Long, token: String): ResponseEntity<JoinChannelResponse> {
        val response = JoinChannelResponse()

        val channel: Optional<Channels> = channelsRepository.findById(channelId)
        val user: Users? = userUtil.findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()
        val channelDetail = channelEntity.detail

        if (user.id == channelEntity.leader.id) {
            response.message = MessageString.CANT_JOIN_REQUEST_CHANNEL_FOR_LEADER
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        if (channelDetail == null) {
            response.message = MessageString.NOT_FOUND_CHANNEL_DETAIL
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response)
        }

        if (channelEntity.channelUserRelations.count { it.removedAt == null } >= channelDetail.limitPeople) {
            response.message = MessageString.MAX_JOINED_CHANNEL
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        val existEntity = channelUserRelRepository.getChannelUserRel(user.id, channelEntity.id)

        if (existEntity != null) {
            if (existEntity.status == ChannelMemberStatus.Waiting) {
                response.message = MessageString.ALREADY_JOIN_REQUEST_CHANNEL
                return ResponseEntity.status(HttpStatus.OK).body(response)
            }
            if (existEntity.status == ChannelMemberStatus.Approved) {
                response.message = MessageString.ALREADY_JOINED_CHANNEL
                return ResponseEntity.status(HttpStatus.OK).body(response)
            }
        }

        // 관계 테이블 매핑 - 채널 참가 신청시, 우선 대기 채널 리스트로 보낸다.
        // 즉시 참가의 경우 바로 Approve 상태로 합류한다.
        val channelUserRelations = ChannelUserRelations(user)

        if (channelDetail.recruitMethod == RecruitMethod.Immediately) {
            channelUserRelations.status = ChannelMemberStatus.Approved
            channelUserRelations.approveRequestAt = LocalDateTime.now()
        } else {
            channelUserRelations.status = ChannelMemberStatus.Waiting
        }

        channelUserRelations.channel = channelEntity
        channelEntity.channelUserRelations.add(channelUserRelations)

        channelsRepository.save(channelEntity)

        val notificationRequestVo = NotificationRequestVo(
            messageType = MessageType.CHANNEL_APPROVAL_REQUEST,
            sender = user,
            receiver = channelEntity.leader,
            channel = channelEntity
        )
        notificationService.saveNotification(notificationRequestVo)

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun putChannelDetail(
        channelId: Long,
        dto: PutChannelData,
        token: String
    ): ResponseEntity<ChannelPutResponse> {
        val user = userUtil.findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ChannelPutResponse(MessageString.NOT_FOUND_USER))

        val channel = channelsRepository.findById(channelId)
        if (!channel.isPresent)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found channel"))

        val channelEntity = channel.get()
        if (user.id != channelEntity.leader.id) {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ChannelPutResponse("No Permission - Only editable leader user"))
        }

        val detail = channelEntity.detail
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found channel detail"))

        if (dto.limitPeople != null) {
            val joinedChannelUserCount =
                channelsRepository.getChannelFollowers(channelId, ChannelMemberStatus.Approved).count()
            if (joinedChannelUserCount > dto.limitPeople) {
                return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(ChannelPutResponse("Already joined users more then new limitPeople value"))
            }
            detail.limitPeople = dto.limitPeople
        }

        if (dto.name != null) detail.name = dto.name
        if (dto.online != null) detail.online = dto.online
        if (dto.location != null) detail.location = dto.location
        if (dto.contentText != null) detail.contentText = dto.contentText
        if (dto.recruitText != null) detail.recruitText = dto.recruitText
        if (dto.recruitMethod != null) detail.recruitMethod = dto.recruitMethod
        if (dto.lastRecruitDate != null) detail.lastRecruitDate = LocalDateTime.parse(dto.lastRecruitDate)

        if (dto.interests != null) {
            detail.interestRelations.clear()
            dto.interests.map {
                // Create Interest Categories
                var interest: Interest? = interestRepository.findByName(it)
                if (interest == null) {
                    interest = Interest(name = it)
                }

                // InterestRelation Linking
                val rel = interestRelationRepository.findByChannelDetailAndInterestId(detail.id, interest.id)
                    ?: InterestRelations()
                rel.interest = interest
                rel.channelDetail = detail
                detail.interestRelations.add(rel)
                interest.interestRelations.add(rel)
            }
        }

        if (dto.links != null) {
            detail.links.clear()
            dto.links.map { detail.links.add(ChannelLink(detail, it)) }
        }

        if (dto.purposes != null) {
            detail.purposes.clear()
            dto.purposes.map { detail.purposes.add(ChannelPurpose(detail, it)) }
        }

        channelsRepository.save(channelEntity)

        return ResponseEntity.ok(ChannelPutResponse(MessageString.SUCCESS_EN))
    }

    @Transactional(readOnly = true)
    override fun getChannelDetail(channelId: Long, token: String): ResponseEntity<GetChannelDetailResponse> {
        val user = userUtil.findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(GetChannelDetailResponse(message = MessageString.NOT_FOUND_USER))

        val data = channelsRepository.getChannelDetail(channelId, user.id)
            ?: return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(GetChannelDetailResponse(message = "failed found"))

        val response = GetChannelDetailResponse(
            data = data,
            message = "success"
        )
        return ResponseEntity.ok(response)
    }

    @Transactional(readOnly = true)
    override fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Long> {
        val user: Users =
            userUtil.findUserByToken(token, jwtUtil, userRepository) ?: return hashMapOf()
        return channelsRepository.getJoinAndWaitingChannelCounts(user.id) ?: return hashMapOf()
    }

    @Transactional(readOnly = true)
    override fun getMyChannels(status: ChannelMemberStatus?, token: String): ResponseEntity<GetMyChannelsResponse> {
        val response = GetMyChannelsResponse()
        val user: Users? = userUtil.findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        return ResponseEntity.ok(
            GetMyChannelsResponse(
                data = channelsRepository.getMyChannels(status, user.id),
                message = MessageString.SUCCESS_EN
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getChannelFollowers(
        channelId: Long,
        status: ChannelMemberStatus
    ): ResponseEntity<GetChannelFollowersResponse> {
        return ResponseEntity.ok(
            GetChannelFollowersResponse(
                data = channelsRepository.getChannelFollowers(channelId, status),
                message = MessageString.SUCCESS_EN
            )
        )
    }

    @Transactional
    override fun toggleActiveNotify(channelId: Long, token: String): ResponseEntity<ChannelPutResponse> {
        val response = ChannelPutResponse(message = MessageString.SUCCESS_EN)

        /* only leader user usage */
        val channel: Optional<Channels> = channelsRepository.findById(channelId)
        val user: Users? = userUtil.findUserByToken(token, jwtUtil, userRepository)

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

        channelsRepository.save(channelEntity)

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun requestAllowReject(
        isAllow: Boolean,
        userId: Long,
        channelId: Long,
        msg: String,
        leaderToken: String
    ): ResponseEntity<ChannelPutResponse> {
        val response = ChannelPutResponse(message = MessageString.SUCCESS_EN)

        /* only leader user usage */
        val channel: Optional<Channels> = channelsRepository.findById(channelId)
        val leaderUser: Users? = userUtil.findUserByToken(leaderToken, jwtUtil, userRepository)

        if (leaderUser == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()

        // check equal leader user to current user
        if (leaderUser.id != channelEntity.leader.id) {
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

        var messageType : MessageType
        if (isAllow) {
            messageType = MessageType.CHANNEL_APPROVAL
            channelUserRelation.status = ChannelMemberStatus.Approved
            channelUserRelation.channel = channelEntity
            channelUserRelation.approveRequestAt = LocalDateTime.now()
        } else {
            messageType = MessageType.CHANNEL_REFUSE
            channelUserRelation.status = ChannelMemberStatus.Refused
            channelUserRelation.refuseMessage = msg
            channelUserRelation.removedAt = LocalDateTime.now()
        }

        channelsRepository.save(channelEntity)
        val notificationRequestVo = NotificationRequestVo(
            messageType = messageType,
            sender = channelEntity.leader,
            receiver = channelUserRelation.user,
            channel = channelEntity
        )
        notificationService.saveNotification(notificationRequestVo)

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun memberAbortRequest(
        msg: String,
        channelId: Long,
        token: String
    ): ResponseEntity<ChannelPutResponse> {
        val response = ChannelPutResponse(message = MessageString.SUCCESS_EN)
        val channel: Optional<Channels> = channelsRepository.findById(channelId)
        val user: Users? = userUtil.findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()
        val userId = user.id

        // check not leader (member only)
        if (user.id == channelEntity.leader.id) {
            response.message = "해당 기능은 채널에 접근한 멤버만 사용할 수 있습니다."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        val channelUserRelation =
            channelEntity.channelUserRelations.find { it.user.id == userId && it.removedAt == null }

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

        channelsRepository.save(channelEntity)
        val notificationRequestVo = NotificationRequestVo(
            messageType = MessageType.CHANNEL_REQUEST_CANCEL,
            sender = user,
            receiver = channelEntity.leader,
            channel = channelEntity
        )
        notificationService.saveNotification(notificationRequestVo)

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun memberExitChannel(
        msg: String,
        channelId: Long,
        token: String
    ): ResponseEntity<ChannelPutResponse> {
        val response = ChannelPutResponse(message = MessageString.SUCCESS_EN)
        val channel: Optional<Channels> = channelsRepository.findById(channelId)
        val user: Users? = userUtil.findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        if (!channel.isPresent) {
            response.message = MessageString.NOT_FOUND_CHANNEL
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        val channelEntity = channel.get()
        val userId = user.id

        // check not leader (member only)
        if (user.id == channelEntity.leader.id) {
            response.message = "해당 기능은 채널에 가입된 멤버만 사용할 수 있습니다."
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
        }

        // 승인된 유저 대상으로 나가기 동작 수행
        val channelUserRelation =
            channelEntity.channelUserRelations.find { it.user.id == userId && it.removedAt == null }

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

        channelsRepository.save(channelEntity)
        val notificationRequestVo = NotificationRequestVo(
            messageType = MessageType.EXIT_CHANNEL,
            sender = user,
            receiver = channelEntity.leader,
            channel = channelEntity
        )
        notificationService.saveNotification(notificationRequestVo)

        return ResponseEntity.ok(response)
    }

    @Transactional
    override fun increaseChannelView(channelId: Long, token: String): ResponseEntity<ChannelPutResponse> {
        val channel = channelsRepository.findById(channelId)
        if (!channel.isPresent)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found content"))

        val user = userUtil.findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found User"))

        val channelEntity = channel.get()

        val isExisted = channelsRepository.getChannelView(channelId, user.id)

        if (isExisted != null) {
            return ResponseEntity.status(HttpStatus.OK).body(ChannelPutResponse("Already have seen channel"))
        }

        channelEntity.channelViews.add(ChannelView(channelEntity, user))
        channelsRepository.save(channelEntity)

        return ResponseEntity.status(HttpStatus.OK).body(ChannelPutResponse("New channel view"))
    }

    @Transactional
    override fun addBookmark(channelId: Long, token: String): ResponseEntity<ChannelPutResponse> {
        val user = userUtil.findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found User"))

        val channel = channelsRepository.findById(channelId)
        if (!channel.isPresent) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found channel"))
        }

        val isExisted = channelsRepository.getChannelBookMark(channelId, user.id)
        if (isExisted != null) {
            return ResponseEntity.status(HttpStatus.OK).body(ChannelPutResponse("Already added channel bookmark"))
        }

        val channelEntity = channel.get()

        channelEntity.channelBookMarks.add(ChannelBookMark(channelEntity, user))
        channelsRepository.save(channelEntity)

        return ResponseEntity.status(HttpStatus.OK).body(ChannelPutResponse("New channel bookmarked! : ${channelEntity.id}"))
    }

    @Transactional
    override fun removeBookmark(channelId: Long, token: String): ResponseEntity<ChannelPutResponse> {
        val user = userUtil.findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found User"))

        val channel = channelsRepository.findById(channelId)
        if (!channel.isPresent) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found channel"))
        }

        val channelEntity = channel.get()

        val bookmark = channelEntity.channelBookMarks.find { it.users!!.id == user.id }
        if (bookmark != null) channelEntity.channelBookMarks.removeIf { it.id == bookmark.id }

        channelsRepository.save(channelEntity)

        return ResponseEntity.status(HttpStatus.OK).body(ChannelPutResponse("Removed content bookmark : ${channelEntity.id}"))
    }

    @Transactional
    override fun removeBookmarks(params: PutChannelRemoveRemarksData, token: String): ResponseEntity<ChannelPutResponse> {
        val user = userUtil.findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ChannelPutResponse("Not found User"))

        val channels = channelsRepository.findAllById(params.channelIds)
        channels.forEach {
            val bookmark = it.channelBookMarks.find { it2 -> it2.users!!.id == user.id }
            if (bookmark != null) it.channelBookMarks.removeIf { it2 -> it2.id == bookmark.id }
        }

        channelsRepository.saveAll(channels)

        return ResponseEntity.status(HttpStatus.OK).body(ChannelPutResponse("Removed channel bookmarks num : ${channels.count()}"))
    }

    fun findChannelByChannelId(channelId: Long) : Channels? {
        return channelsRepository.findById(channelId).get()
    }
}