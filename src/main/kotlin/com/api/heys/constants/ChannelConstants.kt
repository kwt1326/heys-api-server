package com.api.heys.constants

object ChannelConstants {
    // 채널 관심분야 범례 (usage: 채널 썸네일 분류)
    private val channelInterestLegend1 = listOf("공공/봉사", "기획/아이디어", "인문학/소설/웹툰", "학술/논문", "문화/교육", "과학/공학", "환경/식품")
    private val channelInterestLegend2 = listOf("미디어 콘텐츠/전시", "개발", "디자인", "미술/건축", "사진/영상/UCC", "게임")
    private val channelInterestLegend3 = listOf("외국/언어", "패션/라이프스타일", "해외/관광")
    private val channelInterestLegend4 = listOf("광고/마케팅", "데이터/인공지능", "IT/SW")
    private val channelInterestLegend5 = listOf("금융/경제", "경영/비즈니스")
    private val channelInterestLegend6 = listOf("스포츠/음악", "댄스/무용")

    // 채널 썸네일, 커버 이미지 그룹
    private val channelThumbnails1 = mapOf("list" to "/channel/thumbnail/category-mainGreen-preview.svg", "detail" to "/channel/thumbnail/category-mainGreen-cover.svg")
    private val channelThumbnails2 = mapOf("list" to "/channel/thumbnail/category-mainYellow-preview.svg", "detail" to "/channel/thumbnail/category-mainYellow-cover.svg")
    private val channelThumbnails3 = mapOf("list" to "/channel/thumbnail/category-mainBlue-preview.svg", "detail" to "/channel/thumbnail/category-mainBlue-cover.svg")
    private val channelThumbnails4 = mapOf("list" to "/channel/thumbnail/category-subGreen-preview.svg", "detail" to "/channel/thumbnail/category-subGreen-cover.svg")
    private val channelThumbnails5 = mapOf("list" to "/channel/thumbnail/category-mainPurple-preview.svg", "detail" to "/channel/thumbnail/category-mainPurple-cover.svg")
    private val channelThumbnails6 = mapOf("list" to "/channel/thumbnail/category-mainRed-preview.svg", "detail" to "/channel/thumbnail/category-mainRed-cover.svg")
    val channelThumbnailsDefault = mapOf("list" to "/channel/thumbnail/channel-default-cover.svg", "detail" to "/channel/thumbnail/channel-default-cover.svg")

    // 범례, 이미지 그룹 Mapper
    val channelThumbnailMapper = mapOf(
        channelInterestLegend1 to channelThumbnails1,
        channelInterestLegend2 to channelThumbnails2,
        channelInterestLegend3 to channelThumbnails3,
        channelInterestLegend4 to channelThumbnails4,
        channelInterestLegend5 to channelThumbnails5,
        channelInterestLegend6 to channelThumbnails6,
    )
}