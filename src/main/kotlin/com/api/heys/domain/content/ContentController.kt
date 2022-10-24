package com.api.heys.domain.content

import com.api.heys.domain.channel.ChannelService
import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.domain.content.dto.CreateContentData
import com.api.heys.domain.content.dto.CreateContentResponse
import com.api.heys.entity.Channels
import com.api.heys.entity.Contents

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("content")
class ContentController(
        @Autowired private val contentService: ContentService,
        @Autowired private val channelService: ChannelService,
) {
    @Operation(
            summary = "스터디 컨텐츠 생성",
            description = "스터디 컨텐츠 생성 API 입니다. 스터디 타입은 채널을 동시에 생성합니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = CreateContentResponse::class), mediaType = "application/json")
                ]),
            ]
    )
    @PostMapping("study")
    fun createStudyContent(
            @Valid @RequestBody body: CreateContentData,
            @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
    ): ResponseEntity<CreateContentResponse> {
        val content: Contents? = contentService.createContent(body)
        if (content != null) {
            val createChannelDto = CreateChannelData(name = body.name, contentId = content.id)
            val channel: Channels? = channelService.createChannel(createChannelDto, bearer)

            if (channel != null) {
                return ResponseEntity.ok(CreateContentResponse(message = "스터디 컨텐츠 생성 성공"))
            }

            return ResponseEntity<CreateContentResponse>(CreateContentResponse(message = "스터디 컨텐츠 - 채널 생성 실패"), HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity<CreateContentResponse>(CreateContentResponse(message = "스터디 컨텐츠 생성 실패"), HttpStatus.BAD_REQUEST)
    }

//    @Operation(
//            summary = "공모전/대외활동 등 외부 컨텐츠 생성",
//            description = "공모전/대외활동 등 외부 컨텐츠 생성 API 입니다.",
//            responses = [
//                ApiResponse(responseCode = "200", description = "successful operation", content = [
//                    Content(schema = Schema(implementation = CreateContentResponse::class), mediaType = "application/json")
//                ]),
//            ]
//    )
//    @PostMapping("external")
//    fun createExternalContent(
//            @Valid @RequestBody body: CreateContentData,
//            @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
//    ): ResponseEntity<CreateContentResponse> {
//        val content: Contents? = contentService.createContent(body)
//        if (content != null) {
//            val createChannelDto = CreateChannelData(name = body.name, contentId = content.id)
//            return ResponseEntity.ok(
//                    CreateContentResponse(statusCode = HttpStatus.OK, message = "외부 컨텐츠 생성 성공")
//            )
//        }
//
//        return ResponseEntity<CreateContentResponse>(CreateContentResponse(
//                statusCode = HttpStatus.BAD_REQUEST,
//                message = "스터디 컨텐츠 생성 실패"
//        ), HttpStatus.BAD_REQUEST)
//    }
}