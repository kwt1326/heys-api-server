package com.api.heys.domain.channel.dto

import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getChannelFollowers 요청 결과")
data class GetChannelFollowersResponse(
        @field:ArraySchema(schema = Schema(implementation = ChannelUserData::class))
        var joined: List<ChannelUserData>,

        @field:ArraySchema(schema = Schema(implementation = ChannelUserData::class))
        var waiting: List<ChannelUserData>,

        @field:Schema(example = "success", type = "string")
        override var message: String = "success"
): BaseResponse
