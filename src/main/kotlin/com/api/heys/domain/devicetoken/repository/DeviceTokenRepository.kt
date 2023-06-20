package com.api.heys.domain.devicetoken.repository

import com.api.heys.entity.DeviceToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeviceTokenRepository : JpaRepository<DeviceToken, Long> {

    fun findByToken(token: String) : Optional<DeviceToken>

    fun findAllByUserId(userId : Long) : List<DeviceToken>
}