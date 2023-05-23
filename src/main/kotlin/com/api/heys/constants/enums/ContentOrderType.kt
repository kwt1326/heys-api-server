package com.api.heys.constants.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class ContentOrderType {
    Default, // 최근 업로드 순
    Dday, // d-day 작은 순
    Popular, // 조회 수 높은 순
}