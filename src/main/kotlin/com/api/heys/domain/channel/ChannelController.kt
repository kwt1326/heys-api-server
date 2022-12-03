package com.api.heys.domain.channel

import com.api.heys.domain.channel.dto.GetChannelFollowersResponse
import com.api.heys.domain.channel.dto.GetChannelsResponse
import com.api.heys.domain.channel.dto.JoinChannelResponse
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
@RequestMapping("channel")
class ChannelController(
        @Autowired private val channelService: IChannelService
) {
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
        val result = channelService.joinChannel(id, bearer)
        if (result) {
            return ResponseEntity.ok(JoinChannelResponse(message = "채널 참가 요청 완료"))
        }

        return ResponseEntity(JoinChannelResponse(message = "채널 참가 요청 실패"), HttpStatus.BAD_REQUEST)
    }

    @Operation(
            summary = "컨텐츠 채널 리스트",
            description = "컨텐츠에 속한 채널 리스트 입니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = GetChannelsResponse::class), mediaType = "application/json")
                ]),
            ]
    )
    @GetMapping("list/{contentId}")
    fun getChannels(@PathVariable contentId: Long): ResponseEntity<GetChannelsResponse> {
        val result = channelService.getChannels(contentId)
        if (result != null) {
            return ResponseEntity.ok(GetChannelsResponse(data = result, message = "채널 리스트 가져오기 성공"))
        }

        return ResponseEntity(GetChannelsResponse(data = listOf(), message = "채널 리스트 가져오기 실패"), HttpStatus.BAD_REQUEST)
    }

    @Operation(
            summary = "채널 팔로워 리스트",
            description = "채널에 팔로우(승인 대기/참여중)한 유저들의 리스트 입니다. 컨텐츠 상세 페이지에서 사용할 수 있습니다.",
            responses = [
                ApiResponse(responseCode = "200", description = "successful operation", content = [
                    Content(schema = Schema(implementation = GetChannelFollowersResponse::class), mediaType = "application/json")
                ]),
            ]
    )
    @GetMapping("follow/{id}")
    fun getChannelFollowers(
            @PathVariable id: Long,
            @Schema(hidden = true) @RequestHeader(HttpHeaders.AUTHORIZATION) bearer: String
    ): ResponseEntity<GetChannelFollowersResponse> {
        return ResponseEntity.ok(channelService.getChannelFollowers(id, bearer))
    }
}