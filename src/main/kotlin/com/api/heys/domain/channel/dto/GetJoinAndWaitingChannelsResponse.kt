package com.api.heys.domain.channel.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import com.api.heys.entity.Channels
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getJoinAndWaitingChannels 요청 결과")
data class GetJoinAndWaitingChannelsResponse(
        @field:Schema(example = "[\"채널 1\", \"채널 2\"]", type = "array<string>")
        val joined: List<Channels>,

        @field:Schema(example = "[\"채널 1\", \"채널 2\"]", type = "array<string>")
        val waiting: List<Channels>,

        @field:Schema(example = MessageString.SUCCESS_EN, type = "string")
        override var message: String = MessageString.SUCCESS_EN
): BaseResponse