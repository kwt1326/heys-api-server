package com.api.heys.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus

@Schema(description = "signUp 요청 결과")
data class SignUpResponse(
        @field:Schema(example = "Bearer token123123123123123", type = "string")
        val token: String,

        @field:Schema(example = "OK", type = "HttpStatus.<StatusCode>")
        val statusCode: HttpStatus,

        @field:Schema(example = "success", type = "string")
        val message: String = "success"
)