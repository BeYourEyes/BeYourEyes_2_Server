package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.entity.Disease
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface DiseaseMapper {
    @Update("""
        CREATE TABLE IF NOT EXISTS Disease (
            disease_id INT AUTO_INCREMENT PRIMARY KEY,
            user_id BIGINT NOT NULL,
            diabetes BOOLEAN DEFAULT FALSE,
            hypertension BOOLEAN DEFAULT FALSE,
            hyperlipidemia BOOLEAN DEFAULT FALSE,
            CONSTRAINT fk_disease_user_id FOREIGN KEY (user_id) REFERENCES userInfo(user_id) ON DELETE CASCADE
        )
    """)
    fun createTableIfNotExists()

    @Insert(
        """
        INSERT INTO Disease (user_id, diabetes, hypertension, hyperlipidemia)
        VALUES (#{userId}, #{diabetes}, #{hypertension}, #{hyperlipidemia})
    """
    )
    fun insertDisease(disease: Disease): Int

    @Select(
        """
        SELECT * FROM Disease WHERE user_id = #{userId}
    """
    )
    fun getDiseaseByUserId(userId: Long): Disease?

    @Update("""
        UPDATE Disease
        SET
             diabetes = COALESCE(#{diseaseMap[diabetes]}, diabetes),
            hypertension = COALESCE(#{diseaseMap[hypertension]}, hypertension),
            hyperlipidemia = COALESCE(#{diseaseMap[hyperlipidemia]}, hyperlipidemia)
        WHERE user_id = #{userId}
    """)
    fun updateDisease(userId: Long, diseaseMap: Map<String, Boolean>): Int

    @Select("SELECT * FROM Disease")
    fun getAllDisease(): List<Map<String, Any>>
}