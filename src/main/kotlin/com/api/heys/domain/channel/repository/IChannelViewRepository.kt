package com.api.heys.domain.channel.repository

import com.api.heys.entity.ChannelView
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IChannelViewRepository: CrudRepository<ChannelView, Long>, QuerydslPredicateExecutor<ChannelView>