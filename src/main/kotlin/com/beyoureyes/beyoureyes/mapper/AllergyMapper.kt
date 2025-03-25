package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.entity.Allergy
import org.apache.ibatis.annotations.*

@Mapper
interface AllergyMapper {

    @Update("""
        CREATE TABLE IF NOT EXISTS allergy (
            allergy_id SERIAL PRIMARY KEY,
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
            CONSTRAINT fk_allergy_user_id FOREIGN KEY (user_id) REFERENCES user_info(user_id) ON DELETE CASCADE
        )
    """)
    fun createTableIfNotExists()

    @Insert("""
        INSERT INTO allergy (user_id, buckwheat, wheat, soybean, peanut, walnut, pine_nut, sulfur_dioxide, 
                             peach, tomato, egg, milk, shrimp, mackerel, squid, crab, shellfish, pork, beef, chicken)
        VALUES (#{userId}, #{buckwheat}, #{wheat}, #{soybean}, #{peanut}, #{walnut}, #{pineNut}, #{sulfurDioxide}, 
                #{peach}, #{tomato}, #{egg}, #{milk}, #{shrimp}, #{mackerel}, #{squid}, #{crab}, #{shellfish}, #{pork}, #{beef}, #{chicken})
    """)
    fun insertAllergy(allergy: Allergy): Int

    @Select("""
        SELECT 
            buckwheat, wheat, soybean, peanut, walnut, 
            pine_nut, sulfur_dioxide, peach, tomato, 
            egg, milk, shrimp, mackerel, squid, 
            crab, shellfish, pork, beef, chicken 
        FROM allergy 
        WHERE user_id = #{userId}
    """)
    fun getAllergyByUserId(userId: Long): Allergy?

    @Update("""
        UPDATE allergy
        SET
            buckwheat = COALESCE(#{allergyMap[buckwheat]}, buckwheat),
            wheat = COALESCE(#{allergyMap[wheat]}, wheat),
            soybean = COALESCE(#{allergyMap[soybean]}, soybean),
            peanut = COALESCE(#{allergyMap[peanut]}, peanut),
            walnut = COALESCE(#{allergyMap[walnut]}, walnut),
            pine_nut = COALESCE(#{allergyMap[pine_nut]}, pine_nut),
            sulfur_dioxide = COALESCE(#{allergyMap[sulfur_dioxide]}, sulfur_dioxide),
            peach = COALESCE(#{allergyMap[peach]}, peach),
            tomato = COALESCE(#{allergyMap[tomato]}, tomato),
            egg = COALESCE(#{allergyMap[egg]}, egg),
            milk = COALESCE(#{allergyMap[milk]}, milk),
            shrimp = COALESCE(#{allergyMap[shrimp]}, shrimp),
            mackerel = COALESCE(#{allergyMap[mackerel]}, mackerel),
            squid = COALESCE(#{allergyMap[squid]}, squid),
            crab = COALESCE(#{allergyMap[crab]}, crab),
            shellfish = COALESCE(#{allergyMap[shellfish]}, shellfish),
            pork = COALESCE(#{allergyMap[pork]}, pork),
            beef = COALESCE(#{allergyMap[beef]}, beef),
            chicken = COALESCE(#{allergyMap[chicken]}, chicken)
        WHERE user_id = #{userId}
    """)
    fun updateAllergy(userId: Long, allergyMap: Map<String, Boolean>): Int

    @Select("SELECT * FROM allergy")
    fun getAllAllergy(): List<Map<String, Any>>
}
