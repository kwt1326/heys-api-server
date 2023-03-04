package com.api.heys.domain.channel.repository

import com.api.heys.entity.ChannelBookMark
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IChannelBookMarkRepository: CrudRepository<ChannelBookMark, Long>, QuerydslPredicateExecutor<ChannelBookMark>