package com.api.heys.constants.enums

import com.api.heys.constants.enums.MessageType.SendType.*

enum class MessageType(
    private val sendType : SendType,
    private val title : String?,
    private val content : String,
) {
    PHONE_NUMBER_VERIFICATION(SMS, null, "[heys] 인증번호[{code}]를 입력해주세요."),

    NOTICE(PUSH_TOPIC, "heys", "heys 팀의 새로운 공지사항이에요!"),
    CHANNEL_APPROVAL(PUSH_TARGET, "heys", "[{channel}]채널에서 승인요청을 수락했어요!"),
    CHANNEL_REFUSE(PUSH_TARGET, "heys", "[{channel}] 채널에서 요청을 거절했어요."),
    CHANNEL_APPROVAL_REQUEST(PUSH_TARGET, "heys", "[{user}]이 [{channel}] 채널에 승인을 요청했어요!"),
    CHANNEL_PARTICIPATION(PUSH_TARGET, "heys", "[{user}]이 [{channel}] 채널에 새로 들어왔어요!"),
    CHANNEL_REQUEST_CANCEL(PUSH_TARGET, "heys", "[{channel}] 채널에 참여를 요청한 [{user}]이 요청을 취소했어요."),
    EXIT_CHANNEL(PUSH_TARGET, "heys", "[{channel}] 에 참여중인 [{user}]이 채널을 나갔어요.");

    enum class SendType {
        SMS, PUSH_TOPIC, PUSH_TARGET
    }

    fun sendType() : SendType {
        return this.sendType
    }

    fun title() : String? {
        return this.title
    }

    fun content() : String {
        return this.content
    }
}