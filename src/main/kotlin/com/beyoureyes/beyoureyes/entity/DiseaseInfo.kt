package com.beyoureyes.beyoureyes.entity

data class DiseaseInfo @JvmOverloads constructor(
    val diabetes : Boolean = false, // 당뇨병
    val hypertension : Boolean = false, // 고혈합
    val hyperlipidemia : Boolean = false // 고지혈증 mg 다
)