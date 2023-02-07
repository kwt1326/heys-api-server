package com.api.heys.domain.profilelink.service

import com.api.heys.domain.profilelink.repository.UserProfileLinkRepository
import com.api.heys.entity.QUserProfileLink
import com.api.heys.entity.UserDetail
import com.api.heys.entity.UserProfileLink
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IndexOutOfBoundsException

@Service
@Transactional(readOnly = true)
class UserProfileLinkService (
    private val userProfileLinkRepository: UserProfileLinkRepository
){
    val profileMaxCount = 5;

    fun findUserProfileLink(userDetailId: Long) : Set<UserProfileLink> {
        val userProfiles : Set<UserProfileLink> = userProfileLinkRepository.findAllByUserDetailId(userDetailId = userDetailId) ?: return setOf()

        if (userProfiles.size > profileMaxCount) {
            throw IndexOutOfBoundsException("User Profile Count is more than MAX");
        }
        return userProfiles;
    }

    @Transactional
    fun modifyUserProfileLink(userDetail: UserDetail, profileLinks: Set<String>) {

        if (profileLinks.size > profileMaxCount) {
            throw IndexOutOfBoundsException("User Profile Count is more than MAX");
        }
        deleteUserProfileLink(userDetailId = userDetail.id)
        insertUserProfileLink(userDetail, profileLinks)
    }

    @Transactional
    fun insertUserProfileLink(userDetail: UserDetail, profileLinks: Set<String>) {
        val userProfileLinks: Set<UserProfileLink> = profileLinks
            .map { UserProfileLink(userDetail = userDetail, linkUrl = it) }
            .toSet()

        userDetail.profileLink = userProfileLinks;
        userProfileLinkRepository.saveAll(userProfileLinks)
    }

    @Transactional
    fun deleteUserProfileLink(userDetailId: Long) {
        userProfileLinkRepository.deleteAllByUserDetailId(userDetailId = userDetailId)
    }
}