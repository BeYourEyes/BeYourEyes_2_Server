package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.dto.SaveUserRequestDto
import com.beyoureyes.beyoureyes.entity.Allergy
import com.beyoureyes.beyoureyes.entity.Disease
import com.beyoureyes.beyoureyes.jwt.JwtUtil
import com.beyoureyes.beyoureyes.service.UserInfoService
import com.beyoureyes.beyoureyes.service.UserService
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("/user")
class UserInfoController(
    private val userInfoService: UserInfoService,
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {
    @PostMapping("/save-user")
    fun saveUserInfo(@Valid @RequestBody request: SaveUserRequestDto): ResponseEntity<out ResponseDto<out String?>> {
        val userId = userService.createUser(request.device_id)
            ?: return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.error("사용자 생성 실패", null))

        val allergy = Allergy(
            userId = userId,
            buckwheat = request.allergy.buckwheat,
            wheat = request.allergy.wheat,
            soybean = request.allergy.soybean,
            peanut = request.allergy.peanut,
            walnut = request.allergy.walnut,
            pineNut = request.allergy.pineNut,
            sulfurDioxide = request.allergy.sulfurDioxide,
            peach = request.allergy.peach,
            tomato = request.allergy.tomato,
            egg = request.allergy.egg,
            milk = request.allergy.milk,
            shrimp = request.allergy.shrimp,
            mackerel = request.allergy.mackerel,
            squid = request.allergy.squid,
            crab = request.allergy.crab,
            shellfish = request.allergy.shellfish,
            pork = request.allergy.pork,
            beef = request.allergy.beef,
            chicken = request.allergy.chicken
        )

        val disease = Disease(
            userId = userId,
            diabetes = request.disease.diabetes,
            hypertension = request.disease.hypertension,
            hyperlipidemia = request.disease.hyperlipidemia
        )

        val saved = userInfoService.saveUserInfo(
            userId = userId,
            userBirth = request.user_birth,
            userGender = request.user_gender,
            userNickname = request.user_nickname,
            allergy = allergy,
            disease = disease
        )

        if (!saved) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseUtil.error("사용자 정보 저장 실패", null))
        }

        val accessToken = jwtUtil.generateAccessToken(userId)
        val refreshToken = jwtUtil.generateRefreshToken(userId)
        userService.updateRefreshToken(userId, refreshToken)

        return ResponseEntity.ok(ResponseUtil.success("사용자 정보가 저장되었습니다.", accessToken))
    }


    @GetMapping("/user-info")
    fun getUserInfo(): ResponseEntity<ResponseDto<Map<String, Any?>>> {
        // JWT 필터를 통해 인증된 사용자 ID 가져오기
        val userId = SecurityContextHolder.getContext().authentication.principal.toString().toLong()


        val (userInfo, allergyData, diseaseData) = userInfoService.getUserDetails(userId)

        if (userInfo == null) {
            return ResponseEntity.status(404).body(ResponseUtil.error("사용자 정보가 없습니다.", emptyMap()))
        }

        val responseData: Map<String, Any?> = mapOf(
            "user_info" to mapOf(
                "user_id" to userInfo.userId,
                "user_birth" to userInfo.userBirth,
                "user_gender" to userInfo.userGender,
                "user_nickname" to userInfo.userNickname
            ),
            "allergy" to allergyData,
            "disease" to diseaseData
        )

        return ResponseEntity.ok(ResponseUtil.success("사용자 정보 조회 성공했습니다.", responseData))
    }

    @PatchMapping("/update/user-info")
    fun updateUserInfo2(@RequestBody request: Map<String, Any>): ResponseEntity<ResponseDto<Any?>> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val userBirth = request["user_birth"] as? String
        val userGender = request["user_gender"] as? Int
        val userNickname = request["user_nickname"] as? String

        return if (userInfoService.updateUserInfo(userId, userBirth, userGender, userNickname)) {
            ResponseEntity.ok(ResponseUtil.success("사용자 정보가 업데이트 되었습니다.", null))
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtil.error("사용자 정보 업데이트 실패했습니다.", null))
        }
    }



    @GetMapping("/check-nickname")
    fun checkNickname(@RequestParam nickname: String): ResponseEntity<ResponseDto<Boolean>> {
        if (nickname.isBlank()) {
            return ResponseEntity.badRequest().body(ResponseUtil.error("닉네임이 필요합니다.", false))
        }

        val isAvailable = userInfoService.isNicknameAvaliable(nickname)

        return if (isAvailable) {
            ResponseEntity.ok(ResponseUtil.success("사용 가능한 닉네임입니다.", true))
        } else {
            ResponseEntity.ok(ResponseUtil.success("이미 사용 중인 닉네임입니다.", false))
        }
    }
    @GetMapping("/info")
    fun getAllUserInfo(): ResponseEntity<ResponseDto<Any>> {
        val data = userInfoService.getAllUserInfo()
        return ResponseEntity.ok(ResponseUtil.success("모든 사용자 정보 조회 성공", data))
    }
}