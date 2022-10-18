package com.api.heys.config

import com.api.heys.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
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

    private val jwtUtil: JwtUtil = JwtUtil(168)

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
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").anonymous()
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

//    @Bean
//    fun userDetailsService(): UserDetailsService {
//        val userDetails = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build()
//        return InMemoryUserDetailsManager(userDetails)
//    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}