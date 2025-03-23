package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.dto.DailyFoodResponseDto
import com.beyoureyes.beyoureyes.dto.FlatDailyFoodDto
import com.beyoureyes.beyoureyes.dto.NutrientSummaryDto
import com.beyoureyes.beyoureyes.entity.DailyFood
import org.apache.ibatis.annotations.*

@Mapper
interface DailyFoodMapper {

    @Update("""
        CREATE TABLE IF NOT EXISTS daily_food (
            log_id SERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            date_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            food_photo VARCHAR(255),
            calories INTEGER DEFAULT 0,
            carbohydrates INTEGER DEFAULT 0,
            protein INTEGER DEFAULT 0,
            fat INTEGER DEFAULT 0,
            cholesterol INTEGER DEFAULT 0,
            sodium INTEGER DEFAULT 0,
            sugar INTEGER DEFAULT 0,
            saturated_fat INTEGER DEFAULT 0,
            CONSTRAINT fk_foodlog_user_id FOREIGN KEY (user_id) REFERENCES "user_info"(user_id) ON DELETE CASCADE
        )
    """)
    fun createTableIfNotExists()


    @Insert("""
        INSERT INTO daily_food (
            user_id, date_time, food_photo, calories, carbohydrates, protein, fat, 
            cholesterol, sodium, sugar, saturated_fat
        ) VALUES (
            #{userId}, #{dateTime}, #{foodPhoto}, #{calories}, #{carbohydrates}, #{protein}, 
            #{fat}, #{cholesterol}, #{sodium}, #{sugar}, #{saturatedFat}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    fun insertDailyFood(dailyFood: DailyFood): Int

    @Select("SELECT food_photo FROM daily_food")
    fun getAllImageUrls(): List<String>

    @Update("DELETE FROM daily_food")
    fun deleteAllDailyFood(): Int

    @Select("""
        SELECT 
            log_id, food_photo, calories, carbohydrates, protein, fat,
            cholesterol, sodium, sugar, saturated_fat, date_time
        FROM daily_food
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
        FROM daily_food
        WHERE user_id = #{userId}
        AND DATE(date_time) = #{date}
    """)
    fun getNutrientSummaryByDate(
        @Param("userId") userId: Long,
        @Param("date") date: String
    ): NutrientSummaryDto

    @Select("SELECT * FROM daily_food")
    fun getAllDailyFood(): List<Map<String, Any>>
}
