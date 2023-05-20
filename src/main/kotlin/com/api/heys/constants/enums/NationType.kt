package com.api.heys.constants.enums

enum class NationType(private val prefix: String) {

    KR("+82");

    fun getPrefix() : String {
        return this.prefix
    }
}