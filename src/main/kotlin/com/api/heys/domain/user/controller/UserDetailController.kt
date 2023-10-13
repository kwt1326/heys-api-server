package com.api.heys.domain.user.controller

import com.api.heys.domain.aws.sms.service.impl.AwsSmsMessageService
import com.api.heys.domain.common.dto.ApiResponse
import com.api.heys.domain.user.dto.OtherUserDetailResponse
import com.api.heys.domain.user.dto.UserDetailRequest
import com.api.heys.domain.user.dto.UserDetailResponse
import com.api.heys.domain.user.service.UserDetailService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "user-detail")
@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
class UserDetailController (
    private val userDetailService: UserDetailService,
    private val awsSmsMessageService: AwsSmsMessageService
){

    @Operation(summary = "나의 정보 조회", description = "나의 정보를 반환하는 API 입니다.")
    @GetMapping("/me")
    fun getMyInfo(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String) : ResponseEntity<ApiResponse<UserDetailResponse>?>? {
        val userDetailResponse = userDetailService.getMyInfo(bearer)
        return ResponseEntity.ok(ApiResponse(data = userDetailResponse))
    }

    @Operation(summary = "나의 정보 수정하기", description = "나의 정보를 수정하는 API 입니다.")
    @PutMapping("/me")
    fun modifyMyInf(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
                    @RequestBody body: UserDetailRequest): ResponseEntity<ApiResponse<Any>?>? {
        userDetailService.modifyMyInfo(bearer, body)
        return ResponseEntity.ok(ApiResponse())
    }

    @Operation(summary = "휴대폰 번호 변경하기", description = "휴대폰 번호 수정 API 입니다.")
    @PutMapping("/me/phone")
    fun modifyMyPhoneNumber(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
                    @RequestBody body: UserDetailRequest): ResponseEntity<ApiResponse<Any>?>? {
        userDetailService.modifyMyPhone(bearer, body.phone)
        return ResponseEntity.ok(ApiResponse())
    }

    @Operation(summary = "다른 유저 상세 정보 조회", description = "다른 유저의 상세 정보를 반환하는 API 입니다.")
    @GetMapping("/users/{userId}")
    fun getOtherUserInfo(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
                         @PathVariable(name = "userId") userId: Long) : ResponseEntity<ApiResponse<OtherUserDetailResponse>?>? {
        val otherUserDetailResponse = userDetailService.findOtherUserDetail(userId)
        return ResponseEntity.ok(ApiResponse(data = otherUserDetailResponse))
    }
}