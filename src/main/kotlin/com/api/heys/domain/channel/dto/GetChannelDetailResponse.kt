package com.api.heys.domain.channel.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getChannelDetail 요청 결과")
data class GetChannelDetailResponse(
    @field:Schema(implementation = GetChannelDetailData::class)
    var data: GetChannelDetailData? = null,

    @field:Schema(example = MessageString.SUCCESS_EN, type = "string")
    override var message: String = MessageString.SUCCESS_EN
): BaseResponse