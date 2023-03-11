package com.api.heys.entity

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "channel_purpose")
class ChannelPurpose (
    detail: ChannelDetail,
    purpose: String,
) : BaseIdentity(), Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_detail_id", nullable = false)
    var channelDetail: ChannelDetail = detail

    @Column(name = "purpose", nullable = false)
    var purpose: String = purpose
}