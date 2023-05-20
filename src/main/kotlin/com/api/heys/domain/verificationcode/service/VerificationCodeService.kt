package com.api.heys.domain.verificationcode.service

import com.api.heys.constants.enums.SmsMessageType
import com.api.heys.domain.aws.sms.service.MessageService
import com.api.heys.domain.aws.sms.vo.MessageRequestVo
import com.api.heys.domain.verificationcode.repository.VerificationCodeRepository
import com.api.heys.domain.verificationcode.vo.CheckVerificationCodeReqVo
import com.api.heys.domain.verificationcode.vo.SendVerificationCodeReqVo
import com.api.heys.entity.VerificationCode
import com.api.heys.utils.VerificationCodeUtils
import com.api.heys.utils.SmsMessageUtils
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class VerificationCodeService (
    private val messageService: MessageService,
    private val verificationCodeRepository : VerificationCodeRepository
){

    @Transactional
    fun saveVerificationCode(sendVerificationCodeReqVo: SendVerificationCodeReqVo) : String {

        val verificationCode = VerificationCodeUtils.getVerificationCode()
        val message = SmsMessageUtils.getMessage(SmsMessageType.PHONE_NUMBER_VERIFICATION, verificationCode)

        runBlocking {
            val messageRequestVo = MessageRequestVo(message = message, targetPhoneNumber = sendVerificationCodeReqVo.phone)
            messageService.sendMessage(messageRequestVo)

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