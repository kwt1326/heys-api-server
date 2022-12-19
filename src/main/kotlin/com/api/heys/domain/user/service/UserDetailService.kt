package com.api.heys.domain.user.service

import com.api.heys.constants.DefaultString
import com.api.heys.domain.channel.ChannelService
import com.api.heys.domain.interest.service.InterestService
import com.api.heys.domain.user.dto.UserDetailRequest
import com.api.heys.domain.user.dto.UserDetailResponse
import com.api.heys.domain.user.repository.UserDetailRepository
import com.api.heys.entity.IUserRepository
import com.api.heys.entity.InterestRelations
import com.api.heys.entity.UserDetail
import com.api.heys.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class UserDetailService(
    @Autowired private val jwtUtil: JwtUtil,
    @Autowired private val userDetailRepository: UserDetailRepository,
    @Autowired private val channelService: ChannelService,
    @Autowired private val interestService: InterestService
) {

    fun getMyInfo(token: String) : UserDetailResponse? {
        val phone: String = jwtUtil.extractUsername(token);
        val userWithUserDetail: UserDetail = userDetailRepository.findUserWithDetail(phone) ?: throw NullPointerException()

        val interestRelations: MutableSet<InterestRelations>? = interestService.findUserInterests(userDetailId = userWithUserDetail.id)

        val interests: MutableSet<String?>? = interestRelations
            ?.stream()
            ?.map { it.interest?.name }
            ?.collect(Collectors.toSet())

        val channels: HashMap<String, Int>? = channelService.getJoinAndWaitingChannelCounts(token)
        val joinChannelCount: Int? = if(channels?.get(DefaultString.joinChannelKey) == null) 0 else channels[DefaultString.joinChannelKey]
        val waitingChannelCount: Int? = if(channels?.get(DefaultString.waitChannelKey) == null) 0 else channels[DefaultString.waitChannelKey];

        return UserDetailResponse(
            userName = userWithUserDetail.username,
            phone = userWithUserDetail.users.phone,
            gender = userWithUserDetail.gender,
            age = userWithUserDetail.age,
            job = userWithUserDetail.job,
            profileUrl = userWithUserDetail.profilePictureUri,
            introduce = userWithUserDetail.introduceText,
            capability = userWithUserDetail.capability,
            interests = interests,
            joinChannelCount = joinChannelCount,
            waitingChannelCount = waitingChannelCount
        )
    }

    @Transactional
    fun modifyMyInfo(token: String, body: UserDetailRequest) : Unit {
        val phone: String = jwtUtil.extractUsername(token);
        val userWithUserDetail: UserDetail = userDetailRepository.findUserWithDetail(phone) ?: throw NullPointerException()

        userWithUserDetail.gender = body.gender
        userWithUserDetail.username = body.userName
        userWithUserDetail.job = body.job
        userWithUserDetail.capability = body.capability
        userWithUserDetail.introduceText = body.introduce

        interestService.modifyInterests(userWithUserDetail, body.interests)
    }
}