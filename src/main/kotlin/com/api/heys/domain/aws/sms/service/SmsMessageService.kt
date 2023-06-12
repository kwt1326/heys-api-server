package com.api.heys.domain.aws.sms.service

import com.api.heys.domain.aws.sms.vo.SmsMessageRequestVo

interface SmsMessageService {

    suspend fun sendMessage(smsMessageRequestVo: SmsMessageRequestVo)
}