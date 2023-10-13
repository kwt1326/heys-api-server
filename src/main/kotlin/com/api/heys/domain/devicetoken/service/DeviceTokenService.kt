package com.api.heys.domain.devicetoken.service

import com.api.heys.domain.aws.endpoint.service.AwsSnsEndPointService
import com.api.heys.domain.devicetoken.repository.DeviceTokenRepository
import com.api.heys.domain.user.service.UserService
import com.api.heys.entity.DeviceToken
import com.api.heys.helpers.DateHelpers
import com.api.heys.utils.JwtUtil
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class DeviceTokenService (
    private val jwtUtil: JwtUtil,
    private val userService: UserService,
    private val awsSnsEndPointService: AwsSnsEndPointService,
    private val deviceTokenRepository: DeviceTokenRepository,
){

    @Value("\${aws.push.arn}")
    lateinit var awsPushArn : String

    @Transactional
    fun saveDeviceToken(bearer: String, token : String) {

        val phone = jwtUtil.extractUsername(bearer)
        val user = userService.findByPhone(phone) ?: throw NullPointerException()

        runBlocking {
            val endpointArn = awsSnsEndPointService.registerEndpoint(awsPushArn, token)
                ?: throw NullPointerException()

            val jwtExpiredDate = jwtUtil.extractExpiration(bearer)
            val expiredTime = DateHelpers.convertDateToLocalDateTime(jwtExpiredDate)
            val findDeviceToken = deviceTokenRepository.findByToken(token)

            if (findDeviceToken.isPresent) {
                findDeviceToken.get().arn = endpointArn
                findDeviceToken.get().expiredTime = expiredTime
                return@runBlocking
            }

            val deviceToken = DeviceToken(
                user = user,
                token = token,
                arn = endpointArn,
                expiredTime = expiredTime
            )
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
            awsSnsEndPointService.deleteEndpoint(deviceToken.get().arn)
            deviceTokenRepository.deleteById(deviceToken.get().id)
        }
    }

    fun getAvailableDeviceTokens(userId : Long) : List<DeviceToken> {
        return deviceTokenRepository.findAllByUserIdAndExpiredTimeAfter(userId, LocalDateTime.now())
    }
}