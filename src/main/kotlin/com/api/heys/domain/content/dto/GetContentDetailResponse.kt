package com.api.heys.domain.content.dto

import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getContentDetail 요청 결과")
data class GetContentDetailResponse(
        @field:Schema(implementation = GetContentDetailData::class)
        val data: GetContentDetailData?,

        @field:Schema(example = "success", type = "string")
        override var message: String = "success"
): BaseResponse
