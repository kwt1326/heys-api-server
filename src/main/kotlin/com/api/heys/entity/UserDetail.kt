package com.api.heys.entity

import com.api.heys.constants.enums.Gender
import lombok.Getter
import java.io.Serializable
import javax.persistence.*

@Getter
@Entity
@Table(name = "user_detail")
class UserDetail(
        users: Users,
        gender: Gender,
        age: Int,
        username: String,
        interests: MutableSet<Interest>,
): Serializable {
        @Id
        @Column(name = "user_id")
        private var id: Long = 0

        @MapsId
        @OneToOne
        @JoinColumn(name = "user_id")
        var users: Users = users

        @OneToMany(fetch = FetchType.EAGER)
        @JoinColumn(name = "id")
        var interests: MutableSet<Interest> = interests

        @Enumerated(EnumType.ORDINAL)
        @Column(name = "gender", nullable = false)
        var gender: Gender = gender

        @Column(name = "username", nullable = false, length = 50)
        var username: String = username

        @Column(name = "age", nullable = false, length = 20)
        var age: Int = age

        @Column(name = "job", length = 50)
        var job: String = ""

        @Column(name = "capability")
        var capability: String = ""

        @Column(name = "introduce_text")
        var introduceText: String = ""

        @Column(name = "profile_picture_uri")
        var profilePictureUri: String = ""
}