package com.api.heys.domain.interest.repository

import com.api.heys.entity.InterestRelations
import com.api.heys.entity.QInterest
import com.api.heys.entity.QInterestRelations
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class InterestCustomRepositoryImpl (
    private val jpaQueryFactory: JPAQueryFactory,
): InterestCustomRepository {

    val qInterest: QInterest = QInterest.interest
    val qInterestRelations : QInterestRelations = QInterestRelations.interestRelations

    override fun findUserInterestsByUserDetailId(userDetailId : Long): MutableSet<InterestRelations>? {
        return jpaQueryFactory.select(qInterestRelations)
            .from(qInterestRelations)
            .join(qInterestRelations.interest, qInterest).fetchJoin()
            .where(qInterestRelations.userDetail.id.eq(userDetailId))
            .fetch().toMutableSet()
    }

    override fun deleteAllByUserDetailId(userDetailId: Long): Unit {
        jpaQueryFactory.delete(qInterestRelations).where(qInterestRelations.userDetail.id.eq(userDetailId)).execute()
    }
}