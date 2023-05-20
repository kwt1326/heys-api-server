package com.api.heys.domain.aws.sms.service.impl

import aws.sdk.kotlin.services.sns.SnsClient
import aws.sdk.kotlin.services.sns.model.PublishRequest
import com.api.heys.domain.aws.sms.service.MessageService
import com.api.heys.domain.aws.sms.vo.MessageRequestVo
import com.api.heys.utils.PhoneNumberUtils
import com.api.heys.utils.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AwsSnsMessageService : MessageService {

    @Value("\${aws.sms.region}")
    lateinit var awsSmsRegion : String

    private fun getClient() : SnsClient {
        return SnsClient { region = awsSmsRegion }
    }

    // 문자 전송하기
    override suspend fun sendMessage(messageRequestVo: MessageRequestVo) {

        if (StringUtils.hasNotText(messageRequestVo.targetPhoneNumber)) {
            throw IllegalArgumentException("휴대폰 번호가 잘못되었습니다.")
        }

        val sendPhoneNumber = PhoneNumberUtils.getPhoneNumberByNation(messageRequestVo.targetPhoneNumber)

        val request = PublishRequest {
            message = messageRequestVo.message
            phoneNumber = sendPhoneNumber
        }
        getClient().use { snsClient -> snsClient.publish(request) }
    }
}