package com.api.heys.entity

import com.api.heys.constants.enums.Gender
import lombok.Getter
import java.io.Serializable
import javax.persistence.*

@Getter
@Entity
@Table(name = "user_detail")
class UserDetail(
        @Id
        private var id: Long,

        @OneToOne
        @JoinColumn(name = "detail")
        @PrimaryKeyJoinColumn(name = "id")
        var user: User,

        @Enumerated(EnumType.ORDINAL)
        @Column(name = "gender", nullable = false)
        var gender: Gender,

        @Column(name = "phone", unique = true, nullable = false, length = 20)
        var phone: String,

        @Column(name = "username", nullable = false, length = 50)
        var username: String,

        @Column(name = "age", nullable = false, length = 20)
        var age: Int,

        @Column(name = "job", length = 50)
        var job: String = "",

        @Column(name = "email", unique = true, length = 50)
        var email: String = "",

        @Column(name = "capability")
        var capability: String = "",

        @Column(name = "introduce_text")
        var introduceText: String = "",

        @Column(name = "profile_picture_uri")
        var profilePictureUri: String = "",

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(referencedColumnName = "id")
        var interests: Interest,
): Serializable