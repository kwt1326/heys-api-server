package com.api.heys.domain.content.dto

import com.api.heys.constants.MessageString
import com.api.heys.domain.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "컨텐츠 게시 상태 변경 요청 결과")
data class PutTogglePublishResponse(
    @field:Schema(example = "1", type = "long")
    var contentId: Long,

    @field:Schema(example = "2023-07-29T10:33:00", type = "date", description = "게시일자")
    var publishedAt: LocalDateTime?,

    @field:Schema(example = MessageString.SUCCESS_EN, type = "string")
    override var message: String = MessageString.SUCCESS_EN
): BaseResponse
