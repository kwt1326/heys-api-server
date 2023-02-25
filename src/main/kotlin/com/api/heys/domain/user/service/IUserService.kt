package com.api.heys.domain.user.service

import com.api.heys.domain.user.dto.CheckMemberData
import com.api.heys.domain.user.dto.SignUpData
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetailsService

interface IUserService: UserDetailsService {
    fun <T: SignUpData>signUp(dto: T, role: String): String?
    fun withDrawal(id: Number, role: String): ResponseEntity<Boolean>
    fun checkMember(dto: CheckMemberData): Boolean
}