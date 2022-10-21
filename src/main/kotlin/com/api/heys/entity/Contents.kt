package com.api.heys.entity

import com.api.heys.constants.enums.ContentType
import java.io.Serializable
import javax.persistence.*

/**
 * 컨텐츠 테이블 코어
 * */

@Entity
@Table(name = "contents")
class Contents(
        contentType: ContentType,
): BaseIdentityDate() {
    @Enumerated(EnumType.STRING)
    @Column(name = "content_type", nullable = false, unique = true)
    var contentType: ContentType = contentType

    @OneToOne(mappedBy = "contents", cascade = [CascadeType.ALL])
    var detail: ContentDetail? = null

    @OneToMany(mappedBy = "contents")
    var channels: MutableSet<Channels> = mutableSetOf()
}