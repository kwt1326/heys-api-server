package com.api.heys.user

import com.api.heys.domain.user.UserService
import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.entity.User
import com.api.heys.entity.UserDetail
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean

import io.mockk.every
import org.mockito.Mockito
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
class UserControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var userService: UserService

    val mapper = jacksonObjectMapper()

    private val user = User()
    private val signUpData = Mockito.mock(SignUpData::class.java)

    @Test
    fun signUp_alreadyExistUsername_givenBadRequestStatus() {
        every { userService.signUp(signUpData) } returns null

        mockMvc.perform(post("/user/signUp")
                .content(mapper.writeValueAsString(signUpData))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError)
    }

    @Test
    fun signUp_givenSuccessStatus() {
        every { userService.signUp(signUpData) } returns user

        mockMvc.perform(post("/user/signUp")
                .content(mapper.writeValueAsString(signUpData))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk)
    }
}