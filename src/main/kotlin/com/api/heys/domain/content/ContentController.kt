package com.api.heys.domain.content

import com.api.heys.constants.enums.ContentType
import com.api.heys.domain.channel.ChannelService
import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.domain.content.dto.*
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
            summary = "컨텐츠 상세 정보",
            description = "컨텐츠 상세 정보 API 입니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = GetContentDetailResponse::class), mediaType = "application/json")
                ]),
            ]
    )
    @GetMapping("/{type}/{id}")
    fun getContentDetail(@PathVariable type: ContentType, @PathVariable id: Long): ResponseEntity<GetContentDetailResponse> {
        val data: GetContentDetailData? = contentService.getContentDetail(type, id)
        if (data != null) {
            return ResponseEntity.ok(GetContentDetailResponse(data = data, message = "컨텐츠 상세 정보 가져오기 성공"))
        }
        return ResponseEntity(GetContentDetailResponse(data = null, message = "컨텐츠 상세 정보 가져오기 실패"), HttpStatus.BAD_REQUEST)
    }

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

            return ResponseEntity(CreateContentResponse(message = "스터디 컨텐츠 - 채널 생성 실패"), HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity(CreateContentResponse(message = "스터디 컨텐츠 생성 실패"), HttpStatus.BAD_REQUEST)
    }

    @Operation(
            summary = "컨텐츠 내용 수정하기",
            description = "컨텐츠 내용 수정 API 입니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = EditContentResponse::class), mediaType = "application/json")
                ]),
            ]
    )
    @PutMapping("/{id}")
    fun putContent(@PathVariable id: Long, @Valid @RequestBody body: EditContentData): ResponseEntity<EditContentResponse> {
        val isModified = contentService.putContentDetail(id, body)

        if (isModified) {
            return ResponseEntity.ok(EditContentResponse(message = "컨텐츠 수정 성공"))
        }
        return ResponseEntity(EditContentResponse(message = "컨텐츠 수정 실패"), HttpStatus.BAD_REQUEST)
    }

    @Operation(
            summary = "컨텐츠 조회수 증가",
            description = "컨텐츠 조회수를 1만큼 증가시킵니다. 로그인 중인 유저 1명당 하나의 컨텐츠의 조회수만 상승합니다.",
            responses = [ApiResponse(responseCode = "200", description = "successful operation")]
    )
    @PutMapping("/count-up/{id}")
    fun putIncreaseViewCount(
            @PathVariable id: Long,
            @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<Boolean> {
        val isIncreased = contentService.increaseContentView(id, bearer)
        if (isIncreased) {
            return ResponseEntity.ok(true)
        }
        return ResponseEntity(false, HttpStatus.BAD_REQUEST)
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
//        return ResponseEntity(CreateContentResponse(
//                statusCode = HttpStatus.BAD_REQUEST,
//                message = "스터디 컨텐츠 생성 실패"
//        ), HttpStatus.BAD_REQUEST)
//    }
}