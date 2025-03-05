package com.beyoureyes.beyoureyes.dto

import java.time.LocalDateTime

data class DailyFoodResponseDto(
    val logId: Int,
    val foodPhoto: String,
    val calories: Double,
    val carbohydrates: Double,
    val protein: Double,
    val fat: Double,
    val cholesterol: Double,
    val sodium: Double,
    val sugar: Double,
    val saturatedFat: Double,
    val dateTime: LocalDateTime
)