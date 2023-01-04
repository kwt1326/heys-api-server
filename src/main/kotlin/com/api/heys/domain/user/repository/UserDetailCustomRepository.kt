package com.api.heys.domain.user.repository

import com.api.heys.domain.user.dto.UserDetailSearchDto
import com.api.heys.entity.UserDetail


interface UserDetailCustomRepository {

    fun findUserDetail(userDetailSearchDto: UserDetailSearchDto) : UserDetail?
}
