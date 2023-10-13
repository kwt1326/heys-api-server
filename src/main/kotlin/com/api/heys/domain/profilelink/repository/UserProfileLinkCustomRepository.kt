package com.api.heys.domain.profilelink.repository

interface UserProfileLinkCustomRepository {

    fun deleteAllByUserDetailId(userDetailId: Long)
}