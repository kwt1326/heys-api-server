package com.api.heys.entity

import lombok.Getter
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Getter
@Entity
@Table(name = "authentication")
class Authentication(
        // val id: User,

        @Column(name = "phone", length = 20, nullable = false, unique = true)
        var phone: String,

        @Column(name = "email", length = 50, unique = true, nullable = false)
        var email: String,

        @Column(name = "password", length = 20, nullable = false)
        var password: String,
) {}