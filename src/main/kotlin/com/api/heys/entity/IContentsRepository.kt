package com.api.heys.entity

import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IContentsRepository: CrudRepository<Contents, Long>, QuerydslPredicateExecutor<Contents> {
}