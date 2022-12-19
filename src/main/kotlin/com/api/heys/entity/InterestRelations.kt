package com.api.heys.entity

import javax.persistence.*
import javax.persistence.FetchType.*

/**
 * 관심분야 Associate Table
 */

@Entity
@Table(name = "interest_relations")
class InterestRelations: BaseIdentity() {
    @ManyToOne(fetch = LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_detail_id")
    var userDetail: UserDetail? = null

    @ManyToOne(fetch = LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "content_detail_id")
    var contentDetail: ContentDetail? = null

    @ManyToOne(fetch = LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "channel_id")
    var channel: Channels? = null

    @ManyToOne(fetch = LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "interest_id")
    var interest: Interest? = null
}