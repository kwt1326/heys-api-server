package com.api.heys.constants.enums

enum class Percentage(private val value: Int) {
    ZERO(0),
    FIFTY(50),
    HUNDRED(100);

    override fun toString() : String {
        return this.value.toString()
    }

    fun getValue(): Int {
        return this.value;
    }

    fun from(value: Int) : Percentage {
        for (percentage in values()) {
            if (percentage.value == value) {
                return percentage;
            }
        }
        throw IllegalArgumentException("Percentage : 알 수 없는 숫자입니다. : $value");
    }
}