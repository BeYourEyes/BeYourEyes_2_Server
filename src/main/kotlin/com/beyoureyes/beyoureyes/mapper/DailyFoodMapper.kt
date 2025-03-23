package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.dto.DailyFoodResponseDto
import com.beyoureyes.beyoureyes.dto.FlatDailyFoodDto
import com.beyoureyes.beyoureyes.dto.NutrientSummaryDto
import com.beyoureyes.beyoureyes.entity.DailyFood
import org.apache.ibatis.annotations.*

@Mapper
interface DailyFoodMapper {

    @Update("""
        CREATE TABLE IF NOT EXISTS DailyFood (
            log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
            user_id BIGINT NOT NULL,
            date_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
            food_photo VARCHAR(255),
            calories DECIMAL(10, 2) DEFAULT 0.0,
            carbohydrates DECIMAL(10, 2) DEFAULT 0.0,
            protein DECIMAL(10, 2) DEFAULT 0.0,
            fat DECIMAL(10, 2) DEFAULT 0.0,
            cholesterol DECIMAL(10, 2) DEFAULT 0.0,
            sodium DECIMAL(10, 2) DEFAULT 0.0,
            sugar DECIMAL(10, 2) DEFAULT 0.0,
            saturated_fat DECIMAL(10, 2) DEFAULT 0.0,
            CONSTRAINT fk_foodlog_user_id FOREIGN KEY (user_id) REFERENCES userInfo(user_id) ON DELETE CASCADE
        )
    """)
    fun createTableIfNotExists()

    @Insert("""
        INSERT INTO DailyFood (
            user_id, date_time, food_photo, calories, carbohydrates, protein, fat, 
            cholesterol, sodium, sugar, saturated_fat
        ) VALUES (
            #{userId}, #{dateTime}, #{foodPhoto}, #{calories}, #{carbohydrates}, #{protein}, 
            #{fat}, #{cholesterol}, #{sodium}, #{sugar}, #{saturatedFat}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    fun insertDailyFood(dailyFood : DailyFood): Int
    @Select("SELECT food_photo FROM DailyFood")
    fun getAllImageUrls(): List<String>
    @Update("DELETE FROM DailyFood")
    fun deleteAllDailyFood(): Int

    @Select("""
    SELECT 
        log_id, food_photo, calories, carbohydrates, protein, fat,
        cholesterol, sodium, sugar, saturated_fat, date_time
        FROM DailyFood 
        WHERE user_id = #{userId} 
        AND DATE(date_time) = #{date}
    """)
    fun getDailyFoodsByDate(
        @Param("userId") userId: Long,
        @Param("date") date: String
    ): List<FlatDailyFoodDto>

    @Select("""
        SELECT 
            SUM(calories) as calories,
            SUM(carbohydrates) as carbohydrates,
            SUM(protein) as protein,
            SUM(fat) as fat,
            SUM(cholesterol) as cholesterol,
            SUM(sodium) as sodium,
            SUM(sugar) as sugar,
            SUM(saturated_fat) as saturatedFat
        FROM DailyFood 
        WHERE user_id = #{userId} 
        AND DATE(date_time) = #{date}
    """)
    fun getNutrientSummaryByDate(
        @Param("userId") userId: Long,
        @Param("date") date: String
    ): NutrientSummaryDto

    @Select("SELECT * FROM DailyFood")
    fun getAllDailyFood(): List<Map<String, Any>>
}