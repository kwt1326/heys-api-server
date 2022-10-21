package com.api.heys.entity

import javax.persistence.*

@Entity
@Table(name = "message_reaction_relations")
class MessageReactionRelations(): BaseIdentity() {
    @ManyToOne
    @JoinColumn(name = "reaction_id")
    var reaction: MessageReaction? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    var chat: Chat? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_notice_id")
    var chatNotice: ChatNotice? = null
}