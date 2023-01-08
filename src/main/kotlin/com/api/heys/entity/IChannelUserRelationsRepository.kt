package com.api.heys.entity

import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface IChannelUserRelationsRepository: CrudRepository<ChannelUserRelations, Long>, QuerydslPredicateExecutor<ChannelUserRelations> {
    @Query("SELECT cur FROM ChannelUserRelations cur WHERE cur.user.id = :id AND cur.removedAt IS NULL")
    fun getChannelUserRelByChannelUserId(@Param("id") channelUserId: Long): ChannelUserRelations?
}