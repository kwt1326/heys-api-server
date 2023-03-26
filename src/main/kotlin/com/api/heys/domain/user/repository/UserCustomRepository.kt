package com.api.heys.domain.user.repository

import com.api.heys.entity.Users

interface UserCustomRepository {
    fun findUserByPhone(phone: String): Users?
}