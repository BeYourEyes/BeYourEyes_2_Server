package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.LoginRequestDto
import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.service.UserService
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
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

    @DeleteMapping("/delete")
    fun deleteUser(): ResponseEntity<ResponseDto<Unit>> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        return if( userService.deleteUser(userId)) {
            ResponseEntity.ok(ResponseUtil.success("사용자 계정이 삭제되었습니다.", Unit))
        } else {
            ResponseEntity.ok(ResponseUtil.error("사용자 계정 삭제 실패", Unit))
        }
    }

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<ResponseDto<Any>> {
        val data = userService.getAllUsers()
        return ResponseEntity.ok(ResponseUtil.success("모든 사용자 데이터 조회 성공", data))
    }

}