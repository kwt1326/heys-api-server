package com.api.heys.constants.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class ChannelType {
    Content, // 컨텐츠 기반 채널
    Study, // 스터디 채널
}