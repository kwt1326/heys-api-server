package com.api.heys.entity

import java.io.Serializable
import javax.persistence.*
import javax.persistence.FetchType.*

@Entity
@Table(name = "user_profile_link")
class UserProfileLink (

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_detail_id", nullable = false)
    var userDetail: UserDetail,

    @Column(name = "link_url", nullable = false)
    var linkUrl: String

) : BaseIdentity(), Serializable