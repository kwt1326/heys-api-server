package com.api.heys.domain.content.dto

import com.api.heys.domain.common.dto.BaseResponse
import com.api.heys.entity.Contents
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getContentDetail 요청 결과")
data class GetContentDetailResponse(
        @field:Schema(example = "success", type = "string")
        val data: GetContentDetailData?,

        @field:Schema(example = "success", type = "string")
        override val message: String = "success"
): BaseResponse
