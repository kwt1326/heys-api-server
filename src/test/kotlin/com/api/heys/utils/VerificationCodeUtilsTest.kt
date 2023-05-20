package com.api.heys.utils

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class VerificationCodeUtilsTest {

    @Test
    @DisplayName("인증 번호 생성하기")
    @Throws(Exception::class)
    fun getVerificationCode () {
        val verificationCode = VerificationCodeUtils.getVerificationCode()

        println("verificationCode : $verificationCode")
        assertThat(verificationCode.length).isEqualTo(6)
    }
}