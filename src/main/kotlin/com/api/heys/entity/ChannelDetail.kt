package com.api.heys.entity

import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 채널 상세 정보 테이블
 * */

@Entity
@Table(name = "channel_detail")
class ChannelDetail(
    channel: Channels,
    name: String,
    purpose: String,
    location: String,
    contentText: String,
    recruitText: String,
    online: Online,
    limitPeople: Int,
    lastRecruitDate: LocalDateTime,
    recruitMethod: RecruitMethod,
    thumbnailUri: String,
): Serializable {
    @Id
    @Column(name = "channel_id")
    var id: Long = 0

    @MapsId
    @OneToOne
    @JoinColumn(name = "channel_id")
    var channel: Channels = channel

    @OneToMany(mappedBy = "channelDetail", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var interestRelations: MutableSet<InterestRelations> = mutableSetOf()

    @OneToMany(mappedBy = "channelDetail", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var links: MutableSet<ChannelLink> = mutableSetOf()

    // 채널 이름
    @Column(name = "name", nullable = false)
    var name: String = name

    // 활동 목적
    @Column(name = "purpose", nullable = false)
    var purpose: String = purpose

    // 소개글
    @Column(columnDefinition = "TEXT", name = "content_text", nullable = false)
    var contentText: String = contentText

    // 이런분을 찾아요! 글
    @Column(columnDefinition = "TEXT", name = "recruit_text", nullable = false)
    var recruitText: String = recruitText

    // 활동 형태
    @Column(name = "online", nullable = false)
    var online: Online = online

    // 활동 지역 (online = '온라인' 일 경우 blank)
    @Column(name = "location", nullable = false)
    var location: String = location

    // 참여 제한 인원 수
    @Column(name = "limit_people")
    var limitPeople: Int = limitPeople

    // 모집 마감 날짜
    @Column(name = "last_recruit_date", nullable = false)
    var lastRecruitDate: LocalDateTime = lastRecruitDate

    // 모집 방식
    @Enumerated(EnumType.STRING)
    @Column(name = "recruit_method", nullable = false)
    var recruitMethod: RecruitMethod = recruitMethod

    // 썸네일 이미지 URI
    @Column(columnDefinition = "TEXT", name = "thumbnail_uri")
    var thumbnailUri: String = thumbnailUri
}