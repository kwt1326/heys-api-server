package com.api.heys.constants.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class ChannelMemberStatus {
    Approved, // 승인
    Refused,  // 승인 거절
    Waiting,  // 대기중
    Exited,   // 탈퇴
}