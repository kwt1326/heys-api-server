package com.api.heys.domain.user.dto

import com.api.heys.constants.enums.Gender
import com.api.heys.constants.enums.UserPersonality
import io.swagger.v3.oas.annotations.media.Schema

data class OtherUserDetailResponse (

    @field:Schema(example = "Joenna", type = "string")
    val userName: String? = null,

    @field:Schema(example = "NonBinary", type = "string")
    val gender: Gender? = null,

    @field:Schema(example = "개발자", type = "string")
    val job: String? = null,

    @field:Schema(example = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg", type = "string")
    val profileUrl: String? = null,

    @field:Schema(example = "안녕하세요!", type = "string")
    val introduce: String? = null,

    @field:Schema(example = "java, Spring", type = "string")
    val capability: String? = null,

    @field:Schema(example = "ISTP", type = "string")
    val userPersonality: UserPersonality? = null,

    @field:Schema(example = "[\"스터디\", \"자기개발\"]", type = "array<string>")
    val interests: Set<String?> = setOf(),

    @field:Schema(example = "[\"https://www.github.com\", \"https://www.instagram.com/\"]", type = "array<string>")
    var profileLinks: Set<String> = setOf(),

    ){

}