package com.beyoureyes.beyoureyes.dto

import java.time.LocalDateTime

data class DailyFoodResponseDto(
    val logId: Int,
    val foodPhoto: String,
    val nutritionInfo : NutrientSummaryDto,
    val dateTime: LocalDateTime
)