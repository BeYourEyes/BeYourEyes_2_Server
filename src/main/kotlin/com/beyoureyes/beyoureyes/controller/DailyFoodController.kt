package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.dto.DailyFoodRequestDto
import com.beyoureyes.beyoureyes.dto.ResponseDto
import com.beyoureyes.beyoureyes.service.DailyFoodService
import com.beyoureyes.beyoureyes.utils.ResponseUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/food")
@Tag(name = "Daily Food API", description = "일일 음식 기록 API")
class DailyFoodController(private val dailyFoodService: DailyFoodService) {

    @PostMapping("/record", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "음식 기록 저장", description = "음식 사진과 영양소 정보를 저장합니다.")
    fun saveDailyFood(
        @RequestPart("image") image: MultipartFile,
        @RequestPart("foodData") request: DailyFoodRequestDto
    ): ResponseEntity<ResponseDto<String>> {
        val userId = SecurityContextHolder.getContext().authentication.principal.toString().toLong()
        val imageUrl = dailyFoodService.saveDailyFood(userId, image, request)
        return ResponseEntity.ok(ResponseUtil.success("음식 기록이 저장되었습니다.", imageUrl))
    }
}