package com.api.heys.domain.aws.sms.service.impl

import com.api.heys.constants.enums.SmsMessageType
import com.api.heys.domain.aws.sms.service.MessageService
import com.api.heys.domain.aws.sms.vo.MessageRequestVo
import com.api.heys.utils.SmsMessageUtils
import com.api.heys.utils.VerificationCodeUtils
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SlackMessageServiceTest (
    @Autowired private val messageService: MessageService
){
    @Test
    @DisplayName("슬랙으로 인증번호 보내기")
    @Throws(Exception::class)
    fun sendSlackMessage () {
        // given
        val verificationCode = VerificationCodeUtils.getVerificationCode()
        val message = SmsMessageUtils.getMessage(SmsMessageType.PHONE_NUMBER_VERIFICATION, verificationCode)
        val messageReqVo = MessageRequestVo(message = message)
        // when
        runBlocking {
            messageService.sendMessage(messageReqVo)
        }
    }
}