package com.api.heys.domain.user.controller

import com.api.heys.constants.DefaultString
import com.api.heys.domain.common.dto.CommonApiResponse
import com.api.heys.domain.user.dto.*
import com.api.heys.domain.user.service.UserService

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@Tag(name = "user")
@RestController
@RequestMapping("user")
class UserController(
        @Autowired private val userService: UserService
) {
    @Operation(
            summary = "일반 유저 회원가입",
            description = "일반 유저 회원가입 API 입니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = SignUpResponse::class), mediaType = "application/json")
                ]),
            ]
    )
    @PostMapping("signUp") // TODO: @OASRequestBody 사용했을때 age Int 가 null 로 들어오는 문제 수정
    fun signUpCommon(
        @Valid @RequestBody body: CommonSignUpData
    ): ResponseEntity<SignUpResponse> {
        return userService.signUp(body, DefaultString.commonRole)
    }

    @Operation(
        summary = "어드민 회원가입",
        description = "어드민 회원가입 API 입니다.",
        responses = [
            ApiResponse(responseCode = "200", description = "successful operation", content = [
                Content(schema = Schema(implementation = SignUpResponse::class), mediaType = "application/json")
            ]),
        ]
    )
    @PostMapping("signUp/admin")
    fun signUpAdmin(
        @Valid @RequestBody body: AdminSignUpData
    ): ResponseEntity<SignUpResponse> {
        return userService.signUp(body, DefaultString.adminRole)
    }

    @Operation(
        summary = "회원탈퇴",
        description = "회원탈퇴 API 입니다. (현재 사용되지 않습니다. interface 만 구현됨)",
        responses = [
            ApiResponse(responseCode = "200", description = "successful operation", content = [
                Content(schema = Schema(implementation = Boolean::class), mediaType = "application/json")
            ]),
        ]
    )
    @PutMapping("withDrawal")
    fun withDrawal(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) token: String,
    @RequestBody withdrawalUserRequest: WithdrawalUserRequest): CommonApiResponse<Any> {
        userService.withDrawal(token, withdrawalUserRequest)
        return CommonApiResponse.ok()
    }

    @Operation(
            summary = "가입 여부 확인",
            description = "회원가입 여부 확인 API 입니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = CheckMemberResponse::class), mediaType = "application/json")
                ]),
            ]
    )
    @PutMapping("check-member")
    fun checkMember(@Valid @RequestBody body: CheckMemberData): ResponseEntity<CheckMemberResponse> {
        return ResponseEntity.ok(CheckMemberResponse(result = userService.checkMember(body)))
    }

    @Operation(
        summary = "토큰이 있는 경우 패스워드 변경",
        description = "헤더에 로그인 토큰이 있어야 변경이 가능"
    )
    @PutMapping("/password")
    fun modifyPassword(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
                            @RequestBody body: ModifyPasswordRequest): ResponseEntity<CommonApiResponse<Any>?>? {
        userService.modifyPassword(bearer, body)
        return ResponseEntity.ok(CommonApiResponse())
    }
}