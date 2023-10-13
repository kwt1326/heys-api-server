package com.api.heys.domain.content.repository

import com.api.heys.entity.ContentView
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IContentViewRepository: CrudRepository<ContentView, Long>, QuerydslPredicateExecutor<ContentView> {
}