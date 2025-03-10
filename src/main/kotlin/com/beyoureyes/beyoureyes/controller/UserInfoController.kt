package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.entity.Allergy
import com.beyoureyes.beyoureyes.entity.Disease
import com.beyoureyes.beyoureyes.jwt.JwtUtil
import com.beyoureyes.beyoureyes.service.UserInfoService
import com.beyoureyes.beyoureyes.service.UserService
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

/* -
* 사용자 정보 저장 - 0 (세부 처리 필요) / 일 년 로그인안한 사람 - 0
* 사용자 정보 수정 - 0
* 해당 사용자 -> 오늘 섭취 저장 / firebase 연결 서버에서!복잡할까?0? - 한국 자정
* 해당 사용자 -> 오늘 섭취 저장 / 한국 자정
*
* 익명 로그인 - 0
* 안드로이드 로컬 저장소 토큰을 저장 - 0
* */
@RestController
@RequestMapping("/user")
class UserInfoController(
    private val userInfoService: UserInfoService,
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/save-user")
    fun saveUserInfo(@RequestBody request: Map<String, Any>): ResponseEntity<out ResponseDto<out String?>> {
        val deviceId = request["device_id"] as? String
            ?: return ResponseEntity.badRequest().body(ResponseUtil.error("device_id가 필요합니다.", null))

        val userBirth = request["user_birth"] as? String
            ?: return ResponseEntity.badRequest().body(ResponseUtil.error("생년월일이 필요합니다.", null))

        val userGender = request["user_gender"] as? Int
            ?: return ResponseEntity.badRequest().body(ResponseUtil.error("성별이 필요합니다.", null))

        val userNickname = request["user_nickname"] as? String
            ?: return ResponseEntity.badRequest().body(ResponseUtil.error("닉네임이 필요합니다.", null))

        val allergyMap = request["allergy"] as? Map<String, Boolean>
            ?: return ResponseEntity.badRequest().body(ResponseUtil.error("알러지 정보가 필요합니다.", null))

        val diseaseMap = request["disease"] as? Map<String, Boolean>
            ?: return ResponseEntity.badRequest().body(ResponseUtil.error("질환 정보가 필요합니다.", null))

        // 1. 유저 생성 및 user_id 가져오기
        val userId = userService.createUser(deviceId)
            ?: return ResponseEntity.status(500).body(ResponseUtil.error("사용자 생성 실패", null))

        // 2. 알러지 및 질환 데이터 매핑
        val allergy = Allergy(
            userId = userId,
            buckwheat = allergyMap["buckwheat"] ?: false,
            wheat = allergyMap["wheat"] ?: false,
            soybean = allergyMap["soybean"] ?: false,
            peanut = allergyMap["peanut"] ?: false,
            walnut = allergyMap["walnut"] ?: false,
            pineNut = allergyMap["pineNut"] ?: false,
            sulfurDioxide = allergyMap["sulfurDioxide"] ?: false,
            peach = allergyMap["peach"] ?: false,
            tomato = allergyMap["tomato"] ?: false,
            egg = allergyMap["egg"] ?: false,
            milk = allergyMap["milk"] ?: false,
            shrimp = allergyMap["shrimp"] ?: false,
            mackerel = allergyMap["mackerel"] ?: false,
            squid = allergyMap["squid"] ?: false,
            crab = allergyMap["crab"] ?: false,
            shellfish = allergyMap["shellfish"] ?: false,
            pork = allergyMap["pork"] ?: false,
            beef = allergyMap["beef"] ?: false,
            chicken = allergyMap["chicken"] ?: false
        )

        val disease = Disease(
            userId = userId,
            diabetes = diseaseMap["diabetes"] ?: false,
            hypertension = diseaseMap["hypertension"] ?: false,
            hyperlipidemia = diseaseMap["hyperlipidemia"] ?: false
        )

        // 3. 사용자 정보 저장
        if (userInfoService.saveUserInfo(userId, userBirth, userGender, userNickname, allergy, disease)) {
            val accessToken = jwtUtil.generateAccessToken(userId)
            val refreshToken = jwtUtil.generateRefreshToken(userId)

            // Refresh Token을 DB에 저장
            userService.updateRefreshToken(userId, refreshToken)

            return ResponseEntity.ok(ResponseUtil.success("사용자 정보가 저장되었습니다.", accessToken))
        } else {
            return ResponseEntity.status(500).body(ResponseUtil.error("사용자 정보 저장 실패", null))
        }
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
            "userInfo" to userInfo,
            "allergy" to allergyData,
            "disease" to diseaseData
        )

        return ResponseEntity.ok(ResponseUtil.success("사용자 정보 조회 성공했습니다.", responseData))
    }

    @PatchMapping("/update")
    fun updateUserInfo(@RequestBody request: Map<String, Any>) :ResponseEntity<ResponseDto<Unit>> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long

        val userBirth = request["user_birth"] as? String
        val userGender = request["user_gender"] as? Int
        val userNickname = request["user_nickname"] as? String

        val allergyMap = request["allergy"] as? Map<String, Boolean>
        val diseaseMap = request["disease"] as? Map<String, Boolean>

        return if (userInfoService.updateUserInfo(userId, userBirth, userGender, userNickname, allergyMap, diseaseMap )) {
            ResponseEntity.ok(ResponseUtil.success("사용자 정보가 업데이트 되었습니다.", Unit))

        } else {
            ResponseEntity.status(500).body(ResponseUtil.error("사용자 정보 업데이트 실패했습니다.", Unit))
        }
    }
}