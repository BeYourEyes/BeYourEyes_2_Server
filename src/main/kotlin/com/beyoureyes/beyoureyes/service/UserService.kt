package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.entity.User
import com.beyoureyes.beyoureyes.jwt.JwtUtil
import com.beyoureyes.beyoureyes.mapper.UserMapper
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper, private val jwtUtil: JwtUtil) {

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun deactivateInactiveUsers() {
        val affectedRows = userMapper.deactivateInactiveUsers()
        println("1년 이상 로그인하지 않은 사용자 $affectedRows 명 비활성화 되었습니다.")
    }

    // v2/user/login : DB에 deviceId가 저장되어있는지 확인
    fun login(deviceId: String): ResponseEntity<out ResponseDto<out String?>> {

        val users = userMapper.findAll()

        val user = users
            .filterNotNull()
            .find { it.deviceId == deviceId }

        if (user == null) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ResponseUtil.no_data("일치하는 device_id가 없습니다.", null))
        }

        if (user.deletedAt != null) {
            userMapper.reactivateUser(user.userId!!)

            val accessToken = jwtUtil.generateAccessToken(user.userId)
            val refreshToken = jwtUtil.generateRefreshToken(user.userId)
            userMapper.updateRefreshToken(user.userId, refreshToken)

            return ResponseEntity.ok(
                ResponseUtil.active("휴면 계정이 다시 활성화되었습니다.", accessToken)
            )
        }

        val accessToken = jwtUtil.generateAccessToken(user.userId!!)
        val refreshToken = jwtUtil.generateRefreshToken(user.userId)
        userMapper.updateRefreshToken(user.userId, refreshToken)

        return ResponseEntity.ok(ResponseUtil.success("로그인 성공", accessToken))

    }



    fun refreshAccessToken(accessToken: String): String? {
        // 만료된 Access Token에서도 사용자 ID를 추출할 수 있어야 함
        val userId = jwtUtil.extractUserId(accessToken) ?: return null

        // DB에 저장된 Refresh Token을 가져옴
        val storedRefreshToken = userMapper.getRefreshToken(userId) ?: return null

        // Refresh Token의 유효성 검증
        if (!jwtUtil.validateToken(storedRefreshToken)) {
            return null
        }

        // 새로운 Access Token 발급
        return jwtUtil.generateAccessToken(userId)
    }

    fun verifyToken(token : String): Boolean {
        return jwtUtil.validateToken(token)
    }

    fun getUserIdToken(token : String) : Long? {
        return jwtUtil.extractUserId(token)
    }

    fun updateRefreshToken(userId: Long, refreshToken: String) {
        userMapper.updateRefreshToken(userId, refreshToken)
    }

    fun createUser(deviceId: String): Long? {
        val existingUser = userMapper.findByDeviceId(deviceId)
        if (existingUser != null) {
            return -1
        }

        val newUser = User(deviceId = deviceId)
        val result = userMapper.insertUser(newUser)

        return if (result > 0) newUser.userId else null
    }



    fun deleteUser(userId: Long): Boolean {
        return try {
            val result = userMapper.deleteUser(userId)
            result > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }


    fun getAllUsers() = userMapper.findAll()
}