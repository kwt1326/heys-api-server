package com.api.heys.domain.user.repository

import com.api.heys.entity.WithdrawUserReason
import org.springframework.data.jpa.repository.JpaRepository

interface WithdrawUserReasonRepository: JpaRepository<WithdrawUserReason, Long> {
}