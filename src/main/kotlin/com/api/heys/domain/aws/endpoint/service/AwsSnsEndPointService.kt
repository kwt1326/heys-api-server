package com.api.heys.domain.aws.endpoint.service

import aws.sdk.kotlin.services.sns.SnsClient
import aws.sdk.kotlin.services.sns.model.CreatePlatformEndpointRequest
import aws.sdk.kotlin.services.sns.model.DeleteEndpointRequest
import aws.sdk.kotlin.services.sns.model.GetEndpointAttributesRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AwsSnsEndPointService {

    @Value("\${aws.push.region}")
    lateinit var awsPushRegion : String

    fun getClient() : SnsClient {
        return SnsClient { region = awsPushRegion };
    }

    // 엔드포인트 삭제
    suspend fun deleteEndpoint(endPointArn : String?) {
        val client = getClient()
        val request = DeleteEndpointRequest { this.endpointArn = endPointArn }

        client.deleteEndpoint(request)
    }


    // 엔드포인트 등록
    suspend fun registerEndpoint(targetArn : String, deviceToken : String) : String? {
        val client = getClient()

        val request = CreatePlatformEndpointRequest {
            platformApplicationArn = targetArn
            token = deviceToken
        }
        val response = client.createPlatformEndpoint(request)

        return response.endpointArn
    }

    // 엔드 포인트 살아 있는지 확인
    suspend fun checkEndpointAttributes(endPointArn : String) {
        val client = SnsClient { region = awsPushRegion } // 클라이언트의 region을 본인이 사용할 region으로 변경

        val request = GetEndpointAttributesRequest {
            endpointArn = endPointArn
        }

        try {
            val response = client.getEndpointAttributes(request)
            val enabledAttribute = response.attributes?.get("Enabled")
            if (enabledAttribute != null) {
                println("Enabled: ${enabledAttribute}")
            } else {
                println("Enabled attribute not found")
            }
        } catch (e: Exception) {
            println("Error checking endpoint attributes: ${e.message}")
        }
    }
}