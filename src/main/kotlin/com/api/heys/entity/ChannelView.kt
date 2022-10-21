package com.api.heys.entity

import javax.persistence.*

/**
 * 채널 조회 테이블
 */

@Entity
@Table(name = "channel_view")
class ChannelView(
        channel: Channels
): BaseIdentity() {
    @Column(name = "count")
    var count: Long = 0

    @OneToOne
    @JoinColumn(name = "channel_id")
    var channel: Channels = channel

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var viewers: MutableSet<Users> = mutableSetOf()
}