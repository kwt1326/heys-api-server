package com.api.heys.domain.channel.dto

import com.api.heys.constants.enums.ChannelRelationship
import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "getChannelDetail 반환 데이터")
data class GetChannelDetailData(
    @field:Schema(description = "채널 id", example = "1")
    val id: Long,
    @field:Schema(
        description = "썸네일 이미지 URI",
        example = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg"
    )
    val thumbnailUri: String?,
    @field:Schema(description = "채널명", example = "환승연애학개론 스터디 채널 1")
    val title: String,
    @field:Schema(description = "참여 목적 (2개 이상일 경우 - ',' 로 join 시켜 하나의 string 으로 만들고 넣습니다)", example = "역량강화, 실력향상")
    val purpose: String,
    @field:Schema(description = "참여 형태", example = "Offline", implementation = Online::class)
    val online: Online,
    @field:Schema(description = "참여 지역 (online == '온라인' 일 경우 필요하지 않음)", example = "서울시")
    val location: String?,
    @field:Schema(description = "인원 제한", example = "50")
    val limitPeople: Int,
    @field:Schema(description = "모집 방식", example = "Approval", implementation = RecruitMethod::class)
    val recruitMethod: RecruitMethod,
    @field:Schema(description = "마감 날짜 (format : yyyy-MM-ddTHH:mm:ss)", example = "2022-11-07T14:00:00")
    val lastRecruitDate: LocalDateTime,
    @field:Schema(description = "관심분야", example = "[\"자기계발\", \"연애\"]")
    val interests: List<String> = listOf(),
    @field:Schema(description = "소개글", example = "단순 소개글 입니다~!")
    val contentText: String,
    @field:Schema(description = "이런분을 찾아요! 글", example = "이런분만 왔으면 좋겠어요 ㅎㅎ")
    val recruitText: String,
    @field:Schema(description = "연결 링크 URI 리스트", example = "[\"https://www.naver.com\", \"https://www.google.co.kr\"]")
    val links: List<GetChannelDetailLinkData> = listOf(),
    @field:Schema(description = "리더 정보", implementation = GetChannelDetailLeaderData::class)
    val leader: GetChannelDetailLeaderData,
    @field:ArraySchema(schema = Schema(implementation = GetChannelDetailUserData::class, description = "승인된 유저 리스트"))
    val ApprovedUserList: List<GetChannelDetailUserData>,
    @field:ArraySchema(schema = Schema(implementation = GetChannelDetailUserData::class, description = "대기 유저 리스트"))
    val WaitingUserList: List<GetChannelDetailUserData>,
    @field:Schema(description = "참여 중인 채널의 컨텐츠 정보(컨텐츠 기반 채널일 경우에만 해당)", implementation = GetChannelDetailContentData::class)
    val contentData: GetChannelDetailContentData? = null,
    @field:Schema(description = "채널과 나의 관계", implementation = ChannelRelationship::class)
    val relationshipWithMe: ChannelRelationship,
    @field:Schema(description = "채널 북마킹 여부", example = "true")
    val isBookMarked: Boolean,
)