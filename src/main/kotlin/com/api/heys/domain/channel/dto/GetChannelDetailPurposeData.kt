package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "GetChannelDetailData - Purposes 데이터")
data class GetChannelDetailPurposeData(
    @field:Schema(description = "채널 참여 목적 id", example = "1")
    val id: Long,
    @field:Schema(description = "참여 목적", example = "실력 향상")
    val purpose: String,
)
