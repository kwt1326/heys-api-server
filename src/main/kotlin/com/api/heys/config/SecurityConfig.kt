package com.api.heys.config

import com.api.heys.utils.JwtUtil
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig(
        @Autowired private val jwtUtil: JwtUtil
) {
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
                .antMatchers("/app/code").permitAll()
                .antMatchers("/admin/**").permitAll()
                .antMatchers(
                        "/css/**", "/js/**", "/assets/**", "/favicon.ico",
                        "/swagger-ui/**", "/api-docs", "/v3/api-docs", "/v3/api-docs/swagger-config"
                ).anonymous()
                .antMatchers("/managements/**").anonymous()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().sameOrigin() // because: h2 console render issue
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        // Custom Security Filter
        http.apply(CustomSecurityDsl.customSecurityDsl(jwtUtil))

        return http.build()
    }
}