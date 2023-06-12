package com.api.heys.domain.aws.sms.service.impl

import aws.sdk.kotlin.services.sns.SnsClient
import aws.sdk.kotlin.services.sns.model.PublishRequest
import com.api.heys.domain.aws.sms.service.SmsMessageService
import com.api.heys.domain.aws.sms.vo.SmsMessageRequestVo
import com.api.heys.utils.PhoneNumberUtils
import com.api.heys.utils.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AwsSmsMessageService : SmsMessageService {

    @Value("\${aws.sms.region}")
    private lateinit var awsSmsRegion : String

    private fun getClient() : SnsClient {
        return SnsClient { region = awsSmsRegion }
    }

    // 문자 전송하기
    override suspend fun sendMessage(smsMessageRequestVo: SmsMessageRequestVo) {

        if (StringUtils.hasNotText(smsMessageRequestVo.targetPhoneNumber)) {
            throw IllegalArgumentException("휴대폰 번호가 잘못되었습니다.")
        }

        val sendPhoneNumber = PhoneNumberUtils.getPhoneNumberByNation(smsMessageRequestVo.targetPhoneNumber)

        val request = PublishRequest {
            message = smsMessageRequestVo.message
            phoneNumber = sendPhoneNumber
        }
        getClient().use { snsClient -> snsClient.publish(request) }
    }
}