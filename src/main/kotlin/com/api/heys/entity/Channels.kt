package com.api.heys.entity

import org.hibernate.annotations.Fetch
import java.io.Serializable
import javax.persistence.*

/**
 * 채널 테이블
 */

@Entity
@Table(name = "channels")
class Channels(
        content: Contents,
        user: Users,
        channelView: ChannelView,
): BaseIdentityDate(), Serializable {
    // 컨텐츠 내용 기반으로 채널 생성 (스터디도 동일)
    @ManyToOne
    @JoinColumn(name = "content_id")
    var contents: Contents = content

    // 리더 유저
    @ManyToOne
    @JoinColumn(name = "leader_user_id")
    var leader: Users = user

    // 채널 조회 테이블
    @OneToOne(mappedBy = "channel")
    var channelView: ChannelView = channelView

    // 합류 채널 리스트 조인 테이블
    @OneToMany(mappedBy = "joinedChannel", fetch = FetchType.LAZY)
    var joinedChannelRelationUsers: MutableSet<ChannelUserRelations> = mutableSetOf()

    // 대기 채널 리스트 조인 테이블
    @OneToMany(mappedBy = "waitingChannel", fetch = FetchType.LAZY)
    var waitingChannelRelationUsers: MutableSet<ChannelUserRelations> = mutableSetOf()
}