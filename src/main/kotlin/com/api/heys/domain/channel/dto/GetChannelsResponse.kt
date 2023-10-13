package com.api.heys.domain.channel.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getChannelsResponse 요청 결과")
data class GetChannelsResponse(
        @field:ArraySchema(schema = Schema(implementation = ChannelListItemData::class))
        val data: List<ChannelListItemData>,

        @field:Schema(example = "0", description = "현재 필터에서의 전체 페이지 수")
        val totalPage: Long = 0,

        @field:Schema(example = MessageString.SUCCESS_EN, type = "string")
        override var message: String = MessageString.SUCCESS_EN
): BaseResponse
