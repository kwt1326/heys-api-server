package com.api.heys.constants.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class ChannelMemberStatus {
    Waiting,  // 대기중
    Approved, // 승인
    /* 위 2개 상태를 제외하고, 나머지는 재가입 신청시 삭제 대상 */
    Refused,  // 승인 거절
    Canceled, // 요청 취소됨
    Exited,   // 탈퇴
}