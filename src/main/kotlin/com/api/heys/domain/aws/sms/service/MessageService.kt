package com.api.heys.domain.aws.sms.service

import com.api.heys.domain.aws.sms.vo.MessageRequestVo

interface MessageService {

    suspend fun sendMessage(messageRequestVo: MessageRequestVo)
}