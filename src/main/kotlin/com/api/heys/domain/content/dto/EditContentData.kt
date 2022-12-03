package com.api.heys.domain.content.dto

import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "컨텐츠 수정 데이터")
data class EditContentData(
        @NotNull
        @NotBlank
        @field:Schema(description = "컨텐츠 제목 ('스터디'의 경우 채널명으로 공유되어 사용)", example = "환승연애학개론 스터디 채널 2")
        val name: String,

        @field:Schema(description = "주최/주관 (스터디 해당 안됨)", example = "(주)넥슨")
        val company: String?,

        @NotNull
        @field:Schema(description = "소개글", example = "단순 소개글 입니다~!")
        var contentText: String,

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
        @field:Schema(description = "모집 방식", example = "Immediately", implementation = RecruitMethod::class)
        var recruitMethod: RecruitMethod,

        @field:Schema(description = "썸네일 이미지 URI", example = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg")
        var thumbnailUri: String?,
)
