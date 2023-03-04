package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "GetChannelDetailData - Links 데이터")
data class GetChannelDetailLinkData(
    @field:Schema(description = "채널 링크 id", example = "1")
    val id: Long,
    @field:Schema(description = "링크 url", example = "https://www.naver.com")
    val link: String,
)
