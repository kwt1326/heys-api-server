package com.api.heys.domain.user.dto

import com.api.heys.constants.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "회원가입 요청 Body 데이터 입니다.")
data class SignUpData(
        @NotNull
        @NotBlank
        @field:Schema(example = "01012341234", type = "string", description = "phone 이자 로그인을 위한 username 으로 사용됩니다.")
        val phone: String,

        @NotNull
        @NotBlank
        @field:Schema(example = "Joenna", type = "string", description = "실제 유저 이름, ID 가 아닙니다.")
        val username: String,

        @NotNull
        @NotBlank
        @field:Schema(example = "12341234", type = "string", description = "비밀번호")
        val password: String,

        @NotNull
        @field:Schema(example = "29", type = "int", description = "나이")
        val age: Int,

        @NotNull
        @field:Schema(example = "NonBinary", description = "성별", implementation = Gender::class)
        val gender: Gender,

        @field:Schema(example = "[\"자기계발\", \"스터디\"]", type = "array<string>", description = "관심분야")
        val interests: MutableSet<String>? = null,
)
