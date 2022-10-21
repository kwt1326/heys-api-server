package com.api.heys.entity

import com.api.heys.constants.enums.ChannelMemberStatus
import com.fasterxml.jackson.annotation.JsonFormat
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 채널유저 테이블
 * 합류 채널 / 대기 채널 리스트를 가진다.
 */

@Entity
@Table(name = "channel_users")
class ChannelUsers(
        user: Users,
        status: ChannelMemberStatus,
): BaseIdentityDate() {
    @OneToOne
    @JoinColumn(name = "user_id")
    var user: Users = user

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: ChannelMemberStatus = status

    // 탈퇴 사유
    @Column(name = "exit_message", nullable = true)
    var exitMessage: String = ""

    // 승인 거절 사유
    @Column(name = "refuse_message", nullable = true)
    var refuseMessage: String = ""

    // 승인 요청 날짜
    @JsonFormat(pattern = "yyyy-mm-dd")
    @CreationTimestamp
    @Column(name = "approve_request_at")
    var approveRequestAt: LocalDateTime? = null

    // 합류/대기 채널 리스트 조인 테이블
    @OneToMany(mappedBy = "channelUser", fetch = FetchType.LAZY)
    var channelUserRelations: MutableSet<ChannelUserRelations> = mutableSetOf()
}