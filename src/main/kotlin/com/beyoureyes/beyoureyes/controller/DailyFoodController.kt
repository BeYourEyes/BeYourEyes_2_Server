package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.DailyFoodRequestDto
import com.beyoureyes.beyoureyes.dto.DailyFoodResponseDto
import com.beyoureyes.beyoureyes.dto.NutrientSummaryDto
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
        @RequestParam("image") image: MultipartFile,
        @RequestParam("food_data") foodData: String
    ): ResponseEntity<ResponseDto<Map<String, Any>>> {

        val userId = SecurityContextHolder.getContext().authentication.principal.toString().toLong()
        val foodDto = objectMapper.readValue(foodData, DailyFoodRequestDto::class.java)

        // 데이터 저장 및 이미지 URL 반환
        val (imageUrl, savedDateTime) = dailyFoodService.saveDailyFood(userId, image, foodDto)

        val response = mapOf(
            "img_url" to imageUrl,
            "datetime" to savedDateTime.toString()
        )

        return ResponseEntity.ok(ResponseUtil.success("음식 섭취 기록이 저장되었습니다.", response))

    }

    @GetMapping("/today")
    @Operation(summary = "오늘 섭취한 모든 음식 기록 조회", description = "오늘 날짜에 저장된 모든 음식 섭취 기록을 반환합니다.")
    fun getTodayDailyFoods(): ResponseEntity<ResponseDto<List<DailyFoodResponseDto>>> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val dailyFoods = dailyFoodService.getTodayDailyFoods(userId)
        return ResponseEntity.ok(ResponseUtil.success("오늘 섭취한 음식 기록 조회 성공", dailyFoods))
    }

    @GetMapping("/today/summary")
    @Operation(summary = "오늘 섭취한 총 영양소 조회", description = "오늘 날짜에 저장된 모든 음식의 영양소 합계를 반환합니다.")
    fun getTodayNutrientSummary(): ResponseEntity<ResponseDto<NutrientSummaryDto>> {
        val userId = SecurityContextHolder.getContext().authentication.principal as Long
        val nutrientSummary = dailyFoodService.getTodayNutrientSummary(userId)
        return ResponseEntity.ok(ResponseUtil.success("오늘 섭취한 총 영양소 조회 성공", nutrientSummary))
    }

    @GetMapping("/daily-food")
    fun getAllDailyFood(): ResponseEntity<ResponseDto<Any>> {
        val data = dailyFoodService.getAllDailyFood()
        return ResponseEntity.ok(ResponseUtil.success("모든 일일 섭취 음식 데이터 조회 성공", data))
    }
}