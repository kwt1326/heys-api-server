package com.api.heys.domain.verificationcode.service

import com.api.heys.domain.verificationcode.repository.VerificationCodeRepository
import com.api.heys.domain.verificationcode.vo.CheckVerificationCodeReqVo
import com.api.heys.domain.verificationcode.vo.SendVerificationCodeReqVo
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest
@Rollback(false)
@Transactional(readOnly = true)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VerificationCodeServiceTest (
    @Autowired private val verificationCodeService: VerificationCodeService,
    @Autowired private val verificationCodeRepository: VerificationCodeRepository
){

    private val phone = "01023397702"
    private var verificationCode : String = ""

    @Test
    @Order(1)
    @Transactional
    @Throws(Exception::class)
    @DisplayName("휴대폰 인증 번호 전송하기")
    fun saveVerificationCode() {
        // given
        val sendVerificationCodeReqVo = SendVerificationCodeReqVo(phone = phone)
        // when
        val saveVerificationCode = verificationCodeService.saveVerificationCode(sendVerificationCodeReqVo)
        // then
        val verificationCodes
            = verificationCodeRepository.findAllVerificationCodeByPhoneAndExpiredTimeAfter(phone, LocalDateTime.now())

        verificationCode = saveVerificationCode
        val findCode = verificationCodes.find { it.code == verificationCode }

        println("인증 번호 : $verificationCode")
        assertThat(findCode).isNotNull
    }

    @Test
    @Order(2)
    @Transactional
    @Throws(Exception::class)
    @DisplayName("휴대폰 인증 번호 확인 - 성공")
    fun checkVerificationCodeSuccess() {
        // given
        println("checkVerificationCodeSuccess.인증번호 : $verificationCode")
        val checkVerificationCodeReqVo = CheckVerificationCodeReqVo(code = verificationCode, phone = phone)
        // when
        val isVerification = verificationCodeService.checkVerificationCode(checkVerificationCodeReqVo)
        // then
        assertThat(isVerification).isEqualTo(true)
    }

    @Test
    @Order(3)
    @Transactional
    @Throws(Exception::class)
    @DisplayName("휴대폰 인증 번호 확인 - 실패")
    fun checkVerificationCodeFail() {
        // given
        println("checkVerificationCodeFail.인증번호 : $verificationCode")
        val checkVerificationCodeReqVo = CheckVerificationCodeReqVo(code = verificationCode, phone = phone)
        // when
        val isVerification = verificationCodeService.checkVerificationCode(checkVerificationCodeReqVo)
        // then
        assertThat(isVerification).isEqualTo(false)
    }

}