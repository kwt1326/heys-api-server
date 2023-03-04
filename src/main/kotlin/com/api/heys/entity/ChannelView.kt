package com.api.heys.entity

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

/**
 * 채널 조회 테이블
 */

@Entity
@DynamicUpdate
@Table(name = "channel_view")
class ChannelView(
    channel: Channels,
    users: Users,
) : BaseIdentity() {
    @ManyToOne
    @JoinColumn(name = "channel_id")
    var channel: Channels = channel

    @ManyToOne
    @JoinColumn(name = "user_id")
    var users: Users = users
}