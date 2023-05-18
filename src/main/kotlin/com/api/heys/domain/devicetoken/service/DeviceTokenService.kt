package com.api.heys.domain.devicetoken.service

import com.api.heys.constants.enums.AwsArn
import com.api.heys.domain.aws.service.AwsSnsService
import com.api.heys.domain.devicetoken.repository.DeviceTokenRepository
import com.api.heys.domain.user.service.UserService
import com.api.heys.entity.DeviceToken
import com.api.heys.utils.JwtUtil
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.NullPointerException

@Service
@Transactional(readOnly = true)
class DeviceTokenService (
    private val jwtUtil: JwtUtil,
    private val userService: UserService,
    private val awsSnsService: AwsSnsService,
    private val deviceTokenRepository: DeviceTokenRepository,
){

    @Transactional
    fun saveDeviceToken(bearer: String, token : String) {

        val phone = jwtUtil.extractUsername(bearer)
        val user = userService.findByPhone(phone) ?: throw NullPointerException()

        runBlocking {
            val endpointArn = awsSnsService.registerEndpoint(AwsArn.AWS_SNS_PUSH, token)
                ?: throw NullPointerException()

            val deviceToken = DeviceToken(user = user, token = token, arn = endpointArn)
            deviceTokenRepository.save(deviceToken)
        }
    }

    @Transactional
    fun deleteDeviceToken(token : String) {

        val deviceToken = deviceTokenRepository.findByToken(token)

        if (deviceToken.isEmpty) {
            return
        }

        runBlocking {
            awsSnsService.deleteEndpoint(deviceToken.get().arn)

            deviceTokenRepository.deleteById(deviceToken.get().id)
        }
    }
}