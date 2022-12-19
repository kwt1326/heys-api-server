package com.api.heys.domain.channel

import com.api.heys.domain.channel.dto.*
import com.api.heys.entity.ChannelUsers
import com.api.heys.entity.Channels

interface IChannelService {
    fun createChannel(dto: CreateChannelData, token: String): Channels?

    fun joinChannel(channelId: Long, token: String): Boolean

    fun getChannels(contentId: Long): List<ChannelListItemData>?

    fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Long>?

    fun getChannelFollowers(channelId: Long, token: String): GetChannelFollowersResponse

    fun toggleActiveNotify(channelId: Long, token: String): Boolean

    fun requestAllowReject(isAllow: Boolean, channelUserId: Long, channelId: Long, token: String): Boolean

    fun memberExitChannel(channelUserId: Long, channelId: Long, token: String): Boolean

    fun memberAbortRequest(channelUserId: Long, channelId: Long, token: String): Boolean
}