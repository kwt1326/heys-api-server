package com.api.heys.domain.content.dto

import com.api.heys.constants.enums.ContentFilterSort
import com.api.heys.constants.enums.Online
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "getContents query params")
data class GetContentsParam(
        @field:Schema(implementation = ContentFilterSort::class, description = "정렬 기준 키워드")
        val sort: ContentFilterSort?,

        @field:ArraySchema(schema = Schema(example = "연애", type = "string"))
        val interests: List<String>?,

        @field:Schema(implementation = Online::class, description = "활동 형태")
        val online: Online?,

        @field:Schema(implementation = Online::class, description = "모집 마감 일자")
        val lastRecruitDate: LocalDateTime?,

        @field:Schema(example = "false", description = "마감된 컨텐츠 포함 여부")
        val includeClosed: Boolean?,
)