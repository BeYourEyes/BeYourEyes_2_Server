package com.beyoureyes.beyoureyes.dto

import com.google.firebase.database.annotations.NotNull
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "음식 기록 요청 DTO")
data class DailyFoodRequestDto(
    @Schema(description = "칼로리 (kcal)", example = "250")
    @NotNull val calories: Int,

    @Schema(description = "탄수화물 (mg)", example = "30")
    @NotNull val carbohydrates: Int,

    @Schema(description = "단백질 (mg)", example = "10")
    @NotNull val protein: Int,

    @Schema(description = "지방 (mg)", example = "5")
    @NotNull val fat: Int,

    @Schema(description = "콜레스테롤 (mg)", example = "20")
    @NotNull val cholesterol: Int,

    @Schema(description = "나트륨 (mg)", example = "15")
    @NotNull val sodium: Int,

    @Schema(description = "당 (mg)", example = "12")
    @NotNull val sugar: Int,

    @Schema(description = "포화지방 (mg)", example = "2")
    @NotNull val saturatedFat: Int
)
