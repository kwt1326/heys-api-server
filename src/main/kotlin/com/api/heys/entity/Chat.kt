package com.api.heys.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

/**
 * deprecated
 *
 * 채팅 메시지 객체 테이블
 * !!! 채팅 방식이 확정되지 않았으므로, 현재 테이블의 구조는 변동이 클수 있음 !!!
 */

@Entity
@Table(name = "chat")
class Chat(
        message: String,
        channel: Channels,
        writer: Users,
): BaseIdentityDate(), Serializable {
    @Column(name = "message", nullable = false)
    var message: String = message

    @ManyToOne
    @JoinColumn(name = "channel_id")
    var channel: Channels = channel

    @OneToOne
    @JoinColumn(name = "user_id")
    var writer: Users = writer

    @OneToMany(mappedBy = "chat")
    var msgRelations: MutableSet<MessageReactionRelations> = mutableSetOf()
}