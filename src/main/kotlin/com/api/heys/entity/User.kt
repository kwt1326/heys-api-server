package com.api.heys.entity

import com.api.heys.constants.enums.Gender
import lombok.Getter
import javax.persistence.*

@Getter
@Entity
@Table(name = "users")
class User(
        isAvailable: Boolean = false,
        reasonForWithdrawal: String = "",
): BaseIdentityDate() {
        @Column(name = "is_available")
        var isAvailable: Boolean = isAvailable

        @Column(name = "reason_for_withdrawal")
        var reasonForWithdrawal: String = reasonForWithdrawal

        @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
        var detail: UserDetail? = null

        // var user_channel_rels: MutableSet<UserChannelRel>

        // var notifications: MutableSet<Notification>
}
