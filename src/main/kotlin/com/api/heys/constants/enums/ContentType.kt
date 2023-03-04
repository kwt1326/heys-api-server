package com.api.heys.constants.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class ContentType {
    Extra, // 공모전, 대외활동(대학생의 경우 교외활동 이므로)
}