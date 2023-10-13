package com.api.heys.entity

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

/**
 * 공모전 및 대외활동 컨텐츠 상세 정보 테이블
 * 컨텐츠 상세정보 공통 데이터
 * id, title, contents
 * */

@Entity
@Table(name = "extra_content_detail")
class ExtraContentDetail(
        contents: Contents,
        title: String,
        target: String,
        benefit: String,
        company: String,
        contact: String,
        contentText: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        linkUrl: String,
        previewImgUri: String,
        thumbnailUri: String,
): Serializable {
    @Id
    @Column(name = "contents_id")
    var id: Long = 0

    @MapsId
    @OneToOne
    @JoinColumn(name = "contents_id")
    var contents: Contents = contents

    // 컨텐츠 이름 (타이틀)
    @Column(name = "name", nullable = false)
    var title: String = title

    // 주최/주관 이름
    @Column(name = "company", nullable = false)
    var company: String = company

    // 참여 대상
    @Column(name = "target", nullable = false)
    var target: String = target

    // 보상
    @Column(name = "benefit", nullable = false)
    var benefit: String = benefit

    // 소개글
    @Column(columnDefinition = "TEXT", name = "content_text", nullable = false)
    var contentText: String = contentText

    // 모집 기간 시작일
    @Column(name = "start_date", nullable = false)
    var startDate: LocalDateTime = startDate

    // 모집 기간 종료일
    @Column(name = "end_date", nullable = false)
    var endDate: LocalDateTime = endDate

    // 연락처
    @Column(name = "contact", nullable = false)
    var contact: String = contact

    // 연결 링크 URI
    @Column(columnDefinition = "TEXT", name = "link_uri")
    var linkUrl: String = linkUrl

    // 프리뷰 이미지 URI (dev 에서는 개별 cloudinary 미디어 저장소 사용, prod 배포는 AWS S3 사용 예정)
    @Column(columnDefinition = "TEXT", name = "preview_img_uri")
    var previewImgUri: String = previewImgUri

    // 썸네일 이미지 URI (dev 에서는 개별 cloudinary 미디어 저장소 사용, prod 배포는 AWS S3 사용 예정)
    @Column(columnDefinition = "TEXT", name = "thumbnail_uri")
    var thumbnailUri: String = thumbnailUri

    // 관심분야 (태그)
    @OneToMany(mappedBy = "extraDetail", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    var interestRelations: MutableSet<InterestRelations> = mutableSetOf()
}