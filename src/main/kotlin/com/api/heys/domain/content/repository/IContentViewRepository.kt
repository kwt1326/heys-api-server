package com.api.heys.domain.content.repository

import com.api.heys.entity.ContentView
import org.springframework.data.jpa.repository.Query
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface IContentViewRepository: CrudRepository<ContentView, Long>, QuerydslPredicateExecutor<ContentView> {
    @Query(
            value ="SELECT DISTINCT * from content_view cv where cv.content_id = ?1",
            nativeQuery = true
    )
    fun getContentView(@Param("id") id: Long): ContentView?
}