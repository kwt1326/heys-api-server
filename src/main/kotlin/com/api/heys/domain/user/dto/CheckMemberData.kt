package com.api.heys.domain.user.dto

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "회원가입 여부 확인 Body 데이터 입니다.")
data class CheckMemberData(
        @NotNull
        @NotBlank
        @field:Schema(example = "01012341234", type = "string", description = "phone 이자 로그인을 위한 username 으로 사용됩니다.")
        val phone: String,
)
