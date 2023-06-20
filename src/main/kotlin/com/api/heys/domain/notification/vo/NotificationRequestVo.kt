package com.api.heys.domain.notification.vo

import com.api.heys.constants.enums.MessageType
import com.api.heys.entity.Channels
import com.api.heys.entity.Users

data class NotificationRequestVo(
    val messageType : MessageType,
    val sender : Users?,
    val receiver : Users,
    val channel : Channels? = null
)