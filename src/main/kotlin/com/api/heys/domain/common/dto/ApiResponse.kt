package com.api.heys.domain.common.dto

import com.api.heys.constants.MessageString

data class ApiResponse<T>(
    var message: String = MessageString.SUCCESS_EN,

    var isSuccess: Boolean = true,

    var data: T? = null
) {

}