package com.beyoureyes.beyoureyes.entity

import java.time.LocalDate
import java.time.LocalDateTime

data class UserInfo (
    val userInfoId : Long? = null,
    val userId : Long,
    val userBirth : LocalDate,
    val userGender : Int,
    val userNickname : String
)