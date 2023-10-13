package com.api.heys.domain.notification.controller

import com.api.heys.domain.common.dto.ApiResponse
import com.api.heys.domain.notification.service.NotificationService
import com.api.heys.domain.notification.vo.NotificationResponseVo
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/app/notifications")
class NotificationController(
    private val notificationService: NotificationService
){

    @GetMapping
    @Operation(summary = "나의 알림 리스트 조회", description = "나의 알림 리스트 조회 API")
    fun getNotification(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String)
        : ResponseEntity<ApiResponse<List<NotificationResponseVo>>>?{

        val notifications = notificationService.getNotifications(bearer)
        return ResponseEntity.ok(ApiResponse(data = notifications))
    }
}