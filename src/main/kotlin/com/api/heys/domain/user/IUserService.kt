package com.api.heys.domain.user

import com.api.heys.entity.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import kotlin.jvm.Throws

interface IUserService: UserDetailsService {
    @Throws(UsernameNotFoundException::class)
    fun signUp(dto: Any?): User
}