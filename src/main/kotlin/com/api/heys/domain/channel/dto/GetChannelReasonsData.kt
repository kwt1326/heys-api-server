package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "GetChannelReasons 요청 데이터")
data class GetChannelReasonsData(
    @field:Schema(description = "추출할 시작 날짜", example = "2023-01-01T10:01:00")
    val startDate: String?,

    @field:Schema(description = "추출할 종료 날짜", example = "2023-05-01T10:01:00")
    val endDate: String?
)
