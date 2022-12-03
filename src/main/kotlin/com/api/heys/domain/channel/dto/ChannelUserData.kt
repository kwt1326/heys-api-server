package com.api.heys.domain.channel.dto

import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.constants.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "채널 유저 데이터")
data class ChannelUserData(
        @field:Schema(example = "1", type = "long")
        var id: Long,
        @field:Schema(implementation = Gender::class, description = "MVP 단계에서는 성별로 프로필을 나타냅니다.")
        var gender: Gender,
        @field:Schema(example = "Waiting", type = "string")
        var status: ChannelMemberStatus,
        @field:Schema(example = "저와는 안맞는것 같네요 ㅠㅠ", type = "string")
        var exitMessage: String,
        @field:Schema(example = "저희와는 안맞는것 같네요 ㅠㅠ", type = "string")
        var refuseMessage: String
)
