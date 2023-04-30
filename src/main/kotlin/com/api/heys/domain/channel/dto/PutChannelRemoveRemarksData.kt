package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채널 북마크 여러개 제거 데이터")
data class PutChannelRemoveRemarksData(
    @field:Schema(description = "북마크 한 채널 ID 리스트", example = "[\"1\", \"2\"]")
    val channelIds: Set<Long> = setOf(),
)
