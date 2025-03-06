package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.entity.User
import com.beyoureyes.beyoureyes.jwt.JwtUtil
import com.beyoureyes.beyoureyes.mapper.UserMapper
import com.beyoureyes.beyoureyes.utils.ResponseUtil
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

    fun login(deviceId: String): ResponseEntity<out ResponseDto<out String?>> {
        val users = userMapper.findAll() // 모든 유저를 조회하여 비교

        // 저장된 유저들 중에서 device_id가 일치하는 유저를 찾음
        val user = users.find { BCrypt.checkpw(deviceId, it.deviceId) }

        // device_id가 없는 경우
        if (user == null) {
            return ResponseEntity.ok(ResponseUtil.error("존재하지 않는 사용자입니다. 정보 저장이 필요합니다.", null))
        }

        val accessToken = jwtUtil.generateAccessToken(user.userId!!)
        val refreshToken = jwtUtil.generateRefreshToken(user.userId)

        // 리프레시 토큰 DB에 저장
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
        val hashedDeviceId = BCrypt.hashpw(deviceId, BCrypt.gensalt())
        val newUser = User(deviceId = hashedDeviceId)

        // 유저를 생성하고, 생성된 userId를 반환
        val result = userMapper.insertUser(newUser)
        return if (result > 0) newUser.userId else null
    }

    fun deleteUser(userId: Long):Boolean {
        return try{
            userMapper.deleteUser(userId) > 0
        } catch (e:Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getAllUsers() = userMapper.findAll()
}