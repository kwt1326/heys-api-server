package com.api.heys.utils

import com.api.heys.constants.enums.SmsMessageType

class SmsMessageUtils {

    companion object {

        fun getMessage(messageType: SmsMessageType, vararg values : String) : String {

            val needValueCount = messageType.getNeedValueCount()

            if (needValueCount != values.size) {
                throw IllegalArgumentException("메세지에 넣을 인수 개수가 다릅니다.")
            }

            var message = messageType.getPattern()
            for (i in values.indices) {
                message = message.replace("{$i}", values[i])
            }

            return message
        }
    }
}