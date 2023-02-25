package com.api.heys.domain.channel

import com.api.heys.constants.enums.ChannelType
import com.api.heys.domain.channel.dto.*
import org.springframework.http.ResponseEntity

interface IChannelService {
    fun createChannel(dto: CreateChannelData, token: String): ResponseEntity<CreateChannelResponse>

    fun createChannel(dto: CreateStudyChannelData, token: String): ResponseEntity<CreateChannelResponse>

    fun joinChannel(channelId: Long, token: String): ResponseEntity<JoinChannelResponse>

    fun getChannels(type: ChannelType, params: GetChannelsParam, contentId: Long?): ResponseEntity<GetChannelsResponse>

    fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Long>

    fun getChannelFollowers(channelId: Long, token: String): ResponseEntity<GetChannelFollowersResponse>

    fun toggleActiveNotify(channelId: Long, token: String): ResponseEntity<ChannelPutResponse>

    fun requestAllowReject(isAllow: Boolean, userId: Long, channelId: Long, msg: String, token: String): ResponseEntity<ChannelPutResponse>

    fun memberExitChannel(msg: String, userId: Long, channelId: Long, token: String): ResponseEntity<ChannelPutResponse>

    fun memberAbortRequest(msg: String, userId: Long, channelId: Long, token: String): ResponseEntity<ChannelPutResponse>
}