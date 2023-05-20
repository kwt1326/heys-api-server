package com.api.heys.domain.aws.sms.vo

import com.api.heys.constants.enums.NationType

data class MessageRequestVo(
    val message : String,
    val targetPhoneNumber : String = "",
    val nationType : NationType = NationType.KR
)