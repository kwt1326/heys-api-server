package com.api.heys.entity

import com.api.heys.constants.enums.ChannelMemberStatus
import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 채널 *-* 유저 Associate Table
 */

@Entity
@Table(name = "channel_user_relations")
class ChannelUserRelations(
    user: Users
): BaseIdentityDate() {
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: Users = user

    @ManyToOne
    @JoinColumn(name = "channel_id")
    var channel: Channels? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: ChannelMemberStatus = ChannelMemberStatus.Waiting

    // 탈퇴 사유
    @Column(name = "exit_message", nullable = true)
    var exitMessage: String = ""

    // 승인 거절 사유
    @Column(name = "refuse_message", nullable = true)
    var refuseMessage: String = ""

    // 승인 요청 날짜
    @JsonFormat(pattern = "yyyy-mm-dd")
    @Column(name = "approve_request_at")
    var approveRequestAt: LocalDateTime? = null
}