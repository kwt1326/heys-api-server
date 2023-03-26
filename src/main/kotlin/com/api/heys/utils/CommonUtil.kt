package com.api.heys.utils

import java.time.Duration
import java.time.LocalDateTime

open class CommonUtil {
    open fun diffDay(from: LocalDateTime?, to: LocalDateTime?): Long {
        if (from == null || to == null) return Long.MIN_VALUE
        val duration = Duration.between(from, to)
        return duration.toDays()
    }
    open fun calculateDday(diff: LocalDateTime?): Long {
        if (diff == null) return Long.MIN_VALUE
        return this.diffDay(LocalDateTime.now(), diff)
    }

    open fun calcTotalPage(count: Long, limit: Long): Long {
        val plusOne = count % limit > 0
        return count / limit + (if (plusOne) 1 else 0)
    }
}