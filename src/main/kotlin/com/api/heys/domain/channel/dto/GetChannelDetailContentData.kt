package com.api.heys.domain.channel.dto

import com.api.heys.constants.enums.ContentType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "GetChannelDetailData - Content 데이터")
data class GetChannelDetailContentData(
    @field:Schema(description = "컨텐츠 id", example = "1")
    val id: Long,
    @field:Schema(description = "컨텐츠 제목", example = "제 3회 게임 프로그래밍 경진대회")
    val title: String,
    @field:Schema(description = "프리뷰 img url", example = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg")
    val previewImgUrl: String,
    @field:Schema(description = "주최/주관", example = "넥슨")
    val company: String,
    @field:Schema(description = "컨텐츠 타입", example = "Contest")
    val type: ContentType,
)
