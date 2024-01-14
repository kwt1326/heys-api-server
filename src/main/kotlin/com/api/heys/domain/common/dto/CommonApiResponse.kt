package com.api.heys.domain.common.dto

import com.api.heys.constants.MessageString

data class CommonApiResponse<T>(
    var message: String = MessageString.SUCCESS_EN,

    var isSuccess: Boolean = true,

    var data: T? = null
) {

    companion object {
        fun ok() : CommonApiResponse<Any> {
            return CommonApiResponse()
        }

        fun <T> ok (body : T) : CommonApiResponse<T> {
            return CommonApiResponse(data = body)
        }
    }
}