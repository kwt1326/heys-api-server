package com.api.heys.utils

import com.api.heys.constants.enums.NationType

class PhoneNumberUtils {

    companion object {
        fun getPhoneNumberByNation(phoneNumber : String, nationType : NationType = NationType.KR) : String {
            return nationType.getPrefix() + phoneNumber
        }
    }
}