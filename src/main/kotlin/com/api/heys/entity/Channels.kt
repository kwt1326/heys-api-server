package com.api.heys.entity

import com.api.heys.constants.enums.ChannelType
import java.io.Serializable
import javax.persistence.*

/**
 * 채널 테이블
 */

@Entity
@Table(name = "channels")
class Channels(
    type: ChannelType,
    leader: Users,
): BaseIdentityDate(), Serializable {
    // 컨텐츠 내용 기반으로 채널 생성 (스터디는 채널만 생성 하도록 할것)
    @ManyToOne
    @JoinColumn(name = "content_id", nullable = true)
    var contents: Contents? = null

    // 리더 유저
    @ManyToOne
    @JoinColumn(name = "leader_user_id")
    var leader: Users = leader

    // 채널 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type", nullable = false)
    var type: ChannelType = type

    // 채널 알림 여부
    @Column(name = "active_notify")
    var activeNotify: Boolean = false

    // 채널 상세 정보 테이블
    @OneToOne(mappedBy = "channel", cascade = [CascadeType.ALL], orphanRemoval = true)
    var detail: ChannelDetail? = null

    // 채널 조회 테이블
    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var channelViews: MutableSet<ChannelView> = mutableSetOf()

    // 채널 북마크 테이블
    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var channelBookMarks: MutableSet<ChannelBookMark> = mutableSetOf()

    // 채널 합류 유저 조인 테이블
    @OneToMany(mappedBy = "channel", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var channelUserRelations: MutableSet<ChannelUserRelations> = mutableSetOf()
}