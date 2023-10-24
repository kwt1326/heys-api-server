package com.api.heys.entity

import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.IDENTITY

@Entity
class WithdrawUserReason(
    users: Users,
    val reason: String
) {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long = 0

    @MapsId
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    var users: Users = users
}