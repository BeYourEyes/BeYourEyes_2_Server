package com.beyoureyes.beyoureyes.entity

import java.time.LocalDateTime

data class DailyFood(
    val logId: Long? = null,
    val userId: Long,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val foodPhoto: String,
    val calories: Int = 0,
    val carbohydrates: Int = 0,
    val protein: Int = 0,
    val fat: Int = 0,
    val cholesterol: Int = 0,
    val sodium: Int = 0,
    val sugar: Int = 0,
    val saturatedFat: Int = 0
)
