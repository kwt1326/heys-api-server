package com.api.heys.domain.interest.repository

import com.api.heys.entity.Interest
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import kotlin.jvm.Throws

interface InterestRepository: CrudRepository<Interest, Long>, QuerydslPredicateExecutor<Interest> {
    @Throws(NotFoundException::class)
    fun findByName(name: String): Interest?
    fun findAllByNameIn(names: Set<String>): Set<Interest>?
}