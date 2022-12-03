package com.api.heys.domain.channel

import com.api.heys.domain.channel.dto.ChannelListItemData
import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.domain.channel.dto.GetChannelFollowersResponse
import com.api.heys.domain.channel.dto.GetJoinAndWaitingChannelsResponse
import com.api.heys.entity.ChannelUsers
import com.api.heys.entity.Channels

interface IChannelService {
    fun createChannel(dto: CreateChannelData, token: String): Channels?

    fun joinChannel(channelId: Long, token: String): Boolean

    fun getChannels(contentId: Long): List<ChannelListItemData>?

    fun getJoinAndWaitingChannelCounts(token: String): HashMap<String, Int>?

    fun getChannelFollowers(channelId: Long, token: String): GetChannelFollowersResponse
}