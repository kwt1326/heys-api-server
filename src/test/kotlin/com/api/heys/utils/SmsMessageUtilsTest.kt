package com.api.heys.utils

import com.api.heys.constants.enums.SmsMessageType.PHONE_NUMBER_VERIFICATION
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class SmsMessageUtilsTest {


    @Test
    @DisplayName("휴대폰 번호 인증번호 메시지 생성하기 - 성공")
    @Throws(Exception::class)
    fun createSmSVerificationMessageSuccess() {
        // given
        val messageType = PHONE_NUMBER_VERIFICATION
        val code = "123123"
        // when
        val message = SmsMessageUtils.getMessage(messageType, code)
        // then
        assertThat(message).isEqualTo("[heys] 인증번호[123123]를 입력해주세요.")
    }

    @Test
    @DisplayName("휴대폰 번호 인증번호 메시지 생성하기 - 실패")
    @Throws(Exception::class)
    fun createSmSVerificationMessageFail() {
        // given
        val messageType = PHONE_NUMBER_VERIFICATION
        val code1 = "123123"
        val code2 = "124124"
        // then
        assertThatThrownBy { SmsMessageUtils.getMessage(messageType, code1, code2) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }
}