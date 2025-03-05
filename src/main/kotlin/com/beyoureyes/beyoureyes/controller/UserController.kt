package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.LoginRequestDto
import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.service.UserService
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "사용자 정보 API")
class UserController(private val userService: UserService) {


    @PostMapping("/login")
    fun postLogin(@Valid @RequestBody request: LoginRequestDto): ResponseEntity<out ResponseDto<out String?>> {
        val deviceId = request.device_id
        if (deviceId.isBlank()) {
            return ResponseEntity.badRequest().body(ResponseUtil.error("device_id가 빈값입니다.", null))
        }

        // UserService에서 직접 ResponseEntity를 반환하도록 수정
        return userService.login(deviceId)
    }

    @PostMapping("/refresh-token")
    fun refreshAccessToken(
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<ResponseDto<String>> {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(ResponseUtil.error("Authorization 헤더가 필요합니다.", ""))
        }

        val accessToken = authHeader.substring(7)
        val newAccessToken = userService.refreshAccessToken(accessToken)
            ?: return ResponseEntity.status(401).body(ResponseUtil.error("유효하지 않은 Refresh Token입니다.", ""))

        return ResponseEntity.ok(ResponseUtil.success("Access Token 재발급 성공", newAccessToken))
    }

    @PostMapping("/verify-token")
    fun postVerify(@RequestBody request: Map<String, String>): ResponseEntity<ResponseDto<String>> {
        val token = request["token"]
            ?: return ResponseEntity.badRequest().body(ResponseUtil.error("token이 필요합니다.", ""))

        return if (userService.verifyToken(token)) {
            ResponseEntity.ok(ResponseUtil.success("유효한 토큰입니다.", token))
        } else {
            ResponseEntity.badRequest().body(ResponseUtil.error("토큰이 유효하지 않습니다.", ""))
        }
    }
}