package com.api.heys.domain.aws

import com.api.heys.domain.aws.sms.service.impl.AwsSnsMessageService
import com.api.heys.domain.aws.sms.vo.MessageRequestVo
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/aws")
class AwsController (
    private val awsSnsSmsService: AwsSnsMessageService
){

    @PostMapping("/sms")
    suspend fun sendSms(@RequestBody messageRequestVo: MessageRequestVo) : String {

        awsSnsSmsService.sendMessage(messageRequestVo)
        val result = "${messageRequestVo.targetPhoneNumber} 문자 전송 완료"
        return result
    }
}