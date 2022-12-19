package com.api.heys.domain.content.dto

import com.api.heys.constants.enums.ContentType
import com.api.heys.constants.enums.Online
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getContents query params")
data class GetContentsParam(
        // MVP 단계에서 제거됨
//        @field:Schema(implementation = ContentFilterSort::class, description = "정렬 기준 키워드")
//        val sort: ContentFilterSort?,

        @field:ArraySchema(schema = Schema(example = "연애", type = "string"))
        val interests: List<String>?,

        @field:Schema(implementation = ContentType::class, description = "컨텐츠 종류 (Study, Contest, Extracurricular)")
        val type: ContentType,

        @field:Schema(implementation = Online::class, description = "활동 형태 (OnOffLine, Online, Offline)")
        val online: Online?,

        @field:Schema(example = "2022-12-12T00:08:28", description = "모집 마감 일자")
        val lastRecruitDate: String?,

        @field:Schema(example = "true", description = "마감된 컨텐츠 포함 여부")
        val includeClosed: Boolean?,

        @field:Schema(example = "1", description = "가져올 데이터의 페이지")
        val page: Long = 1,

        @field:Schema(example = "10", defaultValue = "10", description = "페이지당 row 수 (값이 비어있으면 기본 10개)")
        val limit: Long = 10,
)