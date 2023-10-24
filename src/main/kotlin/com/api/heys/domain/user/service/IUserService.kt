package com.api.heys.domain.user.service

import com.api.heys.domain.user.dto.CheckMemberData
import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.domain.user.dto.SignUpResponse
import com.api.heys.domain.user.dto.WithdrawalUserRequest
import com.api.heys.entity.Users
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetailsService

interface IUserService: UserDetailsService {
    fun <T: SignUpData>signUp(dto: T, role: String): ResponseEntity<SignUpResponse>
    fun withDrawal(token : String, withDrawalUserRequest: WithdrawalUserRequest): ResponseEntity<Boolean>
    fun checkMember(dto: CheckMemberData): Boolean

    fun findByPhone(phone: String): Users?
}