package com.api.heys.domain.channel.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import javax.validation.constraints.NotNull

@Schema(description = "채널 생성 결과")
data class CreateChannelResponse(
    @NotNull
    @field:Schema(example = "1")
    var channelId: Long? = null,

    @NotNull
    @field:Schema(example = MessageString.SUCCESS_EN)
    override var message: String,
): BaseResponse