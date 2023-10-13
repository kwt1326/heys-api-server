package com.api.heys.domain.verificationcode.repository

import com.api.heys.entity.VerificationCode
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface VerificationCodeRepository : JpaRepository<VerificationCode, Long> {

    fun findAllVerificationCodeByPhoneAndExpiredTimeAfter(phone : String, expiredTime : LocalDateTime) : List<VerificationCode>
}