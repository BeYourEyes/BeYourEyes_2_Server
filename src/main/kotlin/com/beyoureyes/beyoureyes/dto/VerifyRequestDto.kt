package com.beyoureyes.beyoureyes.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class VerifyRequestDto(
    @Schema(description = "JWT 토큰", example = "your-jwt-token")
    @field:NotBlank(message = "jwt 토큰값은 필수 입력값입니다.") // 공백, 널 값 방지
    val token: String
)