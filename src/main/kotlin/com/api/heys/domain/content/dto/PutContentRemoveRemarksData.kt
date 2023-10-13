package com.api.heys.domain.content.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "컨텐츠 북마크 여러개 제거 데이터")
data class PutContentRemoveRemarksData(
    @field:Schema(description = "북마크 한 컨텐츠 ID 리스트", example = "[\"1\", \"2\"]")
    val contentIds: Set<Long> = setOf(),
)