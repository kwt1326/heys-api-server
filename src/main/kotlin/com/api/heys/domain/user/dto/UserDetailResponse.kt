package com.api.heys.domain.user.dto

import com.api.heys.constants.enums.Gender
import com.api.heys.constants.enums.UserPersonality
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class UserDetailResponse(
    @field:Schema(example = "Joenna", type = "string")
    val userName: String? = null,

    @field:Schema(example = "01012341234", type = "string")
    val phone: String? = null,

    @field:Schema(example = "NonBinary", type = "string")
    val gender: Gender? = null,

    @field:Schema(example = "2002-02-22", type = "localDate")
    val birthDate: LocalDate,

    @field:Schema(example = "개발자", type = "string")
    val job: String? = null,

    @field:Schema(example = "안녕하세요!", type = "string")
    val introduce: String? = null,

    @field:Schema(example = "java, Spring", type = "string")
    val capability: String? = null,

    @field:Schema(example = "ISTP", type = "string")
    val userPersonality: UserPersonality? = null,

    @field:Schema(example = "[\"스터디\", \"자기개발\"]", type = "array<string>")
    val interests: Set<String?>? = setOf(),

    @field:Schema(example = "[\"https://www.github.com\", \"https://www.instagram.com/\"]", type = "array<string>")
    var profileLinks: Set<String> = setOf(),

    @field:Schema(example = "0", type = "int")
    val percentage: Int = 0,

    @field:Schema(example = "2", type = "int")
    val joinChannelCount: Long? = 0,

    @field:Schema(example = "1", type = "int")
    val waitingChannelCount: Long? = 0,
    )