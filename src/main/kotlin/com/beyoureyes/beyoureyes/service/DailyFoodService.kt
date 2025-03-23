package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.dto.DailyFoodRequestDto
import com.beyoureyes.beyoureyes.dto.DailyFoodResponseDto
import com.beyoureyes.beyoureyes.dto.NutrientSummaryDto
import com.beyoureyes.beyoureyes.entity.DailyFood
import com.beyoureyes.beyoureyes.mapper.DailyFoodMapper
import com.google.cloud.storage.Acl
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
    fun saveDailyFood(userId: Long, image: MultipartFile, request: DailyFoodRequestDto): Pair<String, LocalDateTime> {
        val imageUrl = uploadImageToFirebase(image)
        val now = LocalDateTime.now()

        val dailyFood = DailyFood(
            userId = userId,
            dateTime = now,
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

        dailyFoodMapper.insertDailyFood(dailyFood)

        return Pair(imageUrl, now)
    }

    fun uploadImageToFirebase(image: MultipartFile): String {
        val fileName = "daily_food/${UUID.randomUUID()}_${image.originalFilename}"
        val blob = bucket.create(fileName, image.bytes, image.contentType)

        // 모든 사용자에게 읽기 권한 부여
        blob.toBuilder()
            .setAcl(listOf(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
            .build()
            .update()

        return "https://storage.googleapis.com/${bucket.name}/${blob.name}"
    }

    fun getTodayDailyFoods(userId: Long): List<Map<String, Any>> {
        val today = LocalDate.now().toString()
        val rawList = dailyFoodMapper.getDailyFoodsByDate(userId, today)

        return rawList.map { dto ->
            mapOf(
                "log_id" to dto.logId,
                "food_photo" to dto.foodPhoto,
                "date_time" to dto.dateTime,
                "nutrition_info" to mapOf(
                    "calories" to dto.calories,
                    "carbohydrates" to dto.carbohydrates,
                    "protein" to dto.protein,
                    "fat" to dto.fat,
                    "cholesterol" to dto.cholesterol,
                    "sodium" to dto.sodium,
                    "sugar" to dto.sugar,
                    "saturatedFat" to dto.saturatedFat
                )
            )
        }
    }


    fun getTodayNutrientSummary(userId: Long): NutrientSummaryDto {
        val today = LocalDate.now().toString()
        return dailyFoodMapper.getNutrientSummaryByDate(userId, today)
    }
    fun getAllDailyFood() = dailyFoodMapper.getAllDailyFood()

}