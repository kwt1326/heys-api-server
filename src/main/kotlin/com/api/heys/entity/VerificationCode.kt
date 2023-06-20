package com.api.heys.entity

import java.time.LocalDateTime
import javax.persistence.*
import javax.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "verification_code")
class VerificationCode (

    @Column(length = 50, nullable = false)
    val phone : String,

    @Column(length = 10, nullable = false)
    val code : String

) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    val id: Long = 0

    @Column(name = "expired_time", updatable = false)
    val expiredTime: LocalDateTime = LocalDateTime.now().plusMinutes(5)
}