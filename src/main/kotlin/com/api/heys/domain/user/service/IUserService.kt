package com.api.heys.domain.user.service

import com.api.heys.domain.user.dto.CheckMemberData
import com.api.heys.domain.user.dto.SignUpData
import org.springframework.security.core.userdetails.UserDetailsService

interface IUserService: UserDetailsService {
    fun signUp(dto: SignUpData, roles: List<String>): String?
    fun checkMember(dto: CheckMemberData): Boolean
}