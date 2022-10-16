package com.api.heys.entity

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import kotlin.jvm.Throws

interface IInterestRepository: CrudRepository<Interest, Long>, QuerydslPredicateExecutor<Interest> {
    @Throws(NotFoundException::class)
    fun findByName(name: String): Interest?
}