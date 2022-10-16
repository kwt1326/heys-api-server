package com.api.heys.domain.user.dto

import com.api.heys.constants.enums.Gender
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class SignUpData(
        @NotNull
        @NotBlank
        val phone: String,
        @NotNull
        @NotBlank
        val username: String,
        @NotNull
        @NotBlank
        val password: String,
        @NotNull
        val age: Int,
        @NotNull
        val gender: Gender,
        @NotNull
        val interests: MutableSet<String>,
)
