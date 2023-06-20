package com.api.heys.domain.verificationcode.vo

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


data class SendVerificationCodeReqVo (
    @field:NotBlank(message = "휴대폰 번호를 입력해주세요.")
    @field:Pattern(
        regexp = "^01([0|1|6|7|8|9])(\\d{3,4})(\\d{4})$",
        message = "유효한 휴대폰 번호를 입력해주세요."
    )
    val phone : String
)