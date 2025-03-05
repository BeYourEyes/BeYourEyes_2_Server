package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.entity.DailyFood
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Options
import org.apache.ibatis.annotations.Update

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
            #{userId}, NOW(), #{foodPhoto}, #{calories}, #{carbohydrates}, #{protein}, 
            #{fat}, #{cholesterol}, #{sodium}, #{sugar}, #{saturatedFat}
        )
    """)
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    fun insertDailyFood(dailyFood : DailyFood): Int
}