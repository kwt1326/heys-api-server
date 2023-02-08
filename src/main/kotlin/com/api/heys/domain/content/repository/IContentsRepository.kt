package com.api.heys.domain.content.repository

import com.api.heys.entity.Contents
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface IContentsRepository: CrudRepository<Contents, Long>, QuerydslPredicateExecutor<Contents>, ContentCustomRepository {
    @Query("SELECT c FROM Contents c WHERE c.id = :id")
    fun getContentDetail(@Param("id") id: Long): Contents?
}