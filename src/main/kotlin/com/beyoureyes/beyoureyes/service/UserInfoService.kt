package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.entity.Allergy
import com.beyoureyes.beyoureyes.entity.Disease
import com.beyoureyes.beyoureyes.entity.UserInfo
import com.beyoureyes.beyoureyes.mapper.AllergyMapper
import com.beyoureyes.beyoureyes.mapper.DiseaseMapper
import com.beyoureyes.beyoureyes.mapper.UserInfoMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class UserInfoService(
    private val userInfoMapper : UserInfoMapper,
    private val allergyMapper : AllergyMapper,
    private val diseaseMapper : DiseaseMapper,
) {
    @Transactional
    fun saveUserInfo(
        userId : Long,
        userBirth : String,
        userGender : Int?,
        userNickname : String?,
        allergy : Allergy,
        disease : Disease
    ):Boolean {
        if (userGender == null || userNickname.isNullOrBlank()) {
            throw IllegalArgumentException("성별과 닉네임은 필수")
        }

        val userInfo = UserInfo(
            userId = userId,
            userBirth = LocalDate.parse(userBirth),
            userGender = userGender,
            userNickname = userNickname
        )

        val userInfoSaved = userInfoMapper.insertUserInfo(userInfo) > 0

        val allergySaved = allergyMapper.insertAllergy(allergy.copy(userId = userId)) > 0

        val diseaseSaved = diseaseMapper.insertDisease(disease.copy(userId = userId)) > 0

        return userInfoSaved && allergySaved && diseaseSaved
    }

    private val objectMapper = jacksonObjectMapper()
    fun getUserDetails(userId: Long): Triple<UserInfo?, Map<String, Boolean>, Map<String, Boolean>> {
        val userInfo = userInfoMapper.getUserInfoByUserId(userId)
        val allergy = allergyMapper.getAllergyByUserId(userId)
        val disease = diseaseMapper.getDiseaseByUserId(userId)

        // userId, allergyId 제외하여 정보 제공
        val allergyData = allergy?.let {
            objectMapper.convertValue<Map<String, Any>>(it)
                .filterKeys { key -> key != "userId" && key != "allergyId" }
                .filterValues { value -> value == true }
                .mapValues { it.value as Boolean }
        } ?: emptyMap()

        val diseaseData = disease?.let {
            objectMapper.convertValue<Map<String, Any>>(it)
                .filterKeys { key -> key != "userId" && key != "diseaseId" }
                .filterValues { value -> value == true }
                .mapValues { it.value as Boolean }
        } ?: emptyMap()

        return Triple(userInfo, allergyData, diseaseData)
    }

    @Transactional
    fun updateUserInfo(
        userId: Long,
        userBirth: String?,
        userGender: Int?,
        userNickname: String?,
        allergyMap : Map<String, Boolean>?,
        diseaseMap : Map<String, Boolean>?
    ) : Boolean {
        val updated = userInfoMapper.updateUserInfo(userId, userBirth, userGender, userNickname) > 0

        allergyMap?.let {
            allergyMapper.updateAllergy(userId, it)
        }
        diseaseMap?.let {
            diseaseMapper.updateDisease(userId, it)
        }

        return updated
    }
}