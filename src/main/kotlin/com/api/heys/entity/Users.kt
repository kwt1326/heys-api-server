package com.api.heys.entity

import javax.persistence.*

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

        @OneToMany(mappedBy = "users", cascade = [CascadeType.ALL])
        var authentications: MutableSet<Authentication> = mutableSetOf()

        // var user_channel_rels: MutableSet<UserChannelRel>

        // var notifications: MutableSet<Notification>

        fun addAuthentication(auth: Authentication) { authentications.add(auth) }
}
