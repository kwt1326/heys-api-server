package com.api.heys.entity

import com.api.heys.constants.enums.Reaction
import javax.persistence.*

/**
 * 리액션 테이블
 * 리액션은 공지의 리액션일 수 있고 채팅의 리액션일 수 있다.
 * MessageReactionRelations 관계 설정 필수
 */

@Entity
@Table(name = "message_reaction")
class MessageReaction(
        reaction: Reaction,
        reactor: Users,
): BaseIdentityDate() {
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "reaction")
    var reaction: Reaction = reaction

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var reactor: Users = reactor

    @OneToMany(mappedBy = "reaction", fetch = FetchType.LAZY)
    var msgRelations: MutableSet<MessageReactionRelations> = mutableSetOf()
}