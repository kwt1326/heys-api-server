package com.api.heys.domain.content.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "컨텐츠 수정 데이터")
data class EditContentData(
        @NotNull
        @NotBlank
        @field:Schema(description = "컨텐츠 제목 ('스터디'의 경우 채널명으로 공유되어 사용)", example = "환승연애학개론 스터디 채널 2")
        val name: String,

        @NotNull
        @field:Schema(description = "소개글", example = "단순 소개글 입니다~!")
        var contentText: String,
)
