package com.api.heys.utils

import com.api.heys.domain.user.service.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

open class UserUtil {
    fun isLogging(userService: UserService): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        val users = userService.loadUserByUsername(
            if (authentication.principal is UserDetails)
                    (authentication.principal as UserDetails).username
            else authentication.principal.toString()
        )
        return users != null
    }

    fun logout(): Boolean {
        return false
    }
}