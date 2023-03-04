package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채널 리스트 요소 데이터")
data class ChannelListItemData(
        @field:Schema(example = "1", type = "long")
        var id: Long,
        @field:Schema(example = "게임 프로그래머 경진대회", type = "string")
        var name: String,
        @field:Schema(example = "123", type = "long")
        var viewCount: Long,
        @field:Schema(example = "3", type = "long", description = "남은 정원")
        var joinRemainCount: Long,
        @field:Schema(example = "30", type = "long", description = "시작일로 부터 지난 일 수")
        var pastDay: Long,
        @field:Schema(example = "1", type = "long")
        var dDay: Long,
        @field:Schema(example = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg", type = "string")
        var thumbnailUri: String,
)
