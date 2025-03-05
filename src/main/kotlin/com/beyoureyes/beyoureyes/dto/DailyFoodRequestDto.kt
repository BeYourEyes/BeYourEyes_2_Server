package com.beyoureyes.beyoureyes.dto

import com.google.firebase.database.annotations.NotNull
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "음식 기록 요청 DTO")
data class DailyFoodRequestDto(
    @Schema(description = "칼로리 (kcal)", example = "250.0")
    @NotNull val calories: Double,

    @Schema(description = "탄수화물 (mg)", example = "30.0")
    @NotNull val carbohydrates: Double,

    @Schema(description = "단백질 (mg)", example = "10.0")
    @NotNull val protein: Double,

    @Schema(description = "지방 (mg)", example = "5.0")
    @NotNull val fat: Double,

    @Schema(description = "콜레스테롤 (mg)", example = "20.0")
    @NotNull val cholesterol: Double,

    @Schema(description = "나트륨 (mg)", example = "15.0")
    @NotNull val sodium: Double,

    @Schema(description = "당 (mg)", example = "12.0")
    @NotNull val sugar: Double,

    @Schema(description = "포화지방 (mg)", example = "2.0")
    @NotNull val saturatedFat: Double
)