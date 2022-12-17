package com.api.heys.domain.user.repository

import com.api.heys.entity.UserDetail


interface UserDetailCustomRepository {

    fun findUserWithDetail(phone: String) : UserDetail?
}