package com.api.heys.domain.interest.repository

import com.api.heys.entity.InterestRelations

interface InterestCustomRepository {

    fun findUserInterestsByUserDetailId(userDetailId : Long): MutableSet<InterestRelations>?

    fun deleteAllByUserDetailId(userDetailId: Long): Unit

    fun findByChannelDetailAndInterestId(channelDetailId: Long, interestId: Long): InterestRelations?

    fun findByExtraContentDetailIdAndInterestId(extraContentDetailId: Long, interestId: Long): InterestRelations?
}