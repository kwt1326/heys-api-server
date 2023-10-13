package com.api.heys.domain.profilelink.repository

import com.api.heys.entity.QUserProfileLink
import com.querydsl.jpa.impl.JPAQueryFactory

class UserProfileLinkCustomRepositoryImpl (
    private val jpaQueryFactory: JPAQueryFactory
) : UserProfileLinkCustomRepository
{
    val qUserProfileLink = QUserProfileLink.userProfileLink

    override fun deleteAllByUserDetailId(userDetailId: Long) {
        jpaQueryFactory.delete(qUserProfileLink).where(qUserProfileLink.userDetail.id.eq(userDetailId)).execute()
    }

}