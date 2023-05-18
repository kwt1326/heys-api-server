package com.api.heys.domain.devicetoken.service

import com.api.heys.constants.DefaultString
import com.api.heys.constants.SecurityString
import com.api.heys.constants.enums.Gender
import com.api.heys.domain.devicetoken.repository.DeviceTokenRepository
import com.api.heys.domain.user.dto.CommonSignUpData
import com.api.heys.domain.user.service.UserService
import com.api.heys.utils.JwtUtil
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Rollback(true)
@ActiveProfiles("test")
@Transactional(readOnly = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeviceTokenServiceTest(
    @Autowired private val jwtUtil: JwtUtil,
    @Autowired private val userService: UserService,
    @Autowired private val deviceTokenService: DeviceTokenService,
    @Autowired private val deviceTokenRepository: DeviceTokenRepository
){
    private val phone = "01012341234"
    private val deviceToken = "cCnbC19_Qaq5SuHL60H97j:APA91bFnif8WFbN9EgRZ76iashhxnQvMR9uXNS-5omfty7KsKw1q21gWURt6QupvgCyaPCB7VJRkDzR7ymcSThCzKQn2rKE9-9SXLMDDE8ElTiLqa-yyO1o9LXNAyXb-6XgEwDY0ikTb"
    private var token : String = ""

    @BeforeAll
    fun signUp() {
        val signUpData = CommonSignUpData(
            phone = phone,
            username = "Joenna2",
            password = "12341234",
            birthDate =  LocalDate.of(2002, 9, 10),
            gender = Gender.Male,
            interests = mutableSetOf("자기계발", "스터디")
        )
        println("회원 생성")
        userService.signUp(signUpData, DefaultString.commonRole)
    }

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        token = SecurityString.PREFIX_TOKEN + jwtUtil.createJwt(phone, listOf("USER"))
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    @DisplayName("디바이스 토큰 등록하기")
    fun registerDeviceToken () {
        // given
        println("$token + : $deviceToken")
        println(deviceToken.length)
        deviceTokenService.saveDeviceToken(token, deviceToken)
        // when
        val registerDeviceToken = deviceTokenRepository.findByToken(deviceToken).orElse(null) ?: throw NullPointerException()
        // then
        assertThat(registerDeviceToken.user.phone).isEqualTo(phone)
        assertThat(registerDeviceToken.token).isEqualTo(deviceToken)
    }

    @Test
    @Transactional
    @Throws(Exception::class)
    @DisplayName("디바이스 토큰 삭제하기")
    fun deleteDeviceToken () {
        // given
        deviceTokenService.deleteDeviceToken(deviceToken)
        // when
        val deleteDeviceToken = deviceTokenRepository.findByToken(deviceToken)
        // then
        assertThat(deleteDeviceToken.isEmpty).isEqualTo(true)
    }

}