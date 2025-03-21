package com.beyoureyes.beyoureyes.dto

data class UserDetailResponseDto(
    val userInfo: UserInfoDto,
    val allergy: Map<String, Boolean>,
    val disease: Map<String, Boolean>
)