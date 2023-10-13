package com.api.heys.domain.channel.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채널 상태 변경 요청 결과")
data class ChannelPutResponse(
    @field:Schema(example = MessageString.SUCCESS_EN, type = "string")
    override var message: String = MessageString.SUCCESS_EN
): BaseResponse