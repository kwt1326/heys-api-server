package com.api.heys.domain.channel.repository

import com.api.heys.constants.enums.ChannelMemberStatus
import com.api.heys.constants.enums.ChannelType
import com.api.heys.domain.channel.dto.ChannelListItemData
import com.api.heys.domain.channel.dto.ChannelUserData
import com.api.heys.domain.channel.dto.GetChannelDetailData
import com.api.heys.domain.channel.dto.GetChannelsParam
import com.api.heys.entity.ChannelBookMark
import com.api.heys.entity.ChannelView
import com.api.heys.entity.Channels
import com.api.heys.entity.ContentView

interface ChannelCustomRepository {
    fun getJoinAndWaitingChannelCounts(userId: Long): HashMap<String, Long>?

    fun getChannelFollowers(channelId: Long, status: ChannelMemberStatus): List<ChannelUserData>

    fun getChannels(type: ChannelType, params: GetChannelsParam, contentId: Long?): List<ChannelListItemData>

    fun getChannelDetail(channelId: Long, userId: Long): GetChannelDetailData?

    fun getChannelView(channelId: Long, userId: Long): ChannelView?

    fun getChannelBookMark(channelId: Long, userId: Long): ChannelBookMark?
}