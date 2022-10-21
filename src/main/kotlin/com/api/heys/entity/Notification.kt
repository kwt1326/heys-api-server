package com.api.heys.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

/**
 * 알림 메시지 테이블
 */

@Entity
@Table(name = "notification")
class Notification(
        title: String,
        content: String,
        writer: Users
): BaseIdentityDate(), Serializable {
    @Column(name = "title")
    var title: String = title

    @Column(name = "content")
    var content: String = content

    @ManyToOne
    @JoinColumn(name = "user_id")
    var writer: Users = writer
}