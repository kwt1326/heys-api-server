package com.api.heys.config

import com.api.heys.utils.JwtUtil
import com.api.heys.utils.UserUtil
import com.api.heys.utils.ChannelUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class CommonConfig {
    @Bean
    fun jwtUtil(): JwtUtil { return JwtUtil(168) }

    @Bean
    fun userUtil(): UserUtil { return UserUtil() }

    @Bean
    fun channelUtil(): ChannelUtil { return ChannelUtil() }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}