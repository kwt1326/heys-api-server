package com.api.heys.domain.aws.sms.service.impl

import com.api.heys.constants.enums.MessageType.*
import com.api.heys.domain.aws.sms.service.SmsMessageService
import com.api.heys.domain.aws.sms.vo.SmsMessageRequestVo
import com.api.heys.utils.MessageUtils
import com.api.heys.utils.VerificationCodeUtils
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@Disabled
@SpringBootTest
class SlackSmsMessageServiceTest (
    @Autowired private val smsMessageService: SmsMessageService
){
    @Test
    @DisplayName("슬랙으로 인증번호 보내기")
    @Throws(Exception::class)
    fun sendSlackMessage () {
        // given
        val verificationCode = VerificationCodeUtils.getVerificationCode()

        val messageParam = MessageUtils.getMessageParam(code = verificationCode)
        val message = MessageUtils.getContent(PHONE_NUMBER_VERIFICATION, messageParam)
        val messageReqVo = SmsMessageRequestVo(message = message)
        // when
        runBlocking {
            smsMessageService.sendMessage(messageReqVo)
        }
    }
}