package com.api.heys.entity

import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface IChannelUserRepository: CrudRepository<ChannelUsers, Long>, QuerydslPredicateExecutor<ChannelUsers> {
    @Query("SELECT cu FROM ChannelUsers cu WHERE cu.user.id = :id")
    fun getChannelUserByUserId(@Param("id") id: Long): ChannelUsers?
}