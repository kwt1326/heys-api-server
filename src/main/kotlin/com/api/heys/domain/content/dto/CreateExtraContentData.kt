package com.api.heys.domain.content.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "컨텐츠(공모전, 대외활동) 생성을 위한 데이터")
data class CreateExtraContentData(
        @NotNull
        @NotBlank
        @field:Schema(description = "컨텐츠 제목", example = "환승연애학개론 스터디 채널 1")
        val title: String,

        @NotNull
        @field:Schema(description = "참여 대상", example = "누구나 지원 가능")
        val target: String,

        @NotNull
        @field:Schema(description = "보상", example = "100만원")
        val benefit: String,

        @field:Schema(description = "연락처", example = "test@company.com")
        val contact: String,

        @field:Schema(description = "주최/주관 (스터디 포함 안됨)", example = "(주)넥슨")
        val company: String,

        @NotNull
        @field:Schema(description = "소개글", example = "단순 소개글 입니다~!")
        var contentText: String,

        @NotNull
        @field:Schema(description = "모집 시작 날짜 (format : yyyy-MM-ddTHH:mm:ss)", example = "2022-11-07T14:00:00")
        var startDate: LocalDateTime,

        @NotNull
        @field:Schema(description = "모집 종료 날짜 (format : yyyy-MM-ddTHH:mm:ss)", example = "2022-11-07T14:00:00")
        var endDate: LocalDateTime,

        @field:Schema(description = "연결 링크 URI", example = "https://www.naver.com")
        var linkUri: String?,

        @field:Schema(description = "프리뷰 이미지 URI", example = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg")
        var previewImgUri: String?,

        @field:Schema(description = "썸네일 이미지 URI", example = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg")
        var thumbnailUri: String?,

        @field:Schema(description = "관심분야", example = "[\"자기계발\", \"연애\"]")
        val interests: MutableSet<String> = mutableSetOf(),
)
