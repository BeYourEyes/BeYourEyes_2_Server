package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.dto.UserDetailResponseDto
import com.beyoureyes.beyoureyes.dto.UserInfoDto
import com.beyoureyes.beyoureyes.entity.Allergy
import com.beyoureyes.beyoureyes.entity.Disease
import com.beyoureyes.beyoureyes.entity.UserInfo
import com.beyoureyes.beyoureyes.mapper.AllergyMapper
import com.beyoureyes.beyoureyes.mapper.DiseaseMapper
import com.beyoureyes.beyoureyes.mapper.UserInfoMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter


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
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val parsedBirthDate = LocalDate.parse(userBirth, formatter)

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
    fun getUserDetails(userId: Long): UserDetailResponseDto {
        val userInfo = userInfoMapper.getUserInfoByUserId(userId)
            ?: throw IllegalArgumentException("해당 유저 없음")

        val allergy = allergyMapper.getAllergyByUserId(userId)
        val disease = diseaseMapper.getDiseaseByUserId(userId)

        // allergy에서 true인 값만 필터링 후 AllergyDto로 변환
        val allergyData = allergy?.let {
            mapOf(
                "peanut" to it.peanut,
                "milk" to it.milk,
                "shrimp" to it.shrimp,
                // 여기서 나머지 알레르기 항목들도 필터링해서 추가
            ).filterValues { value -> value == true } // true인 값만 필터링
        } ?: emptyMap()

        // disease에서 true인 값만 필터링 후 DiseaseDto로 변환
        val diseaseData = disease?.let {
            mapOf(
                "diabetes" to it.diabetes,
                "hypertension" to it.hypertension,
                "hyperlipidemia" to it.hyperlipidemia
            ).filterValues { value -> value == true } // true인 값만 필터링
        } ?: emptyMap()

        return UserDetailResponseDto(
            userInfo = UserInfoDto(
                userId = userInfo.userId,
                userBirth = userInfo.userBirth.toString(),
                userGender = userInfo.userGender,
                userNickname = userInfo.userNickname
            ),
            allergy = allergyData,  // true인 알레르기 항목만 포함
            disease = diseaseData   // true인 질환 항목만 포함
        )
    }

    @Transactional
    fun updateUserInfo(
        userId: Long,
        userBirth: String?,
        userGender: Int?,
        userNickname: String?
    ) : Boolean {
        // userBirth가 null이 아니면 LocalDate로 변환
        val formattedBirthDate = userBirth?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }?.toString()
        return userInfoMapper.updateUserInfo(userId, formattedBirthDate, userGender, userNickname) > 0
    }

    fun isNicknameAvaliable(nickname : String): Boolean {
        return userInfoMapper.countByNickname(nickname) == 0
    }

    fun getAllUserInfo() = userInfoMapper.getAllUserInfo()
}