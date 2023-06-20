package com.api.heys.utils

import java.util.concurrent.ThreadLocalRandom

class VerificationCodeUtils {

    companion object {

        fun getVerificationCode() : String {
            val min = 0
            val max = 999999
            val randomNum = ThreadLocalRandom.current().nextInt(min, max + 1)
            return randomNum.toString().padStart(6, '0')
        }
    }
}