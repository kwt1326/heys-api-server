package com.api.heys.exception

import com.api.heys.domain.common.dto.CommonApiResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handlerCustomException(e : CustomException) : CommonApiResponse<Any> {
        return CommonApiResponse.error(e)
    }

}