package com.api.heys.constants.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(enumAsRef = true)
enum class Reaction {
    Like,    // 좋아요
    CheerUp, // 힘내요
    Check,   // 확인했어요
    Saved,   // 저장했어요
}