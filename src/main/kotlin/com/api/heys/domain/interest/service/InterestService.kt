package com.api.heys.domain.interest.service

import com.api.heys.domain.interest.repository.InterestRelationRepository
import com.api.heys.domain.interest.repository.InterestRepository
import com.api.heys.entity.Interest
import com.api.heys.entity.InterestRelations
import com.api.heys.entity.UserDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class InterestService(
    @Autowired private val interestRepository: InterestRepository,
    @Autowired private val interestRelationRepository: InterestRelationRepository
) {

    @Transactional
    fun modifyInterests(userDetail: UserDetail, interestNames: Set<String>) {

        val interestMap: Map<String, Interest>? =
            interestRepository.findAllByNameIn(interestNames)?.associate { it.name to it }

        val interestRelations: MutableSet<InterestRelations> = mutableSetOf()
        interestNames.forEach {
            var interest: Interest? = interestMap?.get(it)

            if (interest == null) {
                interest = Interest(name = it)
            }

            val interestRelation =  InterestRelations()
            interestRelation.interest = interest
            interestRelation.userDetail = userDetail
            interestRelations.add(interestRelation)
        }

        deleteInterests(userDetailId = userDetail.id)
        interestRelationRepository.saveAll(interestRelations)
    }

    @Transactional
    fun deleteInterests(userDetailId: Long) {
        interestRelationRepository.deleteAllByUserDetailId(userDetailId = userDetailId)
    }

    fun findUserInterests(userDetailId : Long) : Set<InterestRelations> {
        return interestRelationRepository.findUserInterestsByUserDetailId(userDetailId) ?: setOf()
    }
}