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

        @OneToOne(mappedBy = "user")
        var detail: UserDetail

        // user_channel_rels
        // notifications
): BaseIdentityDate()
