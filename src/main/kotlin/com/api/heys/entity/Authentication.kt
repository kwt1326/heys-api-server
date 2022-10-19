package com.api.heys.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "authentication")
class Authentication(
        users: Users,
        role: String,
): BaseIdentity(), Serializable {
        @ManyToOne
        @JoinColumn(name = "user_id")
        var users: Users = users

        @Column(name = "role", length = 20, nullable = false)
        var role: String = role
}