package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.dto.DailyFoodRequestDto
import com.beyoureyes.beyoureyes.mapper.DailyFoodMapper
import com.google.api.services.storage.model.Bucket
import com.google.common.collect.Multimap
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class DailyFoodService (
    private val bucket: Bucket,
    private val dailyFoodMapper: DailyFoodMapper
) {

    @Transactional
    fun saveDailyFood(userId: Long, image: MultipartFile, request: DailyFoodRequestDto): String {


    }
}