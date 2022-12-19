package com.api.heys.domain.user.controller

import com.api.heys.constants.SecurityString
import com.api.heys.domain.user.dto.CheckMemberData
import com.api.heys.domain.user.dto.CheckMemberResponse
import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.domain.user.dto.SignUpResponse
import com.api.heys.domain.user.service.UserService

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody as OASRequestBody // 기존 RequestBody 와 이름이 같다.
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("user")
class UserController(
        @Autowired private val userService: UserService
) {
    @Operation(
            summary = "회원가입",
            description = "회원가입 API 입니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = SignUpResponse::class), mediaType = "application/json")
                ]),
            ]
    )
    @PostMapping("signUp") // TODO: @OASRequestBody 사용했을때 age Int 가 null 로 들어오는 문제 수정
    fun signUp(@Valid @RequestBody body: SignUpData): ResponseEntity<SignUpResponse> {
        val token: String? = userService.signUp(body, listOf("COMMON_USER"))
        if (token != null) {
            return ResponseEntity.ok(SignUpResponse(token = SecurityString.PREFIX_TOKEN + token))
        }
        return ResponseEntity<SignUpResponse>(SignUpResponse("", "Already Exist User"), HttpStatus.BAD_REQUEST)
    }

    @Operation(summary = "회원가입 어드민", description = "회원가입 어드민 API 입니다. 가입은 가능하나, 현재 사용되지 않습니다.")
    @PostMapping("signUp/admin")
    fun signUpAdmin(@Valid @RequestBody body: SignUpData): ResponseEntity<SignUpResponse> {
        val token: String? = userService.signUp(body, listOf("ADMIN_USER", "COMMON_USER"))
        if (token != null) {
            return ResponseEntity.ok(SignUpResponse(token = SecurityString.PREFIX_TOKEN + token))
        }
        return ResponseEntity<SignUpResponse>(SignUpResponse("", "Already Exist User"), HttpStatus.BAD_REQUEST)
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
}