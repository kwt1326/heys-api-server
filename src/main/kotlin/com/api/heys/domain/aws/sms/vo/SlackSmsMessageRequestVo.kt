package com.api.heys.domain.aws.sms.vo

data class SlackSmsMessageRequestVo (
    val channel : String,
    val text : String
)
