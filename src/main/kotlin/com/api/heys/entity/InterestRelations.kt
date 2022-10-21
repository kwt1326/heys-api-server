package com.api.heys.entity

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * 관심분야 Associate Table
 */

@Entity
@Table(name = "interest_relations")
class InterestRelations: BaseIdentity() {
    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_detail_id")
    var userDetail: UserDetail? = null

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "content_detail_id")
    var contentDetail: ContentDetail? = null

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "channel_id")
    var channel: Channels? = null

    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "interest_id")
    var interest: Interest? = null
}