package com.api.heys.domain.aws.service

import aws.sdk.kotlin.services.sns.SnsClient
import aws.sdk.kotlin.services.sns.model.CreatePlatformEndpointRequest
import aws.sdk.kotlin.services.sns.model.DeleteEndpointRequest
import com.api.heys.constants.enums.AwsArn
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AwsSnsService {

    @Value("\${aws.region}")
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
    suspend fun registerEndpoint(arn : AwsArn, deviceToken : String) : String? {
        val client = getClient()

        val request = CreatePlatformEndpointRequest {
            platformApplicationArn = arn.value
            token = deviceToken
        }
        val response = client.createPlatformEndpoint(request)

        return response.endpointArn
    }
}