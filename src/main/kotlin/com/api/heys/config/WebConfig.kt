package com.api.heys.config

import com.api.heys.constants.SecurityString
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",
                "https://admin-dev.teamheys.com",
                "https://admin.teamheys.com"
            ).exposedHeaders(SecurityString.HEADER_AUTHORIZATION)
    }
}