package com.api.heys.entity

import com.api.heys.constants.enums.ContentType
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface IContentsRepository: CrudRepository<Contents, Long>, QuerydslPredicateExecutor<Contents> {
    @Query("SELECT c FROM Contents c WHERE c.contentType = :type AND c.id = :id")
    fun getContentDetail(@Param("type") type: ContentType, @Param("id") id: Long): Contents?
}