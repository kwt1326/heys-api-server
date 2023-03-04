package com.api.heys.domain.channel.dto

import com.api.heys.constants.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "채널 유저 데이터")
data class ChannelUserData(
        @field:Schema(example = "1", type = "long")
        var id: Long,
        @field:Schema(implementation = Gender::class, description = "MVP 단계에서는 성별로 프로필을 나타냅니다.")
        var gender: Gender,
        @field:Schema(example = "서강준", type = "string", description = "유저명")
        var username: String,
        @field:Schema(example = "2023-02-01T00:00:00", type = "string", description = "요청일자")
        var requestedAt: String,
)
