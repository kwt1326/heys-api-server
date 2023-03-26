package com.api.heys.helpers

import com.api.heys.domain.user.repository.UserRepository
import com.api.heys.entity.Users
import com.api.heys.utils.JwtUtil

fun findUserByToken(token: String, jwtUtil: JwtUtil, userRepository: UserRepository): Users? {
    val phone: String = jwtUtil.extractUsername(token)
    return userRepository.findUserByPhone(phone)
}