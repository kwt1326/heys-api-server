package com.api.heys.domain.common.dto

data class ApiResponse<T>(
    var message: String = "success",

    var isSuccess: Boolean = true,

    var data: T? = null
) {


}