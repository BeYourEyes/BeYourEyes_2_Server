package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.mapper.AllergyMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AllergyService (
    private val allergyMapper: AllergyMapper
) {

    @Transactional
    fun updateAllergyInfo (userId : Long, allergyMap: Map<String, Boolean>) : Boolean {
        return allergyMapper.updateAllergy(userId, allergyMap) > 0
    }
    fun getAllAllergy() = allergyMapper.getAllAllergy()

}