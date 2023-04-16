package com.api.heys.user

import com.api.heys.constants.DefaultString
import com.api.heys.constants.SecurityString
import com.api.heys.constants.enums.Gender
import com.api.heys.domain.user.dto.CommonSignUpData
import com.api.heys.domain.user.service.UserDetailService
import com.api.heys.domain.user.service.UserService
import com.api.heys.utils.JwtUtil
import net.bytebuddy.asm.Advice.Local
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@SpringBootTest
@Rollback(true)
@Transactional(readOnly = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDetailServiceTest(
    @Autowired private val userDetailService : UserDetailService,
    @Autowired private val userService: UserService,
    @Autowired private val jwtUtil: JwtUtil
){

    private val phone = "01012341234";
    private var token : String = "";

    @BeforeAll
    fun signUp() {
        val signUpData = CommonSignUpData(
            phone = "01012341234",
            username = "Joenna",
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
    @DisplayName("나의 정보 조회하기")
    @Throws(Exception::class)
    fun getMyUserInfo () {

        // when
        val myInfo = userDetailService.getMyInfo(token)

        // then
        if (myInfo != null) {
            assertThat(myInfo.phone).isEqualTo("01012341234")
            assertThat(myInfo.userName).isEqualTo("Joenna2")
            assertThat(myInfo.percentage).isEqualTo(100)
        }
    }

    @Test
    @DisplayName("다른 유저 정보 조회하기")
    @Throws(Exception::class)
    fun findOtherUserDetail () {

        // given
        val userId : Long = 1

        // when
        val findOtherUserDetail = userDetailService.findOtherUserDetail(userId)

        // then
        if (findOtherUserDetail != null) {
            assertThat(findOtherUserDetail.userName).isEqualTo("Joenna2")
            assertThat(findOtherUserDetail.percentage).isEqualTo(100)
        }

    }

}

