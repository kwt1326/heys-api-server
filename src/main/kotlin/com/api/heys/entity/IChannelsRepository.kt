package com.api.heys.entity

import com.api.heys.domain.channel.repository.ChannelCustomRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IChannelsRepository: CrudRepository<Channels, Long>, QuerydslPredicateExecutor<Channels>, ChannelCustomRepository {
}