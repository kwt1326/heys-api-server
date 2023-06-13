package com.api.heys.domain.verificationcode.controller

import com.api.heys.domain.verificationcode.service.VerificationCodeService
import com.api.heys.domain.verificationcode.vo.CheckVerificationCodeReqVo
import com.api.heys.domain.verificationcode.vo.VerificationCodeResVo
import com.api.heys.domain.verificationcode.vo.SendVerificationCodeReqVo
import com.api.heys.domain.common.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/app/code")
class VerificationCodeController (
    private val verificationCodeService: VerificationCodeService
){
    @PostMapping
    @Operation(summary = "휴대폰 인증 번호 전송", description = "휴대폰 인증 번호 전송 API")
    fun sendVerificationCode(@Valid @RequestBody sendVerificationCodeReqVo: SendVerificationCodeReqVo) : ResponseEntity<ApiResponse<Any>>? {
        verificationCodeService.saveVerificationCode(sendVerificationCodeReqVo)
        return ResponseEntity.ok(ApiResponse())
    }

    @DeleteMapping
    @Operation(summary = "휴대폰 인증 번호 검증", description = "휴대폰 인증 번호 검증 API")
    fun checkVerificationCode(@RequestBody checkVerificationCodeReqVo: CheckVerificationCodeReqVo) : ResponseEntity<ApiResponse<Any>>? {
        val isVerification = verificationCodeService.checkVerificationCode(checkVerificationCodeReqVo)
        val codeResVo = VerificationCodeResVo(isVerification)
        return ResponseEntity.ok(ApiResponse(data = codeResVo))
    }
}