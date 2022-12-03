package com.api.heys.entity

import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 컨텐츠 상세 정보 테이블
 * */

@Entity
@Table(name = "content_detail")
class ContentDetail(
        contents: Contents,
        name: String,
        purpose: String,
        location: String,
        contentText: String,
        online: Online,
        limitPeople: Int,
        lastRecruitDate: LocalDateTime,
        recruitMethod: RecruitMethod,
        company: String,
        thumbnailUri: String,
): Serializable {
    @Id
    @Column(name = "contents_id")
    private var id: Long = 0

    @MapsId
    @OneToOne
    @JoinColumn(name = "contents_id")
    var contents: Contents = contents

    @OneToMany(mappedBy = "contentDetail", cascade = [CascadeType.PERSIST])
    var interestRelations: MutableSet<InterestRelations> = mutableSetOf()

    // 컨텐츠 이름 (타이틀)
    @Column(name = "name", nullable = false)
    var name: String = name

    // 주최/주관 이름, 스터디는 해당되지 않음
    @Column(name = "company", nullable = false)
    var company: String = company

    // 활동 목적
    @Column(name = "purpose", nullable = false)
    var purpose: String = purpose

    // 소개글
    @Column(columnDefinition = "TEXT", name = "content_text", nullable = false)
    var contentText: String = contentText

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

    // 썸네일 이미지 URI (dev 에서는 개별 cloudinary 미디어 저장소 사용, prod 배포는 AWS S3 사용 예정)
    @Column(columnDefinition = "TEXT", name = "thumbnail_uri")
    var thumbnailUri: String = thumbnailUri
}