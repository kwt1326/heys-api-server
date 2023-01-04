package com.api.heys.domain.user.repository

import com.api.heys.domain.user.dto.UserDetailSearchDto
import com.api.heys.entity.*
import com.api.heys.entity.QUserDetail.*
import com.api.heys.entity.QUsers.*
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserDetailCustomRepositoryImpl (
    private val jpaQueryFactory: JPAQueryFactory,
) : UserDetailCustomRepository {

    override fun findUserDetail(userDetailSearchDto: UserDetailSearchDto): UserDetail? {
        return jpaQueryFactory.select(userDetail).from(userDetail)
            .join(userDetail.users, users).fetchJoin()
            .where(equalUserId(userDetailSearchDto.userId), equalUserPhone(userDetailSearchDto.phone))
            .fetchOne()
    }

    fun equalUserPhone(phone: String?) : BooleanExpression? {
        if (phone == null) {
            return null;
        }
        return users.phone.eq(phone)
    }

    fun equalUserId(userId: Long?) : BooleanExpression? {
        if (userId == null) {
            return null;
        }
        return users.id.eq(userId)
    }
}