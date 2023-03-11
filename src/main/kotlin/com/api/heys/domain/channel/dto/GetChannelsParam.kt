package com.api.heys.domain.channel.dto

import com.api.heys.constants.enums.Online
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "채널 목록 필터 query params")
data class GetChannelsParam(
    @field:ArraySchema(schema = Schema(example = "연애", type = "string"))
    val interests: List<String>?,

    @field:Schema(example = "2022-12-12T00:08:28", description = "모집 마감 일자")
    val lastRecruitDate: String?,

    @field:Schema(example = "[\"역량강화\", \"계기확립\"]", description = "참여 목적")
    val purposes: Set<String>?,

    @field:Schema(example = "OnOffLine", implementation = Online::class, description = "활동 형태 (OnOffLine, Online, Offline)")
    val online: Online?,

    @field:Schema(description = "활동 지역 - 활동 형태가 OnOffLine, Offline 일 경우에만 유효함")
    val location: String?,

    @field:Schema(example = "true", description = "마감된 컨텐츠 포함 여부")
    val includeClosed: Boolean?,

    @field:Schema(example = "1", description = "가져올 데이터의 페이지")
    val page: Long = 1,

    @field:Schema(example = "10", defaultValue = "10", description = "페이지당 row 수 (값이 비어있으면 기본 10개)")
    val limit: Long = 10,
)
