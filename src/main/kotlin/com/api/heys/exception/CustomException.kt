package com.api.heys.exception

class CustomException(
    e : CommonCustomException
) : RuntimeException() {

    override val message : String = e.message()
}