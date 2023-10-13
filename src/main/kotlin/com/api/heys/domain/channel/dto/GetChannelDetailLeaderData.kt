package com.api.heys.domain.channel.dto

import com.api.heys.constants.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "GetChannelDetailData - Leader 데이터")
data class GetChannelDetailLeaderData(
    @field:Schema(description = "리더 id", example = "1")
    val id: Long,
    @field:Schema(description = "리더명", example = "Joanna")
    val username: String,
    @field:Schema(description = "리더 인증 id", example = "01012341234")
    val phone: String,
    @field:Schema(description = "리더 소개글", example = "안냥하세여")
    val introduceText: String,
    @field:Schema(description = "프로필 완성도", example = "77")
    val percentage: Int = 0,
    @field:Schema(description = "성별", example = "Male")
    val gender: Gender = Gender.NonBinary,
)
