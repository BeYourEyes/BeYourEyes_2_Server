package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.entity.Disease
import com.beyoureyes.beyoureyes.entity.DiseaseInfo
import org.apache.ibatis.annotations.*

@Mapper
interface DiseaseMapper {

    @Update("""
        CREATE TABLE IF NOT EXISTS disease (
            disease_id SERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            diabetes BOOLEAN DEFAULT FALSE,
            hypertension BOOLEAN DEFAULT FALSE,
            hyperlipidemia BOOLEAN DEFAULT FALSE,
            CONSTRAINT fk_disease_user_id FOREIGN KEY (user_id) REFERENCES "user_info"(user_id) ON DELETE CASCADE
        )
    """)
    fun createTableIfNotExists()

    @Insert("""
        INSERT INTO disease (user_id, diabetes, hypertension, hyperlipidemia)
        VALUES (#{userId}, #{diabetes}, #{hypertension}, #{hyperlipidemia})
    """)
    fun insertDisease(disease: Disease): Int

    @Select("""
        SELECT diabetes, hypertension, hyperlipidemia 
        FROM disease 
        WHERE user_id = #{userId}
    """)
    fun getDiseaseByUserId(userId: Long): DiseaseInfo?

    @Update("""
        UPDATE disease
        SET
            diabetes = CASE 
                WHEN #{diseaseMap[diabetes]} IS TRUE AND diabetes = FALSE THEN TRUE 
                WHEN #{diseaseMap[diabetes]} IS FALSE AND diabetes = TRUE THEN FALSE
                ELSE diabetes END,
            hypertension = CASE 
                WHEN #{diseaseMap[hypertension]} IS TRUE AND hypertension = FALSE THEN TRUE
                WHEN #{diseaseMap[hypertension]} IS FALSE AND hypertension = TRUE THEN FALSE
                ELSE hypertension END,
            hyperlipidemia = CASE 
                WHEN #{diseaseMap[hyperlipidemia]} IS TRUE AND hyperlipidemia = FALSE THEN TRUE
                WHEN #{diseaseMap[hyperlipidemia]} IS FALSE AND hyperlipidemia = TRUE THEN FALSE
                ELSE hyperlipidemia END
        WHERE user_id = #{userId}
    """)
    fun updateDisease(userId: Long, diseaseMap: Map<String, Boolean>): Int

    @Select("SELECT * FROM disease")
    fun getAllDisease(): List<Map<String, Any>>
}
