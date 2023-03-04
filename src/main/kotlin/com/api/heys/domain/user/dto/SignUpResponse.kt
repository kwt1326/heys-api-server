package com.api.heys.domain.user.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

@Schema(description = "signUp 요청 결과")
data class SignUpResponse(
        @field:Schema(example = "Bearer token123123123123123", type = "string")
        val token: String,

        @field:Schema(example = MessageString.SUCCESS_EN, type = "string")
        override var message: String = MessageString.SUCCESS_EN
): BaseResponse