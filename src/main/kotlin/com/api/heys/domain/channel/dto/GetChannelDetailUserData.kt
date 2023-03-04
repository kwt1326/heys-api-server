package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "GetChannelDetailData - User 데이터")
data class GetChannelDetailUserData(
    @field:Schema(description = "유저 id", example = "1")
    val id: Long,
    @field:Schema(description = "프로필 완성도", example = "77")
    val percentage: Int = 0,
)
