package com.api.heys.domain.content.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getExtraContentDetail 요청 결과")
data class GetExtraContentDetailResponse(
        @field:Schema(implementation = GetExtraContentDetailData::class)
        val data: GetExtraContentDetailData?,

        @field:Schema(example = MessageString.SUCCESS_EN, type = "string")
        override var message: String = MessageString.SUCCESS_EN
): BaseResponse
