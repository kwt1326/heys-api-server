package com.api.heys.exception.type

import com.api.heys.exception.CommonCustomException

enum class UserExceptionType(
    private val message : String
) : CommonCustomException {

    NOT_EXIST_USER("유저가 존재하지 않습니다."),
    NOT_ACTIVATE_USER("유저가 활성화 되어있지 않습니다.");

    override fun message() : String {
        return this.message
    }

}