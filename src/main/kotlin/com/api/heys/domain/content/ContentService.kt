package com.api.heys.domain.content

import com.api.heys.constants.enums.ContentType
import com.api.heys.domain.content.dto.CreateContentData
import com.api.heys.domain.content.dto.GetContentDetailData
import com.api.heys.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
class ContentService(
        @Autowired private val contentRepository: IContentsRepository,
        @Autowired private val interestRepository: IInterestRepository,
): IContentService {
    /**
     * 컨텐츠 생성
     * Contents, ContentDetail 생성 및 Interest(관심분야 테이블) Associate Table 관계 설정 포함
     */
    @Transactional
    override fun createContent(dto: CreateContentData): Contents? {
        val newContents = Contents(contentType = dto.contentType)
        val newContentView = ContentView(contents = newContents)
        val newContentsDetail = ContentDetail(
                contents = newContents,
                name = dto.name,
                purpose = dto.purpose,
                contentText = dto.contentText,
                online = dto.online,
                location = dto.location ?: "",
                limitPeople = dto.limitPeople,
                lastRecruitDate = dto.lastRecruitDate,
                recruitMethod = dto.recruitMethod,
        )

        dto.interests.map {
            // Create Interest Categories
            var interest: Interest? = interestRepository.findByName(it)
            if (interest == null) {
                interest = Interest(name = it)
            }

            // InterestRelation Linking
            val rel = InterestRelations()
            rel.interest = interest
            rel.contentDetail = newContentsDetail
            newContentsDetail.interestRelations.add(rel)
            interest.interestRelations.add(rel)
        }

        newContents.contentView = newContentView

        return contentRepository.save(newContents)
    }

    @Transactional
    override fun getContentDetail(type: ContentType, id: Long): GetContentDetailData? {
        val content = contentRepository.getContentDetail(type, id)
        if (content?.detail != null) {
            val detail: ContentDetail = content.detail!!

            // Study 타입일 경우 채널이 항상 1개이므로(컨텐츠당 하나), channels list 의 첫번째 요소를 가져온다.
            // 이외의 타입일 경우 컨텐츠만 보여주고 채널을 생성하도록 유도하므로 channel count 만 내보낸다.
            val joinedUsers =
                    if (type == ContentType.Study)
                        if (content.channels.size > 0) content.channels.first().joinedChannelRelationUsers
                                .filter { it.channelUser != null }
                                .map { it.channelUser!! }.toList()
                        else listOf()
                    else listOf()

            val waitingUsers =
                    if (type == ContentType.Study)
                        if (content.channels.size > 0) content.channels.first().waitingChannelRelationUsers
                                .filter { it.channelUser != null }
                                .map { it.channelUser!! }.toList()
                        else listOf()
                    else listOf()

            val duration = Duration.between(LocalDateTime.now(), detail.lastRecruitDate)
            val dDay: Long = duration.toDays()

            return GetContentDetailData(
                    dDay = dDay,
                    title = detail.name,
                    purpose = detail.purpose,
                    location = detail.location,
                    contentText = detail.contentText,
                    online = detail.online,
                    limitPeople = detail.limitPeople,
                    recruitMethod = detail.recruitMethod,
                    viewCount = content.contentView?.count ?: -1,
                    channelCount = content.channels.size,
                    usersJoined = joinedUsers,
                    usersWaitingApprove = waitingUsers,
            )
        }
        return null
    }
}