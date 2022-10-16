package com.api.heys.domain.user

import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.User
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import kotlin.jvm.Throws

interface IUserService: UserDetailsService {
    fun signUp(dto: SignUpData): User?
}