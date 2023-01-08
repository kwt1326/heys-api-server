package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채널 상태 변경 요청 데이터")
data class ChannelPutData(
    @field:Schema(example = "환영합니다!", type = "string")
    var message: String = ""
)