package com.beyoureyes.beyoureyes.entity

import java.time.LocalDateTime

data class User (
    val userId : Long? = null,
    val deviceId: String?,
    val lastLogin : LocalDateTime? = null,
    val deletedAt : LocalDateTime? = null
) {
    constructor() : this(null, "", null)
}

