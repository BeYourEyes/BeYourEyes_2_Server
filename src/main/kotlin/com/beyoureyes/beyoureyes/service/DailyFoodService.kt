package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.dto.DailyFoodRequestDto
import com.beyoureyes.beyoureyes.dto.DailyFoodResponseDto
import com.beyoureyes.beyoureyes.dto.NutrientSummaryDto
import com.beyoureyes.beyoureyes.entity.DailyFood
import com.beyoureyes.beyoureyes.mapper.DailyFoodMapper
import com.google.common.collect.Multimap
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Bucket
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class DailyFoodService(
    private val bucket: Bucket,
    private val dailyFoodMapper: DailyFoodMapper
) {

    @Transactional
    fun saveDailyFood(userId: Long, image: MultipartFile, request: DailyFoodRequestDto): String {
        // 이미지 업로드 및 URL 생성
        val imageUrl = uploadImageToFirebase(image)

        // DailyFood 엔티티 생성
        val dailyFood = DailyFood(
            userId = userId,
            dateTime = LocalDateTime.now(),
            foodPhoto = imageUrl,
            calories = request.calories,
            carbohydrates = request.carbohydrates,
            protein = request.protein,
            fat = request.fat,
            cholesterol = request.cholesterol,
            sodium = request.sodium,
            sugar = request.sugar,
            saturatedFat = request.saturatedFat
        )

        // 데이터베이스에 저장
        dailyFoodMapper.insertDailyFood(dailyFood)
        return imageUrl
    }

    fun uploadImageToFirebase(image: MultipartFile): String {
        val fileName = "daily_food/${UUID.randomUUID()}_${image.originalFilename}"
        val blob = bucket.create(fileName, image.bytes, image.contentType)
        return "https://storage.googleapis.com/${bucket.name}/${blob.name}"
    }

    fun getTodayDailyFoods(userId: Long): List<DailyFoodResponseDto> {
        val today = LocalDate.now().toString()
        return dailyFoodMapper.getDailyFoodsByDate(userId, today)
    }

    fun getTodayNutrientSummary(userId: Long): NutrientSummaryDto {
        val today = LocalDate.now().toString()
        return dailyFoodMapper.getNutrientSummaryByDate(userId, today)
    }
    fun getAllDailyFood() = dailyFoodMapper.getAllDailyFood()

}