package com.api.heys.domain.channel

import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.entity.Channels

interface IChannelService {
    fun createChannel(dto: CreateChannelData, token: String): Channels?
}