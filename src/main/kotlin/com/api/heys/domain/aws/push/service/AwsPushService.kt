package com.api.heys.domain.aws.push.service

import aws.sdk.kotlin.services.sns.SnsClient
import aws.sdk.kotlin.services.sns.model.PublishRequest
import aws.smithy.kotlin.runtime.util.asyncLazy
import com.api.heys.domain.aws.endpoint.service.AwsSnsEndPointService
import com.api.heys.domain.aws.push.vo.PushMessageVo
import com.api.heys.domain.aws.push.vo.TargetPushMessageVo
import com.google.gson.Gson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.CollectionUtils

@Service
class AwsPushService (
    private val awsSnsEndPointService: AwsSnsEndPointService
){
    private val JSON = "json";

    @Value("\${aws.push.arn}")
    lateinit var awsPushArn : String

    @Value("\${aws.push.region}")
    private lateinit var awsPushRegion : String

    private fun <T : PushMessageVo > getPushMessage(messageVo: T) : Map<String, String> {
        return mapOf(
            "GCM" to
                    "{ \"notification\": {\"title\": \"${messageVo.title}\", \"body\": \"${messageVo.content}\" } }",
        )
    }

    // 특정 endpoint Arn 푸시 발송
    suspend fun sendTargetArnPush(targetPushMessageVo: TargetPushMessageVo) {

        if (CollectionUtils.isEmpty(targetPushMessageVo.endPoints)) {
            return
        }

        val client = SnsClient { region = awsPushRegion } // 클라이언트의 region을 본인이 사용할 region으로 변경

        val pushSendMessage = getPushMessage(targetPushMessageVo)

        targetPushMessageVo.endPoints.stream().parallel().map {
            asyncLazy {
                awsSnsEndPointService.checkEndpointAttributes(it)

                val request = PublishRequest {
                    messageStructure = JSON
                    message = Gson().toJson(pushSendMessage)
                    targetArn = it // 모바일 푸시 알림 엔드포인트 ARN
                }

                client.publish(request)
            }
        }
    }

    // 특정 Topic Arn 푸시 발송
    // TODO : 전체 전송
    suspend fun sendTopicArnPush(pushMessageVo: PushMessageVo) {

        val client = SnsClient { region = awsPushRegion } // 클라이언트의 region을 본인이 사용할 region으로 변경

        val pushSendMessage = getPushMessage(pushMessageVo)
        println(pushSendMessage)

        awsSnsEndPointService.checkEndpointAttributes(awsPushArn)

        val request = PublishRequest {
            messageStructure = JSON
            message = Gson().toJson(pushSendMessage)
            targetArn = "endPoints" // 모바일 푸시 알림 엔드포인트 ARN
        }

        val response = client.publish(request)
        println("FCM push sent successfully: ${response.messageId}")
    }
}