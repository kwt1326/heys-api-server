package com.api.heys.domain.user.controller

import com.api.heys.domain.common.dto.ApiResponse
import com.api.heys.domain.common.dto.BaseResponse
import com.api.heys.domain.user.dto.SignUpData
import com.api.heys.domain.user.dto.UserDetailRequest
import com.api.heys.domain.user.dto.UserDetailResponse
import com.api.heys.domain.user.service.UserDetailService
import com.api.heys.entity.UserDetail
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
class UserDetailController (
    private val userDetailService: UserDetailService
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
}