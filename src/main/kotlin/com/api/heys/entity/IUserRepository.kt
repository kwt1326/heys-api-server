package com.api.heys.entity

import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IUserRepository: CrudRepository<User, Long>, QuerydslPredicateExecutor<User> {
    fun findByUsername(username: String): User?
}