package com.api.heys.entity

import lombok.Getter
import javax.persistence.*

@Getter
@Entity
@Table(name = "authentication")
class Authentication(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id")
        private var userId: Long,

        @OneToOne
        @PrimaryKeyJoinColumn(name = "user_id")
        var user: User,

        @Column(name = "phone", length = 20, unique = true, nullable = false)
        var phone: String,

        @Column(name = "email", length = 50, unique = true, nullable = false)
        var email: String,

        @Column(name = "password", length = 50, nullable = false)
        var password: String,
)