package com.api.heys.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.persistence.Table

/**
 * 채팅방 공지 메시지 테이블
 * 채팅방 테이블이 필요 할 것 같다. (채팅 방식에 따라 결정될 것)
 */

@Entity
@Table(name = "chat_notice")
class ChatNotice(
        title: String,
        content: String,
): BaseIdentityDate() {
    @Column(name = "title", nullable = false)
    var title: String = title

    @Column(name = "content", nullable = false)
    var content: String = content

    @OneToMany(mappedBy = "chatNotice", fetch = FetchType.LAZY)
    var msgRelations: MutableSet<MessageReactionRelations> = mutableSetOf()

    // @OneToOne
    // @JoinColumn(name = "chat_room_id")
    // var charRoom: ChatRoom
}