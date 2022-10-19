package com.api.heys.security.domain

import com.api.heys.entity.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

class CustomUser(
        username: String,
        password: String,
        authorities: List<SimpleGrantedAuthority>
): User(
        username,
        password,
        authorities
) {
    private val serialVersionUID = 1L

    private val authEntity: Authentication? = null

    fun getAuthEntity(): Authentication? { return authEntity }
}