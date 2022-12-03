package com.api.heys.utils

import java.time.Duration
import java.time.LocalDateTime

open class CommonUtil {
    open fun calculateDday(diff: LocalDateTime): Long {
        val duration = Duration.between(LocalDateTime.now(), diff)
        return duration.toDays()
    }
}