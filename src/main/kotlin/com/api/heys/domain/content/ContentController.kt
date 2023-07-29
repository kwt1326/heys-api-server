package com.api.heys.domain.content

import com.api.heys.domain.content.dto.*

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@Tag(name = "content")
@RestController
@RequestMapping("content")
class ContentController(@Autowired private val contentService: ContentService) {
    @Operation(
        summary = "컨텐츠 상세 정보",
        description = "컨텐츠 상세 정보 API 입니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = GetExtraContentDetailResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @GetMapping("extra/{id}")
    fun getExtraContentDetail(
        @PathVariable id: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
    ): ResponseEntity<GetExtraContentDetailResponse> {
        return contentService.getExtraContentDetail(id, bearer)
    }

    @Operation(
        summary = "컨텐츠 필터링 리스트",
        description = "컨텐츠 리스트의 필터링 결과를 가져오는 API 입니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = GetExtraContentsResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @GetMapping("extra")
    fun getExtraContents(params: GetExtraContentsParam): ResponseEntity<GetExtraContentsResponse> {
        return contentService.getExtraContents(params)
    }

    @Operation(
        summary = "컨텐츠 필터링 리스트 (using Admin)",
        description = "컨텐츠 리스트의 필터링 결과를 가져오는 API 입니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = GetExtraContentsResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @GetMapping("extra/admin")
    fun getExtraContentsForAdmin(
        params: GetExtraContentsParam,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
    ): ResponseEntity<GetExtraContentsResponse> {
        return contentService.getExtraContentsForAdmin(params, bearer)
    }

    @Operation(
        summary = "공모전/대외활동 등 외부 컨텐츠 생성",
        description = "공모전/대외활동 등 외부 컨텐츠 생성 API 입니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = CreateContentResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @PostMapping("extra")
    fun createExtraContent(
        @Valid @RequestBody body: CreateExtraContentData,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
    ): ResponseEntity<CreateContentResponse> {
        return contentService.createExtraContent(body, bearer)
    }

    @Operation(
        summary = "컨텐츠 내용 수정하기",
        description = "컨텐츠 내용 수정 API 입니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = ContentPutResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @PutMapping("extra/{id}")
    fun putExtraContent(
        @PathVariable id: Long,
        @Valid @RequestBody body: PutExtraContentData
    ): ResponseEntity<ContentPutResponse> {
        return contentService.putExtraContentDetail(id, body)
    }

    @Operation(
        summary = "컨텐츠 게시 중단 및 해제",
        description = "컨텐츠 게시 중단 및 해제 상태 전환 API 입니다. (publish/unPublish)",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = ContentPutResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @PutMapping("toggle-publish/{id}")
    fun putTogglePublishStateContent(
        @PathVariable id: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String,
    ): ResponseEntity<PutTogglePublishResponse> {
        return contentService.putTogglePublishStateContent(id, bearer)
    }

    @Operation(
        summary = "컨텐츠 조회 수 증가",
        description = "컨텐츠 조회시 호출하면 해당 컨텐츠의 view count 가 1 증가합니다. 중복 적용되지 않습니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = ContentPutResponse::class),
                        mediaType = "application/json"
                    )
                ]
            )
        ]
    )
    @PutMapping("/view-count-up/{contentId}")
    fun putIncreaseViewCount(
        @PathVariable contentId: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ContentPutResponse> {
        return contentService.increaseContentView(contentId, bearer)
    }

    @Operation(
        summary = "컨텐츠 북마크 추가",
        description = "컨텐츠를 북마킹 합니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = ContentPutResponse::class),
                        mediaType = "application/json"
                    )
                ]
            )
        ]
    )
    @PutMapping("/add-bookmark/{contentId}")
    fun putAddBookmark(
        @PathVariable contentId: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ContentPutResponse> {
        return contentService.addBookmark(contentId, bearer)
    }

    @Operation(
        summary = "컨텐츠 북마크 제거",
        description = "컨텐츠 북마크를 취소합니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = ContentPutResponse::class),
                        mediaType = "application/json"
                    )
                ]
            )
        ]
    )
    @PutMapping("/remove-bookmark/{contentId}")
    fun putRemoveBookmark(
        @PathVariable contentId: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ContentPutResponse> {
        return contentService.removeBookmark(contentId, bearer)
    }

    @Operation(
        summary = "여러 개의 컨텐츠 북마크 동시 취소",
        description = "여러 개의 컨텐츠 북마크들을 동시 취소합니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = ContentPutResponse::class),
                        mediaType = "application/json"
                    )
                ]
            )
        ]
    )
    @PutMapping("/remove-bookmarks")
    fun putRemoveBookmarks(
        @Valid @RequestBody dto: PutContentRemoveRemarksData,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ContentPutResponse> {
        return contentService.removeBookmarks(dto, bearer)
    }

    @Operation(
        summary = "컨텐츠 엑셀 업로드",
        description = "정해진 포맷(중요!)의 엑셀 파일을 업로드 하여 DB 에 컨텐츠를 추가합니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = ContentPutResponse::class),
                        mediaType = "application/json"
                    )
                ]
            )
        ]
    )
    @PostMapping("upload/excel", consumes = ["multipart/form-data"])
    fun postContentExcelUpload(
        @RequestPart("file") excelFile: MultipartFile,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<CreateContentResponse> {
        return contentService.createExtraContentFromExcel(excelFile, bearer)
    }
}