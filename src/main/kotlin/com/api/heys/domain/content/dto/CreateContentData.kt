package com.api.heys.domain.content.dto

import com.api.heys.constants.enums.ContentType
import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "컨텐츠(공모전, 스터디 공통) 생성을 위한 데이터")
data class CreateContentData(
        @NotNull
        @NotBlank
        @field:Schema(description = "컨텐츠 제목 ('스터디'의 경우 채널명으로 공유되어 사용)", example = "환승연애학개론 스터디 채널 1")
        val name: String,

        @NotNull
        @NotBlank
        @field:Schema(description = "컨텐츠 타입 (스터디, 공모전, 대외활동)", example = "Study")
        val contentType: ContentType,

        /* 컨텐츠 및 채널 정보 */

        @NotNull
        @NotBlank
        @field:Schema(description = "참여 목적", example = "실력향상")
        val purpose: String,

        @NotNull
        @field:Schema(description = "참여 형태", example = "Offline", implementation = Online::class)
        val online: Online,

        @field:Schema(description = "참여 지역 (online == '온라인' 일 경우 필요하지 않음)", example = "서울시")
        val location: String?,

        @NotNull
        @field:Schema(description = "인원 제한", example = "50")
        val limitPeople: Int,

        @NotNull
        @field:Schema(description = "마감 날짜 (format : yyyy-MM-ddTHH:mm:ss)", example = "2022-11-07T14:00:00")
        var lastRecruitDate: LocalDateTime,

        @NotNull
        @NotBlank
        @field:Schema(description = "모집 방식", example = "Immediately")
        var recruitMethod: RecruitMethod,

        @NotNull
        @field:Schema(description = "소개글", example = "단순 소개글 입니다~!")
        var contentText: String,

        @field:Schema(description = "관심분야", example = "[\"자기계발\", \"연애\"]")
        val interests: MutableSet<String> = mutableSetOf(),
)
