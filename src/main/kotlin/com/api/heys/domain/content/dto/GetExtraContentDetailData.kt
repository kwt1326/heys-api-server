package com.api.heys.domain.content.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "getExtraContentDetail 반환 데이터")
data class GetExtraContentDetailData(
    val title: String, // 제목
    val company: String, // 주최/주관
    val target: String, // 참가 자격
    val benefit: String, // 상금
    val contentText: String, // 상세내용
    val contact: String, // 문의처
    val startDate: LocalDateTime, // 모집 시작일
    val endDate: LocalDateTime, // 모집 마감일
    val dDay: Long, // 오늘로부터 마감일까지 남은 일 수
    val viewCount: Long, // 조회수
    val channelCount: Long, // 개설된 채널 수
    val linkUrl: String, // 연결 링크
    val thumbnailUri: String, // 썸네일 URL
    val interests: List<String>, // 관심분야 목록
    val isBookMarked: Boolean,
)
