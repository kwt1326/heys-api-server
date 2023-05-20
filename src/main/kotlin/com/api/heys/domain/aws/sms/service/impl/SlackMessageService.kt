package com.api.heys.domain.aws.sms.service.impl

import com.api.heys.constants.enums.Slack
import com.api.heys.constants.enums.Slack.MESSAGE
import com.api.heys.domain.aws.sms.service.MessageService
import com.api.heys.domain.aws.sms.vo.SlackMessageReqVo
import com.api.heys.domain.aws.sms.vo.MessageRequestVo
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.http.HttpMethod.*
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.RuntimeException

@Service
class SlackMessageService (
    private val restTemplate: RestTemplate
): MessageService {

    @Value("\${slack.channel}")
    lateinit var channelId : String

    @Value("\${slack.token}")
    lateinit var slackToken : String

    private fun getHeaders() : HttpHeaders {
        val httpHeaders = HttpHeaders()
        httpHeaders.contentType = MediaType.APPLICATION_JSON
        httpHeaders.setBearerAuth(slackToken)
        return httpHeaders
    }


    override suspend fun sendMessage(messageRequestVo: MessageRequestVo) {
        val headers = getHeaders()
        val requestBody = SlackMessageReqVo(channel = channelId, text = messageRequestVo.message)
        val httpEntity = HttpEntity(requestBody, headers)

        val response = restTemplate.exchange(MESSAGE.getUrl(), POST, httpEntity, Any::class.java)

        if (response.statusCode != OK) {
            throw RuntimeException("슬랙 메시지 전송에 실패하였습니다.")
        }
    }
}