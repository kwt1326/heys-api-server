package com.api.heys.domain.user.dto

import com.api.heys.constants.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Schema(description = "일반 유저 회원가입 요청 Body 데이터 입니다.")
data class CommonSignUpData(
    @NotNull
    @NotBlank
    @field:Schema(example = "01012341234", type = "string", description = "phone 이자 로그인을 위한 username 으로 사용됩니다.")
    override val phone: String,

    @NotNull
    @NotBlank
    @field:Schema(example = "Joenna", type = "string", description = "실제 유저 이름, ID 가 아닙니다.")
    override val username: String,

    @NotNull
    @NotBlank
    @field:Schema(example = "12341234", type = "string", description = "비밀번호")
    override val password: String,

    @NotNull
    @field:Schema(example = "2002-02-22", type = "localDate", description = "생년월일")
    override val birthDate: LocalDate,

    @NotNull
    @field:Schema(example = "NonBinary", description = "성별", implementation = Gender::class)
    val gender: Gender,

    @field:Schema(example = "[\"자기계발\", \"스터디\"]", type = "array<string>", description = "관심분야")
    val interests: MutableSet<String> = mutableSetOf(),
): SignUpData
