package com.api.heys.security.filter

import com.api.heys.constants.SecurityString
import com.api.heys.utils.JwtUtil

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(
        authenticationManager: AuthenticationManager?,
        private val jwtUtil: JwtUtil,
): UsernamePasswordAuthenticationFilter(authenticationManager) {
    init {
        setFilterProcessesUrl(SecurityString.AUTHENTICATION_FILTER_URL)
    }

    // 인증 시도
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val username: String? = request?.getParameter("username")
        val password: String? = request?.getParameter("password")

        val authToken: Authentication = UsernamePasswordAuthenticationToken(username, password)

        return authenticationManager.authenticate(authToken)
    }

    // 인증 성공 시 토큰 반환
    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        val user: User? = authResult?.principal as User?

        if (user !== null) {
            val username: String = user.username
            val authorities: List<String> = user.authorities.map { it.authority } // Roles
            val token = jwtUtil.createJwt(username, authorities)

            response?.addHeader(SecurityString.HEADER_AUTHORIZATION, SecurityString.PREFIX_TOKEN + token)
        }
    }
}