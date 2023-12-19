package com.api.heys.config

import com.api.heys.constants.SecurityString
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedMethods()
            .allowedOrigins(
                "http://127.0.0.1:3001",
                "https://admin-dev.teamheys.com",
                "https://admin.teamheys.com"
            ).exposedHeaders(SecurityString.HEADER_AUTHORIZATION)
    }
}