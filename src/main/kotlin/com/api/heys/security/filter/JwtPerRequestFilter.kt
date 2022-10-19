package com.api.heys.security.filter

import com.api.heys.constants.SecurityString
import com.api.heys.utils.JwtUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtPerRequestFilter(private val jwtUtil: JwtUtil): OncePerRequestFilter() {
    private fun getTokenParsedAuthentication(headerValue: String): UsernamePasswordAuthenticationToken {
        val userDetail: UserDetails = jwtUtil.makeUserDetail(headerValue)

        return UsernamePasswordAuthenticationToken(userDetail, null, userDetail.authorities)
    }

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val authorizationValue: String? = request.getHeader(SecurityString.HEADER_AUTHORIZATION)

        if (authorizationValue == null || !authorizationValue.startsWith(SecurityString.PREFIX_TOKEN)) {
            return filterChain.doFilter(request, response)
        }

        val authentication: Authentication = getTokenParsedAuthentication(authorizationValue)

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }
}