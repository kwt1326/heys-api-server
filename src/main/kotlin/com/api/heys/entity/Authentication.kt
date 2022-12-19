package com.api.heys.entity

import java.io.Serializable
import javax.persistence.*
import javax.persistence.FetchType.*

@Entity
@Table(name = "authentication")
class Authentication(
        users: Users,
        role: String,
): BaseIdentity(), Serializable {
        @ManyToOne(fetch = LAZY)
        @JoinColumn(name = "user_id")
        var users: Users = users

        @Column(name = "role", length = 20, nullable = false)
        var role: String = role
}