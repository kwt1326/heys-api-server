package com.api.heys.domain.content.dto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "공모전/대외활동 컨텐츠 리스트 요소 데이터")
data class ExtraContentListItemData(
        @field:Schema(example = "1", type = "long")
        var id: Long,
        @field:Schema(example = "게임 프로그래머 경진대회", type = "string")
        var title: String,
        @field:Schema(example = "(주)넥슨", type = "string")
        var company: String,
        @field:Schema(example = "123", type = "long")
        var viewCount: Long,
        @field:Schema(example = "10", type = "int")
        var channelCount: Int,
        @field:Schema(example = "1", type = "long")
        var dDay: Long,
        @field:Schema(example = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg", type = "string")
        var previewImgUri: String,
        @field:Schema(example = "2023-07-09T14:00:00", type = "date")
        var publishedAt: LocalDateTime?,
)
