package com.api.heys.constants

object MessageString {
    const val SUCCESS_EN: String = "success"
    const val INVALID_USER: String = "유효하지 않은 유저입니다."
    const val NOT_FOUND_USER: String = "유저를 찾을 수 없습니다."
    const val NOT_CREATED_CHANNEL: String = "채널이 정상적으로 생성되지 않았습니다."
    const val NOT_FOUND_CHANNEL: String = "채널을 찾을 수 없습니다."
    const val NOT_FOUND_CHANNEL_DETAIL: String = "채널 정보를 찾을 수 없습니다."
    const val NOT_FOUND_CONTENT: String = "컨텐츠가 존재하지 않습니다."
    const val MAX_JOINED_CHANNEL: String = "최대 인원이 참가중입니다."
    const val CANT_JOIN_REQUEST_CHANNEL_FOR_LEADER: String = "리더는 채널 참여 요청을 할 수 없습니다. (이미 참여중이기 때문)"
    const val ALREADY_JOIN_REQUEST_CHANNEL: String = "이미 채널에 참여 신청하셨습니다."
    const val ALREADY_JOINED_CHANNEL: String = "이미 채널에 가입되어 있습니다."
    const val ONLY_USABLE_LEADER: String = "해당 기능은 리더만 사용할 수 있습니다."
}