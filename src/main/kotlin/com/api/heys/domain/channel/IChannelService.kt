package com.api.heys.domain.channel

import com.api.heys.domain.channel.dto.*
import com.api.heys.entity.Channels
import org.springframework.http.ResponseEntity

interface IChannelService {
    fun createChannel(dto: CreateChannelData, token: String): CreateChannelResponse

    fun joinChannel(channelId: Long, token: String): ResponseEntity<JoinChannelResponse>

    fun getChannels(contentId: Long): List<ChannelListItemData>?

    fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Long>?

    fun getChannelFollowers(channelId: Long, token: String): ResponseEntity<GetChannelFollowersResponse>

    fun toggleActiveNotify(channelId: Long, token: String): ResponseEntity<ChannelPutResponse>

    fun requestAllowReject(isAllow: Boolean, userId: Long, channelId: Long, msg: String, token: String): ResponseEntity<ChannelPutResponse>

    fun memberExitChannel(msg: String, userId: Long, channelId: Long, token: String): ResponseEntity<ChannelPutResponse>

    fun memberAbortRequest(msg: String, userId: Long, channelId: Long, token: String): ResponseEntity<ChannelPutResponse>
}