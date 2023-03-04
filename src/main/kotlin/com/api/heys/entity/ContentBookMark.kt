package com.api.heys.entity

import javax.persistence.*

@Entity
@Table(name = "content_book_mark")
class ContentBookMark (
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