package com.api.heys.entity

import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * 채널 *-* 채널유저 Associate Table
 */

@Entity
@Table(name = "channel_user_relations")
class ChannelUserRelations: BaseIdentity() {
    @ManyToOne
    @JoinColumn(name = "channel_user_id")
    var channelUser: ChannelUsers? = null

    @ManyToOne
    @JoinColumn(name = "joined_channel_id")
    var joinedChannel: Channels? = null

    @ManyToOne
    @JoinColumn(name = "waiting_channel_id")
    var waitingChannel: Channels? = null
}