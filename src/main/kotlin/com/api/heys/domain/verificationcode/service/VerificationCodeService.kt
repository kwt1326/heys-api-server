package com.api.heys.domain.verificationcode.service

import com.api.heys.constants.enums.MessageType.*
import com.api.heys.domain.aws.sms.service.SmsMessageService
import com.api.heys.domain.aws.sms.vo.SmsMessageRequestVo
import com.api.heys.domain.verificationcode.repository.VerificationCodeRepository
import com.api.heys.domain.verificationcode.vo.CheckVerificationCodeReqVo
import com.api.heys.domain.verificationcode.vo.SendVerificationCodeReqVo
import com.api.heys.entity.VerificationCode
import com.api.heys.utils.VerificationCodeUtils
import com.api.heys.utils.MessageUtils
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class VerificationCodeService (
    private val smsMessageService: SmsMessageService,
    private val verificationCodeRepository : VerificationCodeRepository
){

    @Transactional
    fun saveVerificationCode(sendVerificationCodeReqVo: SendVerificationCodeReqVo) : String {

        val verificationCode = VerificationCodeUtils.getVerificationCode()
        val messageParam = MessageUtils.getMessageParam(code = verificationCode)
        val message = MessageUtils.getContent(PHONE_NUMBER_VERIFICATION, messageParam)

        runBlocking {
            val smsMessageRequestVo = SmsMessageRequestVo(message = message, targetPhoneNumber = sendVerificationCodeReqVo.phone)
            smsMessageService.sendMessage(smsMessageRequestVo)

            val code = VerificationCode(phone = sendVerificationCodeReqVo.phone, code = verificationCode)
            verificationCodeRepository.save(code)
        }
        return verificationCode
    }

    @Transactional
    fun checkVerificationCode(checkVerificationCodeReqVo: CheckVerificationCodeReqVo) : Boolean {

        val verificationCodes = verificationCodeRepository.findAllVerificationCodeByPhoneAndExpiredTimeAfter(checkVerificationCodeReqVo.phone, LocalDateTime.now())

        verificationCodes.find { it.code == checkVerificationCodeReqVo.code } ?: return false

        val codeIds = verificationCodes.stream().map { it.id }.collect(Collectors.toList())
        verificationCodeRepository.deleteAllById(codeIds)
        return true
    }
}