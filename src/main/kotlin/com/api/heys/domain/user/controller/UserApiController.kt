package com.api.heys.domain.user.controller

import com.api.heys.domain.common.dto.CommonApiResponse
import com.api.heys.domain.user.dto.ModifyPasswordRequest
import com.api.heys.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
class UserApiController(
    private val userService: UserService
) {

    @Operation(
        summary = "토큰 없이 패스워드 변경",
        description = "헤더에 토큰 없이 휴대폰 번호를 통해 패스워드 변경"
    )
    @PutMapping("/password")
    fun modifyUserPassword(@RequestBody body: ModifyPasswordRequest) : CommonApiResponse<Any> {
        userService.modifyPassword(null, body)
        return CommonApiResponse.ok()
    }

}