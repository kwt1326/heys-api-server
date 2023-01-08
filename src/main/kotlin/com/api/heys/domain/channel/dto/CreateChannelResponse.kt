package com.api.heys.domain.channel.dto

import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import javax.validation.constraints.NotNull

@Schema(description = "채널 생성 결과")
data class CreateChannelResponse(
    @NotNull
    @field:Schema(example = "200")
    var statusCode: HttpStatus = HttpStatus.OK,

    @NotNull
    @field:Schema(example = "success")
    override var message: String,
): BaseResponse