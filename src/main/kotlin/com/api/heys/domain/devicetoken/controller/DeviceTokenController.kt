package com.api.heys.domain.devicetoken.controller

import com.api.heys.domain.common.dto.ApiResponse
import com.api.heys.domain.devicetoken.service.DeviceTokenService
import com.api.heys.domain.user.service.UserDetailService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/app/device")
@Tag(name = "device-token")
class DeviceTokenController (
    private val deviceTokenService: DeviceTokenService
){

    @PostMapping("/{token}")
    @Operation(summary = "디바이스 토큰 등록", description = "디바이스 토큰을 등록하는 API")
    fun registerDeviceToken(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
                            @PathVariable token : String) : ResponseEntity<ApiResponse<Any>>?{
        deviceTokenService.saveDeviceToken(bearer, token)
        return ResponseEntity.ok(ApiResponse())
    }

    @DeleteMapping("/{token}")
    @Operation(summary = "디바이스 토큰 삭제", description = "디바이스 토큰을 삭제하는 API")
    fun deleteDeviceToken(@PathVariable token : String) : ResponseEntity<ApiResponse<Any>>?{
        deviceTokenService.deleteDeviceToken(token)
        return ResponseEntity.ok(ApiResponse())
    }

}