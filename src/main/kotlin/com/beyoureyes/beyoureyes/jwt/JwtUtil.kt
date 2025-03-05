package com.beyoureyes.beyoureyes.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import javax.crypto.SecretKey
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*


@Component
class JwtUtil {

    companion object {
        private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    }

    fun generateAccessToken(userId: Long): String {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24시간 유효
            .signWith(secretKey)
            .compact()
    }

    fun generateRefreshToken(userId: Long): String {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7일 유효
            .signWith(secretKey)
            .compact()
    }


    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun extractUserId(token: String): Long? {
        return try {
            val claims = extractClaims(token)
            claims.subject.toLongOrNull()
        } catch (e: Exception) {
            null
        }
    }

    fun extractClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}