package com.api.heys.domain.channel

import com.api.heys.constants.enums.ChannelType
import com.api.heys.domain.channel.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("channel")
class ChannelController(
    @Autowired private val channelService: IChannelService
) {
    @Operation(
        summary = "채널 생성",
        description = "채널 생성 (공모전, 대외활동 등) API 입니다.",
        responses = [ApiResponse(responseCode = "200", description = "successful operation")]
    )
    @PostMapping
    fun createChannel(
        @Valid @RequestBody body: CreateChannelData,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<CreateChannelResponse> {
        return channelService.createChannel(body, bearer)
    }

    @Operation(
        summary = "스터디 채널 생성",
        description = "스터디 채널 생성 API 입니다.",
        responses = [ApiResponse(responseCode = "200", description = "successful operation")]
    )
    @PostMapping("study")
    fun createStudyChannel(
        @Valid @RequestBody body: CreateStudyChannelData,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<CreateChannelResponse> {
        return channelService.createChannel(body, bearer)
    }

    @Operation(
        summary = "컨텐츠 소속 채널 리스트",
        description = "컨텐츠에 속한 채널 리스트 입니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = GetChannelsResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @GetMapping("extra/{contentId}")
    fun getContentChannels(
        @PathVariable contentId: Long,
        params: GetChannelsParam
    ): ResponseEntity<GetChannelsResponse> {
        return channelService.getChannels(ChannelType.Content, params, contentId)
    }

    @Operation(
        summary = "스터디 채널 리스트",
        description = "스터디 채널 리스트 입니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = GetChannelsResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @GetMapping("study")
    fun getStudyChannels(params: GetChannelsParam): ResponseEntity<GetChannelsResponse> {
        return channelService.getChannels(ChannelType.Study, params, null)
    }

    @Operation(
        summary = "채널 참가 신청",
        description = "채널 참가 신청(공통) API 입니다. 참가 신청 후, 대기 리스트로 들어갑니다.",
        responses = [ApiResponse(responseCode = "200", description = "successful operation")]
    )
    @PostMapping("join/{id}")
    fun joinChannel(
        @PathVariable id: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<JoinChannelResponse> {
        return channelService.joinChannel(id, bearer)
    }

    @Operation(
        summary = "나의 합류/대기 채널 수",
        description = "나의 합류/대기 채널 수를 카운팅 해주는 API 입니다. (추후 마이페이지 관리 페이지 API 로 병합 할 수 있음)",
    )
    @GetMapping("my-count")
    fun getChannelCounts(@Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String): ResponseEntity<HashMap<String, Long>> {
        return ResponseEntity.ok(channelService.getJoinAndWaitingChannelCounts(bearer))
    }

    @Operation(
        summary = "채널 팔로워 리스트",
        description = "채널에 팔로우(승인 대기/참여중)한 유저들의 리스트 입니다. 컨텐츠 상세 페이지에서 사용할 수 있습니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(
                        schema = Schema(implementation = GetChannelFollowersResponse::class),
                        mediaType = "application/json"
                    )
                ]
            ),
        ]
    )
    @GetMapping("follow/{id}")
    fun getChannelFollowers(
        @PathVariable id: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<GetChannelFollowersResponse> {
        return channelService.getChannelFollowers(id, bearer)
    }

    @Operation(
        summary = "가입 요청 승인",
        description = "채널 가입 요청 대기 중인 유저를 승인 처리합니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = ChannelPutResponse::class), mediaType = "application/json")
                ]
            ),
        ]
    )
    @PutMapping("request-allow/{id}/{followerId}")
    fun putChannelRequestAllow(
        @Valid @RequestBody dto: ChannelPutData,
        @PathVariable id: Long,
        @PathVariable followerId: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ChannelPutResponse> {
        return channelService.requestAllowReject(true, followerId, id, dto.message, bearer)
    }

    @Operation(
        summary = "가입 요청 거부",
        description = "채널 가입 요청 대기 중인 유저를 거부 처리합니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = Boolean::class), mediaType = "application/json")
                ]
            ),
        ]
    )
    @PutMapping("request-reject/{id}/{followerId}")
    fun putChannelRequestReject(
        @Valid @RequestBody dto: ChannelPutData,
        @PathVariable id: Long,
        @PathVariable followerId: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ChannelPutResponse> {
        return channelService.requestAllowReject(false, followerId, id, dto.message, bearer)
    }

    @Operation(
        summary = "채널 알림 활성화",
        description = "채널 알림 활성화 상태 변경을 위한 API 입니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = Boolean::class), mediaType = "application/json")
                ]
            ),
        ]
    )
    @PutMapping("active-notify/{id}")
    fun putToggleActiveNotify(
        @PathVariable id: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ChannelPutResponse> {
        return channelService.toggleActiveNotify(id, bearer)
    }

    @Operation(
        summary = "멤버 가입 요청 취소",
        description = "채널 가입 요청을 멤버가 취소합니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = Boolean::class), mediaType = "application/json")
                ]
            ),
        ]
    )
    @PutMapping("member-abort-request/{id}/{followerId}")
    fun putMemberAbortRequest(
        @Valid @RequestBody dto: ChannelPutData,
        @PathVariable id: Long,
        @PathVariable followerId: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ChannelPutResponse> {
        return channelService.memberAbortRequest(dto.message, followerId, id, bearer)
    }

    @Operation(
        summary = "멤버 채널 탈퇴",
        description = "멤버가 채널을 탈퇴합니다.",
        responses = [
            ApiResponse(
                responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = Boolean::class), mediaType = "application/json")
                ]
            ),
        ]
    )
    @PutMapping("member-exit-channel/{id}/{followerId}")
    fun putMemberExitChannel(
        @Valid @RequestBody dto: ChannelPutData,
        @PathVariable id: Long,
        @PathVariable followerId: Long,
        @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<ChannelPutResponse> {
        return channelService.memberExitChannel(dto.message, followerId, id, bearer)
    }
}