package com.api.heys.domain.channel

import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.constants.enums.ChannelType
import com.api.heys.domain.channel.dto.*
import org.springframework.http.ResponseEntity

interface IChannelService {
    fun createChannel(dto: CreateChannelData, token: String, contentId: Long): ResponseEntity<CreateChannelResponse>

    fun createChannel(dto: CreateChannelData, token: String): ResponseEntity<CreateChannelResponse>

    fun joinChannel(channelId: Long, token: String): ResponseEntity<JoinChannelResponse>

    fun putChannelDetail(channelId: Long, dto: PutChannelData, token: String): ResponseEntity<ChannelPutResponse>

    fun getChannelDetail(channelId: Long, token: String): ResponseEntity<GetChannelDetailResponse>

    fun getChannels(type: ChannelType, params: GetChannelsParam, contentId: Long?): ResponseEntity<GetChannelsResponse>

    fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Long>

    fun getMyChannels(status: ChannelMemberStatus?, token: String): ResponseEntity<GetMyChannelsResponse>

    fun getChannelFollowers(channelId: Long, status: ChannelMemberStatus): ResponseEntity<GetChannelFollowersResponse>

    fun toggleActiveNotify(channelId: Long, token: String): ResponseEntity<ChannelPutResponse>

    fun requestAllowReject(isAllow: Boolean, userId: Long, channelId: Long, msg: String, leaderToken: String): ResponseEntity<ChannelPutResponse>

    fun memberExitChannel(msg: String, channelId: Long, token: String): ResponseEntity<ChannelPutResponse>

    fun memberAbortRequest(msg: String, channelId: Long, token: String): ResponseEntity<ChannelPutResponse>

    fun increaseChannelView(channelId: Long, token: String): ResponseEntity<String>

    fun addBookmark(channelId: Long, token: String): ResponseEntity<String>

    fun removeBookmark(channelId: Long, token: String): ResponseEntity<String>
}