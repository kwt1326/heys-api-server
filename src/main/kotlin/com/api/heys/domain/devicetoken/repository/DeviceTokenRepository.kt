package com.api.heys.domain.devicetoken.repository

import com.api.heys.entity.DeviceToken
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*

interface DeviceTokenRepository : JpaRepository<DeviceToken, Long> {

    fun findByToken(token: String) : Optional<DeviceToken>

    fun findAllByUserIdAndExpiredTimeAfter(userId : Long, time : LocalDateTime) : List<DeviceToken>
}