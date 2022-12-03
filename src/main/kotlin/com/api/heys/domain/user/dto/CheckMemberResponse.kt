package com.api.heys.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "checkMember 요청 결과")
data class CheckMemberResponse(
        @field:Schema(example = "true", type = "boolean")
        val result: Boolean,
)
