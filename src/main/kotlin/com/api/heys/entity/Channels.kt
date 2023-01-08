package com.api.heys.entity

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
    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var channelUserRelations: MutableSet<ChannelUserRelations> = mutableSetOf()
}