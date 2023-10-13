package com.api.heys.domain.user.repository

import com.api.heys.entity.Users
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<Users, Long>, UserCustomRepository