package com.beyoureyes.beyoureyes.dto

import java.time.LocalDateTime

data class FlatDailyFoodDto(
    val logId: Int,
    val foodPhoto: String,
    val calories: Int,
    val carbohydrates: Int,
    val protein: Int,
    val fat: Int,
    val cholesterol: Int,
    val sodium: Int,
    val sugar: Int,
    val saturatedFat: Int,
    val dateTime: LocalDateTime
)
