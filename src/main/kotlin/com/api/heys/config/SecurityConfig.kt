package com.api.heys.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

//    @Bean
//    fun webSecurityCustomizer(): WebSecurityCustomizer? {
//        return WebSecurityCustomizer { web: WebSecurity ->
//            web // debug = true only development used
//                    .ignoring()
//                    .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "**/favicon.ico")
//        }
//    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/common/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico").anonymous()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .httpBasic().disable()

        return http.build();
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