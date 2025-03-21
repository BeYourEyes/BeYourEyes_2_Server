package com.beyoureyes.beyoureyes.controller

import com.beyoureyes.beyoureyes.service.UserInfoService
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get


@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userInfoService: UserInfoService


    @Test
    fun `사용 가능한 닉네임이면 200 OK와 true를 반환한다`() {
        val nickname = "newuser"

        whenever(userInfoService.isNicknameAvaliable(nickname)).thenReturn(true)

        mockMvc.perform(get("/v2/user/check-nickname").param("nickname", nickname))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value(true))
            .andExpect(jsonPath("$.message").value("사용 가능한 닉네임입니다."))
    }

    @Test
    fun `이미 사용 중인 닉네임이면 200 OK와 false를 반환한다`() {
        val nickname = "takenuser"

        whenever(userInfoService.isNicknameAvaliable(nickname)).thenReturn(false)

        mockMvc.perform(get("/v2/user/check-nickname").param("nickname", nickname))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.data").value(false))
            .andExpect(jsonPath("$.message").value("이미 사용 중인 닉네임입니다."))
    }
}