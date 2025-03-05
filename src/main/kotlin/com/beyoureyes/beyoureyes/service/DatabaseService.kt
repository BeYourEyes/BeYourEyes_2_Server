package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.mapper.*
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class DatabaseService(
    private val userMapper : UserMapper,
    private val userInfoMapper: UserInfoMapper,
    private val allergyMapper: AllergyMapper,
    private val diseaseMapper: DiseaseMapper,
    private val dailyFoodMapper : DailyFoodMapper
) {
    @PostConstruct
    fun init() {
        userMapper.createTableNotExists()
        userInfoMapper.createTableIfNotExists()
        allergyMapper.createTableIfNotExists()
        diseaseMapper.createTableIfNotExists()
        dailyFoodMapper.createTableIfNotExists()
    }
}