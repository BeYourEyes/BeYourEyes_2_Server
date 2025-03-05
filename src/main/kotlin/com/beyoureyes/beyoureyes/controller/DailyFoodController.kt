package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.DailyFoodRequestDto
import com.beyoureyes.beyoureyes.dto.DailyFoodResponseDto
import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.service.DailyFoodService
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/food")
@Tag(name = "Daily Food API", description = "일일 음식 기록 API")
class DailyFoodController(
    private val dailyFoodService: DailyFoodService,
    private val objectMapper: ObjectMapper
) {

    @PostMapping("/record")
    @Operation(summary = "음식 섭취 기록 저장", description = "이미지와 영양소 정보를 함께 저장합니다.")
    fun recordDailyFood(
        @RequestParam("image") image: MultipartFile, // 이미지 파일
        @RequestParam("foodData") foodData: String   // JSON 데이터
    ): ResponseEntity<ResponseDto<String>> {

        // 인증된 사용자 ID 가져오기
        val userId = SecurityContextHolder.getContext().authentication.principal.toString().toLong()

        // JSON 문자열을 객체로 변환
        val foodDto = objectMapper.readValue(foodData, DailyFoodRequestDto::class.java)

        // 데이터 저장 및 이미지 URL 반환
        val imageUrl = dailyFoodService.saveDailyFood(userId, image, foodDto)

        return ResponseEntity.ok(ResponseUtil.success("음식 섭취 기록이 저장되었습니다.", imageUrl))
    }

    @GetMapping("/today")
    @Operation(summary = "오늘 섭취한 모든 음식 기록 조회", description = "오늘 날짜에 저장된 모든 음식 섭취 기록을 반환합니다.")
    fun getTodayDailyFoods(): ResponseEntity<ResponseDto<List<DailyFoodResponseDto>>> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val dailyFoods = dailyFoodService.getTodayDailyFoods(userId)
        return ResponseEntity.ok(ResponseUtil.success("오늘 섭취한 음식 기록 조회 성공", dailyFoods))
    }
}