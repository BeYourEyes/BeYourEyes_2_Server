package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.mapper.DiseaseMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DiseaseService (
    private val diseaseMapper: DiseaseMapper
) {
    @Transactional
    fun updateDiseaseInfo(userId : Long, diseaseMap : Map<String, Boolean>) : Boolean {
        return diseaseMapper.updateDisease(userId, diseaseMap) > 0
    }

    fun getAllDisease() = diseaseMapper.getAllDisease()

}