package com.api.heys.domain.channel.dto

import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "채널 생성을 위한 데이터")
data class CreateChannelData(
    @NotNull
    @NotBlank
    @field:Schema(description = "채널명", example = "환승연애학개론 스터디 채널 1")
    val name: String,

    @field:Schema(description = "참여 목적", example = "[\"역량강화\", \"계기확립\"]")
    val purposes: Set<String> = setOf(),

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
    @field:Schema(implementation = RecruitMethod::class, description = "모집 방식")
    var recruitMethod: RecruitMethod,

    @NotNull
    @field:Schema(description = "소개글", example = "단순 소개글 입니다~!")
    var contentText: String,

    @NotNull
    @field:Schema(description = "이런분을 찾아요! 글", example = "이런분만 왔으면 좋겠어요 ㅎㅎ")
    var recruitText: String,

    @field:Schema(description = "연결 링크 URI 배열", example = "[\"https://www.naver.com\", \"https://www.google.co.kr\"]")
    var linkUri: MutableSet<String> = mutableSetOf(),

    @field:Schema(description = "관심분야", example = "[\"자기계발\", \"연애\"]")
    val interests: MutableSet<String> = mutableSetOf(),
)
