package com.api.heys.utils

import org.springframework.util.StringUtils

class StringUtils : StringUtils() {

    companion object {
        fun hasNotText(str : String?) : Boolean {
            return !hasText(str)
        }
    }
}