package com.api.heys.domain.content.dto

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getExtraContentsParam query params")
data class GetExtraContentsParam(
        @field:ArraySchema(schema = Schema(example = "연애", type = "string"))
        val interests: List<String>?,

        @field:Schema(example = "2022-12-12T00:08:28", description = "모집 마감 일자")
        val lastRecruitDate: String?,

        @field:Schema(example = "true", description = "마감된 컨텐츠 포함 여부")
        val includeClosed: Boolean?,

        @field:Schema(example = "1", description = "가져올 데이터의 페이지")
        val page: Long = 1,

        @field:Schema(example = "10", defaultValue = "10", description = "페이지당 row 수 (값이 비어있으면 기본 10개)")
        val limit: Long = 10,
)