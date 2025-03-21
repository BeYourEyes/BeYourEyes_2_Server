package com.beyoureyes.beyoureyes.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.*

data class SaveUserRequestDto(
    @field:NotBlank(message = "device_id는 필수입니다.")
    val device_id: String,

    @field:NotBlank(message = "생년월일은 필수입니다.")
    val user_birth: String,

    @field:NotNull(message = "성별은 필수입니다.")
    val user_gender: Int?,

    @field:NotBlank(message = "닉네임은 필수입니다.")
    val user_nickname: String,

    @field:Valid
    val allergy: AllergyDto,

    @field:Valid
    val disease: DiseaseDto
)
