package com.api.heys.domain.content.dto

import com.api.heys.domain.common.dto.BaseResponse
import com.api.heys.entity.Contents
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import javax.validation.constraints.NotNull

@Schema(description = "createContent 요청 결과")
data class CreateContentResponse(
        @NotNull
        @field:Schema(implementation = Contents::class)
        var contentId: Long?,

        @NotNull
        @field:Schema(example = "200")
        var statusCode: HttpStatus = HttpStatus.OK,

        @NotNull
        @field:Schema(example = "success")
        override var message: String = "success"
): BaseResponse
