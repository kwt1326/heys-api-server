package com.api.heys.domain.content.repository

import com.api.heys.entity.ContentBookMark
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IContentBookMarkRepository: CrudRepository<ContentBookMark, Long>, QuerydslPredicateExecutor<ContentBookMark>