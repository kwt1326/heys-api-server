package com.api.heys.entity

import lombok.Getter
import javax.persistence.*
import javax.persistence.FetchType.LAZY
import javax.persistence.GenerationType.*

@Getter
@Entity
@Table(name = "device_token")
class DeviceToken(

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    val user : Users,

    @Column(length = 200, nullable = false)
    val token : String,

    @Column(length = 200)
    val arn : String?

) : BaseIdentityDate()