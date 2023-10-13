package com.api.heys.domain.channel.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "repository - getChannelUserRelations 데이터 스키마")
data class GetChannelUserRelItemData(
    @field:Schema(description = "채널명", example = "코딩테스트 합격만을 위한 채널!", type = "string")
    val channelName: String,

    @field:Schema(description = "유저이름", example = "홍길동", type = "string")
    val username: String,

    @field:Schema(description = "휴대폰", example = "01012341234", type = "string")
    val phone: String?,

    @field:Schema(description = "거절사유", example = "기술 스택이 맞지 않네요 ㅠㅠ", type = "string")
    val refuseReason: String?,

    @field:Schema(description = "탈퇴사유", example = "이미 목표달성해서 나가욥", type = "string")
    val exitReason: String?
)
