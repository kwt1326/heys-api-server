package com.api.heys.domain.user.repository

import com.api.heys.entity.QAuthentication
import com.api.heys.entity.QUserDetail
import com.api.heys.entity.QUsers
import com.api.heys.entity.Users
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserCustomRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
): UserCustomRepository {
    val qUsers: QUsers = QUsers.users
    val qUserDetail: QUserDetail = QUserDetail.userDetail
    val qAuthentication: QAuthentication = QAuthentication.authentication

    override fun findUserByPhone(phone: String): Users? {
        val query = jpaQueryFactory.selectFrom(qUsers)
            .join(qUsers.detail, qUserDetail).fetchJoin()
            .join(qUsers.authentications, qAuthentication).fetchJoin()
            .where(qUsers.phone.eq(phone))

        return query.fetchOne()
    }
}