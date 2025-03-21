package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.entity.Allergy
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface AllergyMapper {
    @Update("""
        CREATE TABLE IF NOT EXISTS Allergy (
            allergy_id INT AUTO_INCREMENT PRIMARY KEY,
            user_id BIGINT NOT NULL,
            buckwheat BOOLEAN DEFAULT FALSE,
            wheat BOOLEAN DEFAULT FALSE,
            soybean BOOLEAN DEFAULT FALSE,
            peanut BOOLEAN DEFAULT FALSE,
            walnut BOOLEAN DEFAULT FALSE,
            pine_nut BOOLEAN DEFAULT FALSE,
            sulfur_dioxide BOOLEAN DEFAULT FALSE,
            peach BOOLEAN DEFAULT FALSE,
            tomato BOOLEAN DEFAULT FALSE,
            egg BOOLEAN DEFAULT FALSE,
            milk BOOLEAN DEFAULT FALSE,
            shrimp BOOLEAN DEFAULT FALSE,
            mackerel BOOLEAN DEFAULT FALSE,
            squid BOOLEAN DEFAULT FALSE,
            crab BOOLEAN DEFAULT FALSE,
            shellfish BOOLEAN DEFAULT FALSE,
            pork BOOLEAN DEFAULT FALSE,
            beef BOOLEAN DEFAULT FALSE,
            chicken BOOLEAN DEFAULT FALSE,
            CONSTRAINT fk_allergy_user_id FOREIGN KEY (user_id) REFERENCES userInfo(user_id) ON DELETE CASCADE
        )
    """)
    fun createTableIfNotExists()

    @Insert("""
        INSERT INTO Allergy (user_id, buckwheat, wheat, soybean, peanut, walnut, pine_nut, sulfur_dioxide, 
                             peach, tomato, egg, milk, shrimp, mackerel, squid, crab, shellfish, pork, beef, chicken)
        VALUES (#{userId}, #{buckwheat}, #{wheat}, #{soybean}, #{peanut}, #{walnut}, #{pineNut}, #{sulfurDioxide}, 
                #{peach}, #{tomato}, #{egg}, #{milk}, #{shrimp}, #{mackerel}, #{squid}, #{crab}, #{shellfish}, #{pork}, #{beef}, #{chicken})
    """)
    fun insertAllergy(allergy: Allergy) : Int

    @Select("""
    SELECT 
        buckwheat, wheat, soybean, peanut, walnut, 
        pine_nut, sulfur_dioxide, peach, tomato, 
        egg, milk, shrimp, mackerel, squid, 
        crab, shellfish, pork, beef, chicken 
        FROM Allergy 
        WHERE user_id = #{userId}
    """)
    fun getAllergyByUserId(userId: Long): Allergy? // 수정 필요

    @Update("""
    UPDATE Allergy
    SET
        buckwheat = CASE WHEN #{allergyMap[buckwheat]} IS TRUE AND buckwheat = FALSE THEN TRUE ELSE buckwheat END,
        wheat = CASE WHEN #{allergyMap[wheat]} IS TRUE AND wheat = FALSE THEN TRUE ELSE wheat END,
        soybean = CASE WHEN #{allergyMap[soybean]} IS TRUE AND soybean = FALSE THEN TRUE ELSE soybean END,
        peanut = CASE WHEN #{allergyMap[peanut]} IS TRUE AND peanut = FALSE THEN TRUE ELSE peanut END,
        walnut = CASE WHEN #{allergyMap[walnut]} IS TRUE AND walnut = FALSE THEN TRUE ELSE walnut END,
        pine_nut = CASE WHEN #{allergyMap[pine_nut]} IS TRUE AND pine_nut = FALSE THEN TRUE ELSE pine_nut END,
        sulfur_dioxide = CASE WHEN #{allergyMap[sulfur_dioxide]} IS TRUE AND sulfur_dioxide = FALSE THEN TRUE ELSE sulfur_dioxide END,
        peach = CASE WHEN #{allergyMap[peach]} IS TRUE AND peach = FALSE THEN TRUE ELSE peach END,
        tomato = CASE WHEN #{allergyMap[tomato]} IS TRUE AND tomato = FALSE THEN TRUE ELSE tomato END,
        egg = CASE WHEN #{allergyMap[egg]} IS TRUE AND egg = FALSE THEN TRUE ELSE egg END,
        milk = CASE WHEN #{allergyMap[milk]} IS TRUE AND milk = FALSE THEN TRUE ELSE milk END,
        shrimp = CASE WHEN #{allergyMap[shrimp]} IS TRUE AND shrimp = FALSE THEN TRUE ELSE shrimp END,
        mackerel = CASE WHEN #{allergyMap[mackerel]} IS TRUE AND mackerel = FALSE THEN TRUE ELSE mackerel END,
        squid = CASE WHEN #{allergyMap[squid]} IS TRUE AND squid = FALSE THEN TRUE ELSE squid END,
        crab = CASE WHEN #{allergyMap[crab]} IS TRUE AND crab = FALSE THEN TRUE ELSE crab END,
        shellfish = CASE WHEN #{allergyMap[shellfish]} IS TRUE AND shellfish = FALSE THEN TRUE ELSE shellfish END,
        pork = CASE WHEN #{allergyMap[pork]} IS TRUE AND pork = FALSE THEN TRUE ELSE pork END,
        beef = CASE WHEN #{allergyMap[beef]} IS TRUE AND beef = FALSE THEN TRUE ELSE beef END,
        chicken = CASE WHEN #{allergyMap[chicken]} IS TRUE AND chicken = FALSE THEN TRUE ELSE chicken END
    WHERE user_id = #{userId}
""")
    fun updateAllergy(userId: Long, allergyMap: Map<String, Boolean>): Int



    @Select("SELECT * FROM Allergy")
    fun getAllAllergy(): List<Map<String, Any>>
}