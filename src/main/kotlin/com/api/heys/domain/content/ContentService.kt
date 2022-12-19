package com.api.heys.domain.content

import com.api.heys.constants.DefaultString
import com.api.heys.constants.enums.ContentType
import com.api.heys.constants.enums.Online
import com.api.heys.domain.content.dto.*
import com.api.heys.domain.interest.repository.InterestRepository
import com.api.heys.entity.*
import com.api.heys.utils.ChannelUtil
import com.api.heys.utils.CommonUtil
import com.api.heys.utils.JwtUtil
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ContentService(
        @Autowired private val contentRepository: IContentsRepository,
        @Autowired private val contentViewRepository: IContentViewRepository,
        @Autowired private val interestRepository: InterestRepository,
        @Autowired private val userRepository: IUserRepository,
        @Autowired private val channelUtil: ChannelUtil,
        @Autowired private val commonUtil: CommonUtil,
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
                online = dto.online,
                purpose = dto.purpose,
                company = dto.company ?: "",
                limitPeople = dto.limitPeople,
                contentText = dto.contentText,
                recruitMethod = dto.recruitMethod,
                lastRecruitDate = dto.lastRecruitDate,
                location = if (isOffline(dto.online)) dto.location ?: "" else "",
                thumbnailUri = dto.thumbnailUri ?: DefaultString.defaultThumbnailUri
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
    override fun getContentDetail(id: Long): GetContentDetailData? {
        val content = contentRepository.getContentDetail(id)
        if (content?.detail != null) {
            val detail: ContentDetail = content.detail!!
            val checkStudyType = (content.contentType == ContentType.Study) && content.channels.size > 0

            // Study 타입일 경우 채널이 항상 1개이므로(컨텐츠당 하나), channels list 의 첫번째 요소를 가져온다.
            // 이외의 타입일 경우 컨텐츠만 보여주고 채널을 생성하도록 유도하므로 channel count 만 내보낸다.
            val joinedUsers =
                    if (checkStudyType)
                        channelUtil.relationsToChannelUsersData(content.channels.first().joinedChannelRelationUsers)
                    else
                        listOf()

            val waitingUsers =
                    if (checkStudyType)
                        channelUtil.relationsToChannelUsersData(content.channels.first().waitingChannelRelationUsers)
                    else
                        listOf()

            return GetContentDetailData(
                    type = content.contentType,
                    dDay = commonUtil.calculateDday(detail.lastRecruitDate),
                    title = detail.name,
                    company = detail.company ?: "", // DB Sync
                    purpose = detail.purpose,
                    location = detail.location,
                    contentText = detail.contentText,
                    online = detail.online,
                    limitPeople = detail.limitPeople,
                    recruitMethod = detail.recruitMethod,
                    viewCount = content.contentView?.count ?: -1,
                    channelCount = content.channels.size,
                    thumbnailUri = detail.thumbnailUri ?: DefaultString.defaultThumbnailUri,
                    usersJoined = joinedUsers,
                    usersWaitingApprove = waitingUsers,
            )
        }
        return null
    }

    @Transactional(readOnly = true)
    override fun getContents(params: GetContentsParam): List<ContentListItemData> {
        return contentRepository.findContents(params).mapNotNull {
            it.detail?.let { detail ->
                ContentListItemData(
                        id = it.id,
                        name = detail.name,
                        company = detail.company,
                        viewCount = it.contentView?.count ?: 0,
                        channelCount = it.channels.size,
                        dDay = commonUtil.calculateDday(detail.lastRecruitDate),
                        thumbnailUri = detail.thumbnailUri ?: DefaultString.defaultThumbnailUri,
                )
            }
        }
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