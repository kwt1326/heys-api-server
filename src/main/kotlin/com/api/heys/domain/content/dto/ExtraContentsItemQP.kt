package com.api.heys.domain.content.dto

import com.api.heys.constants.enums.ContentType
import com.api.heys.entity.Channels
import com.api.heys.entity.ContentView
import com.api.heys.entity.ExtraContentDetail
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class ExtraContentsItemQP @QueryProjection constructor(
    val id: Long,
    val extraDetail: ExtraContentDetail,
    val contentViews: MutableSet<ContentView>,
    val channels: MutableSet<Channels>,
    val publishedAt: LocalDateTime,
    val removedAt: LocalDateTime,
    val contentType: ContentType
)