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
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

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
                    .requestMatchers("/", "/user/login", "/user/verify-token", "/user/save-user").permitAll()
                    .requestMatchers("/update/allergy/allergy", "/food/daily-food", "/update/disease/disease", "/user/users", "/user/info").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/user/check-nickname").permitAll()
                    .requestMatchers("/user/user-info", "/user/update", "/update/disease", "/update/allergy").authenticated()
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

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration().apply {
            addAllowedOriginPattern("*") // 모든 Origin 허용 (임시로)
            addAllowedMethod("*") // 모든 HTTP 메서드 허용 (GET, POST, PUT, DELETE 등)
            addAllowedHeader("*") // 모든 헤더 허용
            allowCredentials = true // 쿠키 및 인증정보 허용
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}