package com.api.heys.entity

import java.io.Serializable
import javax.persistence.*
import javax.persistence.FetchType.*

@Entity
@Table(name = "channel_link")
class ChannelLink (
    detail: ChannelDetail,
    link: String,
) : BaseIdentity(), Serializable {
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "channel_detail_id", nullable = false)
    var channelDetail: ChannelDetail = detail

    @Column(columnDefinition = "TEXT", name = "link_url", nullable = false)
    var link: String = link
}