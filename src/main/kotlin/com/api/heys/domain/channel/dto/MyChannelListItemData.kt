package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "내 채널 리스트 요소 데이터")
data class MyChannelListItemData(
    @field:Schema(example = "1", type = "long")
    var id: Long,
    @field:Schema(example = "Study", type = "string", description = "Study / Contest / Extracurricular")
    var type: String,
    @field:Schema(example = "게임 프로그래머 경진대회", type = "string")
    var name: String,
    @field:Schema(example = "대장 아냐_포저", type = "string")
    var leaderName: String,
    @field:Schema(example = "1", type = "long")
    var dDay: Long,
    @field:Schema(example = "https://res.$", type = "string")
    var thumbnailUri: String,
    @field:Schema(example = "true", type = "boolean")
    var isLeader: Boolean,
    @field:Schema(example = "true", type = "boolean")
    var isClosed: Boolean,
)
