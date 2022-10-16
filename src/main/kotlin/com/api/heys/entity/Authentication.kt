package com.api.heys.entity

import lombok.Getter
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.*

@Getter
@Entity
@Table(name = "authentication")
class Authentication(
        user: User,
        phone: String,
        password: String,
): Serializable {
        @Id
        @Column(name = "user_id")
        private val id: Long = 0

        @MapsId
        @OneToOne(cascade = [CascadeType.MERGE, CascadeType.REMOVE])
        @JoinColumn(name = "user_id")
        var user: User = user

        @Column(name = "phone", length = 20, unique = true, nullable = false)
        var phone: String = phone

        @Column(name = "password", length = 50, nullable = false)
        var password: String = password
}