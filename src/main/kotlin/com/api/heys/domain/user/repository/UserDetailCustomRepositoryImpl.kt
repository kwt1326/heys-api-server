package com.api.heys.domain.user.repository

import com.api.heys.entity.*
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserDetailCustomRepositoryImpl (
    private val jpaQueryFactory: JPAQueryFactory,
) : UserDetailCustomRepository {

    val qUsers: QUsers = QUsers.users
    val qUserDetail: QUserDetail = QUserDetail.userDetail

    override fun findUserWithDetail(phone: String): UserDetail? {
        return jpaQueryFactory.select(qUserDetail)
            .from(qUserDetail)
            .join(qUserDetail.users, qUsers).fetchJoin()
            .where(qUsers.phone.eq(phone))
            .fetchOne()
    }
}