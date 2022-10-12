package com.api.heys.entity

import com.api.heys.constants.enums.Gender
import lombok.Getter
import javax.persistence.*

@Getter
@Entity
@Table(name = "users")
class User(
        @Column(name = "is_available")
        var isAvailable: Boolean = false,

        @Column(name = "reason_for_withdrawal")
        var reasonForWithdrawal: String = "",

        @Enumerated(EnumType.ORDINAL)
        @Column(name = "gender")
        val gender: Gender

        // val user_detail
        // user_channel_rels
        // notifications
): BaseIdentityDate() {
}