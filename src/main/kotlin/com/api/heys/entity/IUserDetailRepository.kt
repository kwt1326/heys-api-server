package com.api.heys.entity

import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IUserDetailRepository: CrudRepository<UserDetail, Long>, QuerydslPredicateExecutor<UserDetail> {
    fun findByUsername(username: String): UserDetail?
}