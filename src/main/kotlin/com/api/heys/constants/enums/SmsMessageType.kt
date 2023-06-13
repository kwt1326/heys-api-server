package com.api.heys.constants.enums

enum class SmsMessageType (
    private val pattern : String,
    private val needValueCount : Int
) {

    PHONE_NUMBER_VERIFICATION("[heys] 인증번호[{0}]를 입력해주세요.", 1);

    fun getPattern() : String {
        return this.pattern
    }

    fun getNeedValueCount() : Int {
        return this.needValueCount
    }
}