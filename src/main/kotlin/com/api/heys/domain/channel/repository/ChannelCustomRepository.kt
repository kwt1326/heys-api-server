package com.api.heys.domain.channel.repository

import com.api.heys.entity.Channels

interface ChannelCustomRepository {
    fun getJoinAndWaitingChannelCounts(userId: Long): HashMap<String, Long>?

    fun getJoinAndWaitingChannels(userId: Long): HashMap<String, List<Channels>>?

    fun getChannels(contentId: Long): List<Channels>
}