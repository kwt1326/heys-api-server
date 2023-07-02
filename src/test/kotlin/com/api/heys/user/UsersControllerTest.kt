package com.api.heys.user

import com.api.heys.constants.DefaultString
import com.api.heys.constants.MessageString
import com.api.heys.constants.enums.Gender
import com.api.heys.domain.user.controller.UserController
import com.api.heys.domain.user.dto.AdminSignUpData
import com.api.heys.domain.user.dto.CommonSignUpData
import com.api.heys.domain.user.dto.SignUpResponse
import com.api.heys.domain.user.service.UserService
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

@WebMvcTest(UserController::class)
internal class UsersControllerTest(@Autowired val mockMvc: MockMvc) {
    @MockkBean
    lateinit var userService: UserService

    private val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    private val signUpData = CommonSignUpData(
        phone = "01012341234",
        username = "TESTER",
        password = "12341234",
        birthDate = LocalDate.of(1995, 10, 9),
        gender = Gender.Male,
        interests = mutableSetOf("교육", "자기계발"),
    )
    private val adminSignUpData = AdminSignUpData(
        phone = "01012341234",
        username = "TESTER",
        password = "12341234",
        birthDate = LocalDate.of(1995, 10, 9),
    )
    private val successResponseEntity = ResponseEntity.ok(
        SignUpResponse(
            token = "",
            userId = null,
            message = MessageString.SUCCESS_EN
        )
    )

    @Test
    fun signUp_alreadyExistUsername_givenBadRequestStatus() {
        every { userService.signUp(signUpData, DefaultString.commonRole) } returns successResponseEntity

        mockMvc.perform(
            post("/user/signUp")
                .content(mapper.writeValueAsString(signUpData))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().is4xxClientError)
    }

    @Test
    @WithMockUser
    fun signUp_givenSuccessStatus() {
        every { userService.signUp(signUpData, DefaultString.commonRole) } returns successResponseEntity
        every { userService.signUp(adminSignUpData, DefaultString.adminRole) } returns successResponseEntity

        mockMvc.perform(
            post("/user/signUp")
                .content(mapper.writeValueAsString(signUpData))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
        )
            .andExpect(status().isOk)

        mockMvc.perform(
            post("/user/signUp/admin")
                .content(mapper.writeValueAsString(adminSignUpData))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
        )
            .andExpect(status().isOk)
    }
}