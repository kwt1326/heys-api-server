package com.api.heys.utils

import com.api.heys.constants.enums.MessageType

class MessageUtils {

    companion object {

        fun getContent(messageType: MessageType, params : Map<String, String?>) : String {

            var content = messageType.content()
            for (entry in params.entries) {
                val value = entry.value ?: continue
                content = content.replace("{${entry.key}}", value)
            }
            return content
        }

        fun getMessageParam(code : String? = null,
                            user : String? = null,
                            channel : String? = null): Map<String, String?> {
            return mapOf("user" to user ,
                "channel" to channel,
                "code" to code)
        }
    }
}