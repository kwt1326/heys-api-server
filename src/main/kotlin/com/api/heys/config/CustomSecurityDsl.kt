package com.api.heys.config

import com.api.heys.security.filter.JwtAuthenticationFilter
import com.api.heys.security.filter.JwtPerRequestFilter
import com.api.heys.utils.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

// AuthenticationManager 객체 생성 및 Custom 필터 등록 위한 DSL Class
class CustomSecurityDsl(private val jwtUtil: JwtUtil) : AbstractHttpConfigurer<CustomSecurityDsl?, HttpSecurity?>() {
    @Throws(Exception::class)
    override fun configure(builder: HttpSecurity?) {
        val authenticationManager = builder?.getSharedObject(AuthenticationManager::class.java)
        builder?.addFilterAt(JwtAuthenticationFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
        builder?.addFilterBefore(JwtPerRequestFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
    }

    companion object {
        fun customSecurityDsl(jwtUtil: JwtUtil): CustomSecurityDsl {
            return CustomSecurityDsl(jwtUtil)
        }
    }
}