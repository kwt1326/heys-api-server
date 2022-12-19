package com.api.heys.domain.user.repository

import com.api.heys.entity.UserDetail
import org.springframework.data.jpa.repository.JpaRepository

interface UserDetailRepository : JpaRepository<UserDetail, Long>, UserDetailCustomRepository  {
}