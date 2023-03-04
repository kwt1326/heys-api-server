package com.api.heys.domain.user.dto

import java.time.LocalDate

interface SignUpData {
    val phone: String
    val username: String
    val password: String
    val birthDate: LocalDate
}
