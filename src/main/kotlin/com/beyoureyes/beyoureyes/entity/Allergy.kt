package com.beyoureyes.beyoureyes.entity

data class Allergy @JvmOverloads constructor(
    val allergyId : Long? = null,
    val userId : Long,
    val buckwheat : Boolean = false, // 메밀
    val wheat : Boolean  = false, // 밀
    val soybean : Boolean = false, // 대두
    val peanut : Boolean = false, // 땅콩
    val walnut : Boolean = false, // 호두
    val pineNut : Boolean = false, // 잣
    val sulfurDioxide : Boolean = false, // 이산화황
    val peach : Boolean = false, // 복숭아
    val tomato : Boolean = false, // 토마토
    val egg : Boolean = false, // 난류
    val milk : Boolean = false, // 우유
    val shrimp : Boolean = false, // 새우
    val mackerel : Boolean = false, // 고등어
    val squid : Boolean = false, // 오징어
    val crab : Boolean = false, // 게
    val shellfish : Boolean = false, // 조개류
    val pork : Boolean = false, // 돼지고기
    val beef : Boolean = false, // 쇠고기
    val chicken : Boolean = false // 닭고기
)