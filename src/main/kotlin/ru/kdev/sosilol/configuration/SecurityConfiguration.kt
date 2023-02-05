package ru.kdev.sosilol.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .cors().and()
        .csrf().and()
        .securityMatcher("*")
        .anonymous()
        .and()
        .build()
}