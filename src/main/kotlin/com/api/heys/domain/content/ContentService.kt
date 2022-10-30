package com.api.heys.domain.content

import com.api.heys.constants.enums.ContentType
import com.api.heys.constants.enums.Online
import com.api.heys.domain.content.dto.CreateContentData
import com.api.heys.domain.content.dto.EditContentData
import com.api.heys.domain.content.dto.GetContentDetailData
import com.api.heys.entity.*
import com.api.heys.utils.JwtUtil
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime

@Service
class ContentService(
        @Autowired private val contentRepository: IContentsRepository,
        @Autowired private val contentViewRepository: IContentViewRepository,
        @Autowired private val interestRepository: IInterestRepository,
        @Autowired private val userRepository: IUserRepository,
        @Autowired private val jwtUtil: JwtUtil,
): IContentService {
    private fun isOffline(online: Online): Boolean {
        return listOf(Online.Offline, Online.OnOffLine).contains(online)
    }

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
                location = if (isOffline(dto.online)) dto.location ?: "" else "",
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

    @Transactional(readOnly = true)
    override fun getContentDetail(type: ContentType, id: Long): GetContentDetailData? {
        val content = contentRepository.getContentDetail(id)
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

    @Transactional
    override fun putContentDetail(id: Long, dto: EditContentData): Boolean {
        val content = contentRepository.getContentDetail(id)
        if (content?.detail != null) {
            val detail = content.detail!!
            detail.contentText = dto.contentText
            detail.name = dto.name
            detail.online = dto.online
            detail.purpose = dto.purpose
            detail.limitPeople = dto.limitPeople
            detail.lastRecruitDate = dto.lastRecruitDate
            detail.recruitMethod = dto.recruitMethod

            if (isOffline(dto.online)) {
                detail.location = dto.location ?: ""
            }

            // TIP: save 호출하지 않아도 영속성 context 에서 엔티티를 저장한다.
            contentRepository.save(content)
            return true
        }
        return false
    }

    override fun increaseContentView(id: Long, token: String): Boolean {
        val contentView = contentViewRepository.getContentView(id)
        val phone: String = jwtUtil.extractUsername(token)
        val user: Users? = userRepository.findByPhone(phone)

        println(contentView.toString())

        if (contentView != null && user != null) {
            val foundUser = contentView.viewers.find { it.id == user.id }
            if (foundUser == null) {
                contentView.count += 1
                contentView.viewers.add(user)
                contentViewRepository.save(contentView)
                return true
            }
        }

        return false
    }
}