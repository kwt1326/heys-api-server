package com.api.heys.domain.profilelink.repository

import com.api.heys.entity.UserProfileLink
import org.springframework.data.jpa.repository.JpaRepository

interface UserProfileLinkRepository : JpaRepository <UserProfileLink, Long>, UserProfileLinkCustomRepository {

    fun findAllByUserDetailId(userDetailId: Long): Set<UserProfileLink>?
}