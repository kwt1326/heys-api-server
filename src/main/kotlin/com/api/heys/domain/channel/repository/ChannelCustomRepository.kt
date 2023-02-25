package com.api.heys.domain.channel.repository

import com.api.heys.constants.enums.ChannelType
import com.api.heys.domain.channel.dto.ChannelListItemData
import com.api.heys.domain.channel.dto.GetChannelsParam
import com.api.heys.entity.Channels

interface ChannelCustomRepository {
    fun getJoinAndWaitingChannelCounts(userId: Long): HashMap<String, Long>?

    fun getJoinAndWaitingChannels(userId: Long): HashMap<String, List<Channels>>?

    fun getChannels(type: ChannelType, params: GetChannelsParam, contentId: Long?): List<ChannelListItemData>
}