package com.beyoureyes.beyoureyes.entity

import java.time.LocalDateTime

data class DailyFood (
    val logId: Long? = null,
    val userId: Long,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val foodPhoto: String,
    val calories: Double = 0.0,
    val carbohydrates: Double = 0.0,
    val protein: Double = 0.0,
    val fat: Double = 0.0,
    val cholesterol: Double = 0.0,
    val sodium: Double = 0.0,
    val sugar: Double = 0.0,
    val saturatedFat: Double = 0.0
)