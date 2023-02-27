package com.api.heys.domain.user.service

import com.api.heys.constants.DefaultString
import com.api.heys.domain.channel.ChannelService
import com.api.heys.domain.interest.service.InterestService
import com.api.heys.domain.profilelink.service.UserProfileLinkService
import com.api.heys.domain.user.dto.OtherUserDetailResponse
import com.api.heys.domain.user.dto.UserDetailRequest
import com.api.heys.domain.user.dto.UserDetailResponse
import com.api.heys.domain.user.dto.UserDetailSearchDto
import com.api.heys.domain.user.repository.UserDetailRepository
import com.api.heys.entity.InterestRelations
import com.api.heys.entity.UserDetail
import com.api.heys.utils.JwtUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserDetailService(
    private val jwtUtil: JwtUtil,
    private val userDetailRepository: UserDetailRepository,
    private val channelService: ChannelService,
    private val interestService: InterestService,
    private val userProfileLinkService: UserProfileLinkService
) {

    fun getMyInfo(token: String) : UserDetailResponse? {
        val phone: String = jwtUtil.extractUsername(token);
        val userDetailSearchDto = UserDetailSearchDto(phone = phone)
        val findUserDetail: UserDetail = findUserDetail(userDetailSearchDto) ?: throw NullPointerException()

        val interestRelations: Set<InterestRelations> = interestService.findUserInterests(userDetailId = findUserDetail.id)

        val interests: Set<String?> = interestRelations
            .mapNotNull { it.interest?.name }
            .toSet()

        val userProfileLinks = userProfileLinkService.findUserProfileLink(userDetailId = findUserDetail.id)

        val profileLinks: Set<String> = userProfileLinks
            .map{ it.linkUrl }
            .toSet()

        val channels: HashMap<String, Long>? = channelService.getJoinAndWaitingChannelCounts(token)
        val joinChannelCount: Long? = if(channels?.get(DefaultString.joinChannelKey) == null) 0 else channels[DefaultString.joinChannelKey]
        val waitingChannelCount: Long? = if(channels?.get(DefaultString.waitChannelKey) == null) 0 else channels[DefaultString.waitChannelKey];

        return UserDetailResponse(
            userName = findUserDetail.username,
            phone = findUserDetail.users.phone,
            gender = findUserDetail.gender,
            birthDate = findUserDetail.birthDate,
            job = findUserDetail.job,
            introduce = findUserDetail.introduceText,
            capability = findUserDetail.capability,
            userPersonality = findUserDetail.userPersonality,
            interests = interests,
            profileLinks = profileLinks,
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
        userWithUserDetail.userPersonality = body.userPersonality

        interestService.modifyInterests(userWithUserDetail, body.interests)
        userProfileLinkService.modifyUserProfileLink(userWithUserDetail, body.profileLinks)
    }

    fun findOtherUserDetail(userId : Long) : OtherUserDetailResponse? {

        val userDetailSearchDto = UserDetailSearchDto(userId = userId)
        val findUserDetail = findUserDetail(userDetailSearchDto) ?: throw NullPointerException()

        val interestRelations: Set<InterestRelations> = interestService.findUserInterests(userDetailId = findUserDetail.id)

        val interests: Set<String> = interestRelations
            .mapNotNull { it.interest?.name }
            .toSet()

        val userProfileLinks = userProfileLinkService.findUserProfileLink(userDetailId = findUserDetail.id)

        val profileLinks: Set<String> = userProfileLinks
            .map{ it.linkUrl }
            .toSet()

        return OtherUserDetailResponse(
            userName = findUserDetail.username,
            gender = findUserDetail.gender,
            job = findUserDetail.job,
            introduce = findUserDetail.introduceText,
            capability = findUserDetail.capability,
            userPersonality = findUserDetail.userPersonality,
            interests = interests,
            profileLinks = profileLinks,
        )
    }

    fun findUserDetail(userDetailSearchDto: UserDetailSearchDto) : UserDetail? {
        return userDetailRepository.findUserDetail(userDetailSearchDto);
    }
}