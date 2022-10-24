package com.api.heys.entity

import javax.persistence.*

/**
 * 유저 테이블 코어
 * */

@Entity
@Table(name = "users")
class Users(
        phone: String,
        password: String,
        isAvailable: Boolean = false,
        reasonForWithdrawal: String = "",
): BaseIdentityDate() {
        @Column(name = "is_available")
        var isAvailable: Boolean = isAvailable

        @Column(name = "reason_for_withdrawal")
        var reasonForWithdrawal: String = reasonForWithdrawal

        @Column(name = "phone", length = 15)
        var phone: String = phone

        @Column(name = "password")
        var password: String = password

        @OneToOne(mappedBy = "users", cascade = [CascadeType.ALL])
        var detail: UserDetail? = null

        @OneToMany(mappedBy = "users", fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        var authentications: MutableSet<Authentication> = mutableSetOf()

        @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        var notifications: MutableSet<Notification> = mutableSetOf()
}
