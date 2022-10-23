package com.api.heys.constants.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class Gender {
    Male,
    Female,
    NonBinary,
}