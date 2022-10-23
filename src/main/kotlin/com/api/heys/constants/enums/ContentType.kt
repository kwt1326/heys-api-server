package com.api.heys.constants.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class ContentType {
    Study,           // 스터디
    Contest,         // 공모전
    Extracurricular, // 대외활동(대학생의 경우 교외활동 이므로)
}