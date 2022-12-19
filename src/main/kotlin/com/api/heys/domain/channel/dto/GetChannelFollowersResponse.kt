package com.api.heys.domain.channel.dto

import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getChannelFollowers 요청 결과")
data class GetChannelFollowersResponse(
        @field:ArraySchema(schema = Schema(implementation = ChannelUserData::class))
        var joined: List<ChannelUserData> = listOf(),

        @field:ArraySchema(schema = Schema(implementation = ChannelUserData::class))
        var waiting: List<ChannelUserData> = listOf(),

        @field:Schema(example = "false", type = "Boolean")
        var activeNotify: Boolean = false,

        @field:Schema(example = "success", type = "string")
        override var message: String = "success"
): BaseResponse
