package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "채널 참가 요청 양식")
data class JoinChannelData(
        @NotNull
        @NotBlank
        @field:Schema(description = "", example = "")
        val name: String
)
