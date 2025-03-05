package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.mapper.AllergyMapper
import com.beyoureyes.beyoureyes.mapper.DiseaseMapper
import com.beyoureyes.beyoureyes.mapper.UserInfoMapper
import com.beyoureyes.beyoureyes.mapper.UserMapper
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class DatabaseService(
    private val userMapper : UserMapper,
    private val  userInfoMapper: UserInfoMapper,
    private val allergyMapper: AllergyMapper,
    private val diseaseMapper: DiseaseMapper,
) {
    @PostConstruct
    fun init() {
        userMapper.createTableNotExists()
        userInfoMapper.createTableIfNotExists()
        allergyMapper.createTableIfNotExists()
        diseaseMapper.createTableIfNotExists()
    }
}