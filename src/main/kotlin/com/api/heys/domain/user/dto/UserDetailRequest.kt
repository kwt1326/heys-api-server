package com.api.heys.domain.user.dto

import com.api.heys.constants.enums.Gender
import io.swagger.v3.oas.annotations.media.Schema

data class UserDetailRequest(


    @field:Schema(example = "NonBinary", type = "string")
    var gender: Gender,

    @field:Schema(example = "Joenna", type = "string")
    var userName: String,

    @field:Schema(example = "개발자", type = "string")
    var job: String = "",

    @field:Schema(example = "java,Spring", type = "string")
    var capability: String = "",

    @field:Schema(example = "안녕하세요!", type = "string")
    var introduce: String = "",

    @field:Schema(example = "[\"스터디\", \"자기개발\"]", type = "array<string>")
    var interests: MutableSet<String> = mutableSetOf(),

)