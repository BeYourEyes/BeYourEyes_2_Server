package com.beyoureyes.beyoureyes.service

import com.beyoureyes.beyoureyes.mapper.DailyFoodMapper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import com.google.cloud.storage.Bucket

@Service
class DailyFoodCleanService(private val dailyFoodMapper: DailyFoodMapper, private val bucket: Bucket) {

    // 매일 자정에 실행 (한국 시간 기준)
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    fun deleteAllDailyFoodRecords() {
        // 1. 삭제할 모든 이미지 URL 가져오기
        val imageUrls = dailyFoodMapper.getAllImageUrls()

        // 2. Firebase Storage에서 이미지 삭제
        imageUrls.forEach { url ->
            val fileName = url.substringAfterLast("/")
            val blob = bucket.get(fileName)
            blob?.delete()
        }

        // 3. 데이터베이스 기록 삭제
        val deletedCount = dailyFoodMapper.deleteAllDailyFood()

        println("모든 DailyFood 기록이 삭제되었습니다. 삭제된 레코드 수: $deletedCount")
    }
}