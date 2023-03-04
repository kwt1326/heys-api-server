package com.api.heys.entity

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

/**
 * 컨텐츠 조회 테이블
 */

@Entity
@DynamicUpdate
@Table(name = "content_view")
class ContentView(
    contents: Contents,
    users: Users,
) : BaseIdentity() {
    @ManyToOne
    @JoinColumn(name = "content_id")
    var content: Contents = contents

    @ManyToOne
    @JoinColumn(name = "user_id")
    var users: Users = users
}