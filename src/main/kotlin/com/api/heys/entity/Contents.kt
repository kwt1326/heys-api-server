package com.api.heys.entity

import com.api.heys.constants.enums.ContentType
import javax.persistence.*

/**
 * 컨텐츠 테이블 코어
 * 2023.02.19 기준 Extra (공모전, 대외활동) 타입만 존재
 * */

@Entity
@Table(name = "contents")
class Contents(
        contentType: ContentType,
): BaseIdentityDate() {
    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false)
    var contentType: ContentType = contentType

    // 공모전, 대외활동 컨텐츠 상세 정보
    // contentType == ContentType.Extra 일 경우에만 존재
    @OneToOne(mappedBy = "contents", cascade = [CascadeType.ALL])
    var extraDetail: ExtraContentDetail? = null

    // 컨텐츠 조회 테이블
    @OneToOne(mappedBy = "contents", cascade = [CascadeType.ALL])
    var contentView: ContentView? = null

    @OneToMany(mappedBy = "contents", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var channels: MutableSet<Channels> = mutableSetOf()
}