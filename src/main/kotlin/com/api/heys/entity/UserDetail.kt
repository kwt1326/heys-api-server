package com.api.heys.entity

import com.api.heys.constants.enums.Gender
import com.api.heys.constants.enums.UserPersonality
import net.minidev.json.annotate.JsonIgnore
import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.FetchType.*

/**
 * 유저 상세 정보 테이블
 * */

@Entity
@Table(name = "user_detail")
class UserDetail(
        users: Users,
        gender: Gender,
        age: LocalDate,
        username: String,
): Serializable {
        @Id
        @Column(name = "user_id")
        var id: Long = 0

        @MapsId
        @OneToOne(fetch = LAZY)
        @JoinColumn(name = "user_id")
        var users: Users = users

        @OneToMany(mappedBy = "userDetail", cascade = [CascadeType.PERSIST])
        var interestRelations: MutableSet<InterestRelations> = mutableSetOf()

        @Enumerated(EnumType.ORDINAL)
        @Column(name = "gender", nullable = false)
        var gender: Gender = gender

        @Column(name = "username", nullable = false, length = 50)
        var username: String = username

        @Column(name = "age", nullable = false, length = 20)
        var age: LocalDate = age

        @Column(name = "job", length = 50)
        var job: String = ""

        @Column(name = "capability")
        var capability: String = ""

        @Column(name = "introduce_text")
        var introduceText: String = ""

        @Enumerated(EnumType.STRING)
        @Column(name = "user_personality")
        var userPersonality: UserPersonality? = null

        @OneToMany(mappedBy = "userDetail")
        var profileLink: Set<UserProfileLink> = setOf();

}