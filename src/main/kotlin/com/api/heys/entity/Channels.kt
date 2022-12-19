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
        leader: Users,
): BaseIdentityDate(), Serializable {
    // 컨텐츠 내용 기반으로 채널 생성 (스터디도 동일)
    @ManyToOne
    @JoinColumn(name = "content_id")
    var contents: Contents = content

    // 리더 유저
    @ManyToOne
    @JoinColumn(name = "leader_user_id")
    var leader: Users = leader

    // 채널 알림 여부
    @Column(name = "active_notify")
    var activeNotify: Boolean = false

    // 채널 조회 테이블
    @OneToOne(mappedBy = "channel", cascade = [CascadeType.ALL])
    var channelView: ChannelView? = null

    // 합류 채널 리스트 조인 테이블
    @OneToMany(mappedBy = "joinedChannel", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var joinedChannelRelationUsers: MutableSet<ChannelUserRelations> = mutableSetOf()

    // 대기 채널 리스트 조인 테이블
    @OneToMany(mappedBy = "waitingChannel", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var waitingChannelRelationUsers: MutableSet<ChannelUserRelations> = mutableSetOf()
}