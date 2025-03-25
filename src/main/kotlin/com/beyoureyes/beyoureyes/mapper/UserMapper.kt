package com.beyoureyes.beyoureyes.mapper

import com.beyoureyes.beyoureyes.entity.User
import org.apache.ibatis.annotations.*

@Mapper
interface UserMapper {

    @Update("""
        CREATE TABLE IF NOT EXISTS "user" (
            user_id SERIAL PRIMARY KEY,
            device_id VARCHAR(255) UNIQUE NOT NULL,
            last_login TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            deleted_at TIMESTAMP NULL DEFAULT NULL,
            refresh_token VARCHAR(500) DEFAULT NULL
        )
    """)
    fun createTableNotExists()

    @Select("SELECT user_id, device_id, deleted_at FROM \"user\"")
    fun findAll(): List<User>

    @Results(
        id = "UserResultMap", value = [
            Result(property = "userId", column = "user_id"),
            Result(property = "deviceId", column = "device_id"),
            Result(property = "lastLogin", column = "last_login"),
            Result(property = "deletedAt", column = "deleted_at")
        ]
    )
    @Select("""
    SELECT user_id, device_id, last_login, deleted_at
    FROM "user"
    WHERE device_id = #{deviceId}
""")
    fun findByDeviceId(deviceId: String): User?



    @Insert("INSERT INTO \"user\" (device_id) VALUES (#{deviceId})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    fun insertUser(user: User): Int

    @Update("UPDATE \"user\" SET last_login = CURRENT_TIMESTAMP WHERE user_id = #{userId}")
    fun updateLastLogin(userId: Long)

    @Update("""
        UPDATE "user"
        SET deleted_at = CURRENT_TIMESTAMP
        WHERE last_login < (NOW() - INTERVAL '1 YEAR') AND deleted_at IS NULL
    """)
    fun deactivateInactiveUsers(): Int

    @Select("SELECT * FROM \"user\" WHERE device_id = #{deviceId} AND deleted_at IS NULL")
    fun findActiveUserByDeviceId(deviceId: String): User?

    @Update("UPDATE \"user\" SET deleted_at = NULL WHERE user_id = #{userId}")
    fun reactivateUser(userId: Long): Int

    @Update("UPDATE \"user\" SET refresh_token = #{refreshToken} WHERE user_id = #{userId}")
    fun updateRefreshToken(userId: Long, refreshToken: String)

    @Select("SELECT refresh_token FROM \"user\" WHERE user_id = #{userId}")
    fun getRefreshToken(userId: Long): String?

    @Delete("DELETE FROM \"user\" WHERE user_id = #{userId}")
    fun deleteUser(userId: Long): Int

    @Select("SELECT * FROM \"user\"")
    fun finalAll(): List<Map<String, Any>>
}
