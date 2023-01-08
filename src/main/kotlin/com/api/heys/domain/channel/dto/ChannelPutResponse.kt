package com.api.heys.domain.channel.dto

import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채널 상태 변경 요청 결과")
data class ChannelPutResponse(
    @field:Schema(example = "success", type = "string")
    override var message: String = "success"
): BaseResponse