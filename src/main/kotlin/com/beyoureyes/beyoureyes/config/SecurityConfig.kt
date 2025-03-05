package com.beyoureyes.beyoureyes.config

import com.beyoureyes.beyoureyes.jwt.JwtAuthenticationFilter
import com.beyoureyes.beyoureyes.jwt.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(private val jwtUtil: JwtUtil) {
    @Bean
    fun securityFilterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { }
            .sessionManagement{it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
            .authorizeHttpRequests {
                it
                    .requestMatchers("/", "/user/login", "/user/verify-token").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    .requestMatchers("/user/save-user", "/user/user-info", "/user/update").authenticated()
                    .requestMatchers("/food/record", "/food/today", "/food/today/summary").authenticated()
                    .anyRequest().permitAll()
            }

            .addFilterBefore(JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter::class.java)


        return http.build()
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration) : AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}