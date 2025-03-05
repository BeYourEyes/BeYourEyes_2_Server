package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.dto.DailyFoodRequestDto
import com.beyoureyes.beyoureyes.entity.DailyFood
import com.beyoureyes.beyoureyes.mapper.DailyFoodMapper
import com.google.common.collect.Multimap
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Bucket
import java.time.LocalDateTime

@Service
class DailyFoodService (
    private val bucket: Bucket,
    private val dailyFoodMapper: DailyFoodMapper
) {

    @Transactional
    fun saveDailyFood(userId: Long, image: MultipartFile, request: DailyFoodRequestDto): String {
        val imageUrl = uploadImageToFirebase(image)

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

        dailyFoodMapper.insertDailyFood(dailyFood)
        return imageUrl
    }

    private fun uploadImageToFirebase(image: MultipartFile): String {
        val fileName = "daily_food/${UUID.randomUUID()}_${image.originalFilename}"
        val blob = bucket.create(fileName, image.bytes, image.contentType)
        return "https://storage.googleapis.com/${bucket.name}/${blob.name}"
    }
}