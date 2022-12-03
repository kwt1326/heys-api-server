package com.api.heys.helpers

import com.api.heys.entity.IUserRepository
import com.api.heys.entity.Users
import com.api.heys.utils.JwtUtil

fun findUserByToken(token: String, jwtUtil: JwtUtil, userRepository: IUserRepository): Users? {
    val phone: String = jwtUtil.extractUsername(token)
    return userRepository.findByPhone(phone)
}