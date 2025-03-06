package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.entity.Allergy
import com.beyoureyes.beyoureyes.entity.Disease
import com.beyoureyes.beyoureyes.entity.UserInfo
import com.beyoureyes.beyoureyes.mapper.AllergyMapper
import com.beyoureyes.beyoureyes.mapper.DiseaseMapper
import com.beyoureyes.beyoureyes.mapper.UserInfoMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class UserInfoService(
    private val userInfoMapper : UserInfoMapper,
    private val allergyMapper : AllergyMapper,
    private val diseaseMapper : DiseaseMapper
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
            userBirth = LocalDateTime.parse(userBirth),
            userGender = userGender,
            userNickname = userNickname
        )

        val userInfoSaved = userInfoMapper.insertUserInfo(userInfo) > 0

        val allergySaved = allergyMapper.insertAllergy(allergy.copy(userId = userId)) > 0

        val diseaseSaved = diseaseMapper.insertDisease(disease.copy(userId = userId)) > 0

        return userInfoSaved && allergySaved && diseaseSaved
    }

    fun getUserDetails(userId : Long): Triple<UserInfo?, Allergy?, Disease?> {
        val userInfo = userInfoMapper.getUserInfoByUserId(userId)
        val allergy = allergyMapper.getAllergyByUserId(userId)
        val disease = diseaseMapper.getDiseaseByUserId(userId)
        return Triple(userInfo, allergy, disease)
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

    fun isNicknameAvaliable(nickname : String): Boolean {
        return userInfoMapper.countByNickname(nickname) == 0
    }

    fun getAllUserInfo() = userInfoMapper.getAllUserInfo()
}