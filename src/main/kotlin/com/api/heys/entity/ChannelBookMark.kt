package com.api.heys.entity

import javax.persistence.*

@Entity
@Table(name = "channel_book_mark")
class ChannelBookMark (
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