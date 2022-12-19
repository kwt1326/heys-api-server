package com.api.heys.entity

import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IUserRepository: CrudRepository<Users, Long>, QuerydslPredicateExecutor<Users>{
    fun findByPhone(phone: String): Users?
}