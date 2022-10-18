package com.api.heys.entity

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import kotlin.jvm.Throws

interface IAuthenticationRepository: CrudRepository<Authentication, Long>, QuerydslPredicateExecutor<Authentication>