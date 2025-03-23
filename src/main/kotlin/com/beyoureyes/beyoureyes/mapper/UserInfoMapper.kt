package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.entity.UserInfo
import org.apache.ibatis.annotations.*

@Mapper
interface UserInfoMapper {

    @Update("""
            CREATE TABLE IF NOT EXISTS user_info (
            user_info_id SERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL UNIQUE,  
            user_birth DATE NOT NULL,
            user_gender SMALLINT NOT NULL,
            user_nickname VARCHAR(255) UNIQUE NOT NULL,
            CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES "user"(user_id) ON DELETE CASCADE
        )
    """)
    fun createTableIfNotExists()

    @Insert("""
        INSERT INTO user_info (user_id, user_birth, user_gender, user_nickname)
        VALUES (#{userId}, #{userBirth}, #{userGender}, #{userNickname})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "userInfoId")
    fun insertUserInfo(userInfo: UserInfo): Int

    @Select("""
        SELECT * FROM user_info WHERE user_id = #{userId}
    """)
    fun getUserInfoByUserId(userId: Long): UserInfo?

    @Update("""
        UPDATE user_info
        SET
            user_birth = COALESCE(#{userBirth}, user_birth),
            user_gender = COALESCE(#{userGender}, user_gender),
            user_nickname = COALESCE(#{userNickname}, user_nickname)
        WHERE user_id = #{userId}
    """)
    fun updateUserInfo(
        userId: Long,
        userBirth: String?,
        userGender: Int?,
        userNickname: String?
    ): Int

    @Select("SELECT COUNT(*) FROM user_info WHERE user_nickname = #{nickname}")
    fun countByNickname(nickname: String): Int

    @Select("SELECT * FROM user_info")
    fun getAllUserInfo(): List<Map<String, Any>>
}
