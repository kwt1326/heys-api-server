package com.api.heys.domain.channel.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getMyChannelsResponse 요청 결과")
data class GetMyChannelsResponse(
    @field:ArraySchema(schema = Schema(implementation = MyChannelListItemData::class))
    val data: List<MyChannelListItemData> = listOf(),

    @field:Schema(example = MessageString.SUCCESS_EN, type = "string")
    override var message: String = MessageString.SUCCESS_EN
): BaseResponse
