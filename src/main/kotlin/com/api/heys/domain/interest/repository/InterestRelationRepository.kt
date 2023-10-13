package com.api.heys.domain.interest.repository

import com.api.heys.entity.InterestRelations
import org.springframework.data.jpa.repository.JpaRepository

interface InterestRelationRepository : JpaRepository <InterestRelations, Long>, InterestCustomRepository