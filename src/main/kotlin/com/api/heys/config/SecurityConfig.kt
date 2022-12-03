package com.api.heys.config

import com.api.heys.utils.ChannelUtil
import com.api.heys.utils.CommonUtil
import com.api.heys.utils.JwtUtil
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun jwtUtil(): JwtUtil { return JwtUtil(168) }
    @Bean
    fun channelUtil(): ChannelUtil { return ChannelUtil() }
    @Bean
    fun commonUtil(): CommonUtil { return CommonUtil() }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun customOpenAPI(@Value("\${springdoc.version}") appVersion: String?): OpenAPI? {
        val schemeName = "User Token"
        return OpenAPI()
                .components(Components().addSecuritySchemes(schemeName,
                        SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")))
                .security(listOf(SecurityRequirement().addList(schemeName)))
                .info(Info().title("Heys Dev API").version(appVersion).description("Heys API Back-end"))
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors()
                .and()
                .csrf().disable() // Rest API
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/common/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers(
                        "/css/**", "/js/**", "/img/**", "/favicon.ico",
                        "/swagger-ui/**", "/api-docs", "/v3/api-docs", "/v3/api-docs/swagger-config"
                ).anonymous()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().sameOrigin() // because: h2 console render issue
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Custom Security Filter
        http.apply(CustomSecurityDsl.customSecurityDsl(jwtUtil()))

        return http.build()
    }
}