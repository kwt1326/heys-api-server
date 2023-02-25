package com.api.heys.domain.content.repository

import com.api.heys.entity.Contents
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository

interface IContentsRepository: CrudRepository<Contents, Long>, QuerydslPredicateExecutor<Contents>, ContentCustomRepository