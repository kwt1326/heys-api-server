package com.api.heys.domain.channel.repository

import com.api.heys.entity.Channels
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IChannelsRepository: CrudRepository<Channels, Long>, QuerydslPredicateExecutor<Channels>, ChannelCustomRepository