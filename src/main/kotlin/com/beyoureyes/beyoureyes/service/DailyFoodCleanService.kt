package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.mapper.DailyFoodMapper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class DailyFoodCleanService(private val dailyFoodMapper: DailyFoodMapper) {

    // 매일 자정에 실행 (한국 시간 기준)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun deleteAllDailyFoodRecords() {
        dailyFoodMapper.deleteAllDailyFood()
        println("모든 DailyFood 기록이 삭제되었습니다.")
    }
}