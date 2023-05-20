package com.api.heys.utils

import com.api.heys.constants.enums.NationType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

internal class PhoneNumberUtilsTest {


    @Test
    @DisplayName("한국 휴대폰 번호 생성하기")
    @Throws(Exception::class)
    fun getKoreaPhoneNumber () {
        // given
        val phone = "01012341234"
        // when
        val phoneNumberByNationKR = PhoneNumberUtils.getPhoneNumberByNation(phoneNumber = phone, NationType.KR)
        // then
        assertThat(phoneNumberByNationKR).isEqualTo("+8201012341234")
    }
}