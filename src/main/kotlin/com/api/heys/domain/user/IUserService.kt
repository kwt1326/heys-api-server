package com.api.heys.domain.user

import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.Users
import org.springframework.security.core.userdetails.UserDetailsService

interface IUserService: UserDetailsService {
    fun signUp(dto: SignUpData, roles: List<String>): String?
}