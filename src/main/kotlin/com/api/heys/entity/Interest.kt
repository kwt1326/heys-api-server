package com.api.heys.entity

import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * 관심분야 테이블
 * 중복된 name 컬럼 값을 가질 수 없다.
 * InterestRelations 관계 설정 필수
 */

@Entity
@Table(name = "interest")
class Interest(
        name: String,
): BaseIdentity(), Serializable {
        @Column(name = "name", unique = true)
        var name: String = name

        @OneToMany(mappedBy = "interest", cascade = [CascadeType.PERSIST])
        var interestRelations: MutableSet<InterestRelations> = mutableSetOf()
}