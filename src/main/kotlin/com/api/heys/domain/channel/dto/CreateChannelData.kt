package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "채널 생성을 위한 데이터")
data class CreateChannelData(
        @NotNull
        @NotBlank
        @field:Schema(description = "채널명", example = "환승연애학개론 스터디 채널 1")
        val name: String,

        @NotNull
        @field:Schema(description = "컨텐츠 Identity", example = "1")
        val contentId: Long,
)
