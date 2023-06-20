package com.api.heys.utils

import com.api.heys.constants.enums.MessageType.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class MessageUtilsTest {


    @Test
    @DisplayName("휴대폰 번호 인증번호 메시지 생성하기 - 성공")
    @Throws(Exception::class)
    fun createSmSVerificationMessageSuccess() {
        // given
        val messageType = PHONE_NUMBER_VERIFICATION
        val code = "123123"
        val messageParam = MessageUtils.getMessageParam(code = code)
        // when
        val message = MessageUtils.getContent(messageType, messageParam)
        // then
        assertThat(message).isEqualTo("[heys] 인증번호[123123]를 입력해주세요.")
    }
}