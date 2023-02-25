package com.api.heys.domain.user.service

import com.api.heys.constants.DefaultString
import com.api.heys.domain.channel.ChannelService
import com.api.heys.domain.interest.service.InterestService
import com.api.heys.domain.user.dto.OtherUserDetailResponse
import com.api.heys.domain.user.dto.UserDetailRequest
import com.api.heys.domain.user.dto.UserDetailResponse
import com.api.heys.domain.user.dto.UserDetailSearchDto
import com.api.heys.domain.user.repository.UserDetailRepository
import com.api.heys.entity.InterestRelations
import com.api.heys.entity.UserDetail
import com.api.heys.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
        val userDetailSearchDto = UserDetailSearchDto(phone = phone)
        val findUserDetail: UserDetail = findUserDetail(userDetailSearchDto) ?: throw NullPointerException()

        val interestRelations: MutableSet<InterestRelations>? = interestService.findUserInterests(userDetailId = findUserDetail.id)

        val interests: Set<String?>? = interestRelations
            ?.mapNotNull { it.interest?.name }
            ?.toSet()

        val channels: HashMap<String, Long> = channelService.getJoinAndWaitingChannelCounts(token)
        val joinChannelCount: Long? = if(channels?.get(DefaultString.joinChannelKey) == null) 0 else channels[DefaultString.joinChannelKey]
        val waitingChannelCount: Long? = if(channels?.get(DefaultString.waitChannelKey) == null) 0 else channels[DefaultString.waitChannelKey];

        return UserDetailResponse(
            userName = findUserDetail.username,
            phone = findUserDetail.users.phone,
            gender = findUserDetail.gender,
            age = findUserDetail.age,
            job = findUserDetail.job,
            profileUrl = findUserDetail.profilePictureUri,
            introduce = findUserDetail.introduceText,
            capability = findUserDetail.capability,
            interests = interests,
            joinChannelCount = joinChannelCount,
            waitingChannelCount = waitingChannelCount
        )
    }

    @Transactional
    fun modifyMyInfo(token: String, body: UserDetailRequest) {
        val phone: String = jwtUtil.extractUsername(token);
        val userDetailSearchDto = UserDetailSearchDto(phone = phone)
        val userWithUserDetail: UserDetail = findUserDetail(userDetailSearchDto) ?: throw NullPointerException()

        userWithUserDetail.gender = body.gender
        userWithUserDetail.username = body.userName
        userWithUserDetail.job = body.job
        userWithUserDetail.capability = body.capability
        userWithUserDetail.introduceText = body.introduce

        interestService.modifyInterests(userWithUserDetail, body.interests)
    }

    fun findOtherUserDetail(userId : Long) : OtherUserDetailResponse? {

        val userDetailSearchDto = UserDetailSearchDto(userId = userId)
        val findUserDetail = findUserDetail(userDetailSearchDto) ?: throw NullPointerException()

        val interestRelations: MutableSet<InterestRelations>? = interestService.findUserInterests(userDetailId = findUserDetail.id)

        val interests: Set<String?>? = interestRelations
            ?.mapNotNull { it.interest?.name }
            ?.toSet()

        return OtherUserDetailResponse(
            userName = findUserDetail.username,
            gender = findUserDetail.gender,
            job = findUserDetail.job,
            profileUrl = findUserDetail.profilePictureUri,
            introduce = findUserDetail.introduceText,
            capability = findUserDetail.capability,
            interests = interests,
        )
    }

    fun findUserDetail(userDetailSearchDto: UserDetailSearchDto) : UserDetail? {
        return userDetailRepository.findUserDetail(userDetailSearchDto);
    }
}