package com.api.heys.domain.content

import com.api.heys.constants.DefaultString
import com.api.heys.constants.MessageString
import com.api.heys.constants.enums.ContentType
import com.api.heys.constants.enums.Online
import com.api.heys.domain.content.dto.*
import com.api.heys.domain.content.repository.IContentViewRepository
import com.api.heys.domain.content.repository.IContentsRepository
import com.api.heys.domain.interest.repository.InterestRepository
import com.api.heys.entity.*
import com.api.heys.helpers.findUserByToken
import com.api.heys.utils.CommonUtil
import com.api.heys.utils.JwtUtil
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ContentService(
    @Autowired private val contentRepository: IContentsRepository,
    @Autowired private val contentViewRepository: IContentViewRepository,
    @Autowired private val interestRepository: InterestRepository,
    @Autowired private val userRepository: IUserRepository,
    @Autowired private val commonUtil: CommonUtil,
    @Autowired private val jwtUtil: JwtUtil,
) : IContentService {
    private fun isOffline(online: Online): Boolean {
        return listOf(Online.Offline, Online.OnOffLine).contains(online)
    }

    /**
     * 컨텐츠 생성
     * Contents, ContentDetail 생성 및 Interest(관심분야 테이블) Associate Table 관계 설정 포함
     */
    @Transactional
    override fun createExtraContent(dto: CreateExtraContentData, token: String): ResponseEntity<String> {
        findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageString.INVALID_USER)

        val newContents = Contents(contentType = ContentType.Extra)
        val newContentView = ContentView(contents = newContents)
        val newExtraContentsDetail = ExtraContentDetail(
            contents = newContents,
            title = dto.title,
            target = dto.target,
            benefit = dto.benefit,
            company = dto.company,
            contact = dto.contact,
            contentText = dto.contentText,
            startDate = dto.startDate,
            endDate = dto.endDate,
            linkUrl = dto.linkUri ?: "",
            previewImgUri = dto.previewImgUri ?: DefaultString.defaultThumbnailUri,
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
            rel.extraDetail = newExtraContentsDetail
            newExtraContentsDetail.interestRelations.add(rel)
            interest.interestRelations.add(rel)
        }

        newContents.extraDetail = newExtraContentsDetail
        newContents.contentView = newContentView
        contentRepository.save(newContents)

        return ResponseEntity.ok().body("success")
    }

    @Transactional(readOnly = true)
    override fun getExtraContentDetail(id: Long): ResponseEntity<GetExtraContentDetailResponse> {
        val result = contentRepository.getExtraContent(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GetExtraContentDetailResponse(data = null, message = "Not found extra content"))

        val detail = result.extraDetail ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(GetExtraContentDetailResponse(data = null, message = "Not found extra content detail"))
        val view = result.contentView ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(GetExtraContentDetailResponse(data = null, message = "Not found extra content view"))
        val channels = result.channels
        val interests = detail.interestRelations
            .mapNotNull { it.interest }
            .map { it.name }

        return ResponseEntity.ok().body(
            GetExtraContentDetailResponse(
                data = GetExtraContentDetailData(
                    title = detail.title,
                    company = detail.company,
                    target = detail.target,
                    benefit = detail.benefit,
                    contentText = detail.contentText,
                    contact = detail.contact,
                    startDate = detail.startDate,
                    endDate = detail.endDate,
                    dDay = commonUtil.diffDay(detail.endDate, LocalDateTime.now()),
                    viewCount = view.count,
                    channelCount = channels.count().toLong(),
                    linkUrl = detail.linkUrl,
                    thumbnailUri = detail.thumbnailUri,
                    interests = interests,
                ),
                message = "success"
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getExtraContents(params: GetExtraContentsParam): ResponseEntity<GetExtraContentsResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(
            GetExtraContentsResponse(
                data = contentRepository.findExtraContents(params),
                message = "success"
            )
        )
    }

    @Transactional
    override fun putExtraContentDetail(id: Long, dto: PutExtraContentData): ResponseEntity<String> {
        val content = contentRepository.getExtraContent(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found extra content")

        val detail = content.extraDetail
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found extra content detail")

        if (dto.title != null) detail.title = dto.title
        if (dto.target != null) detail.target = dto.target
        if (dto.benefit != null) detail.benefit = dto.benefit
        if (dto.company != null) detail.company = dto.company
        if (dto.contact != null) detail.contact = dto.contact
        if (dto.contentText != null) detail.contentText = dto.contentText!!
        if (dto.startDate != null) detail.startDate = dto.startDate!!
        if (dto.endDate != null) detail.endDate = dto.endDate!!
        if (dto.linkUri != null) detail.linkUrl = dto.linkUri!!
        if (dto.previewImgUri != null) detail.previewImgUri = dto.previewImgUri!!
        if (dto.thumbnailUri != null) detail.thumbnailUri = dto.thumbnailUri!!

        if (dto.interests != null) {
            // 기존 관심분야 삭제
            detail.interestRelations.clear()
            dto.interests.map {
                // Create Interest Categories
                var interest: Interest? = interestRepository.findByName(it)
                if (interest == null) {
                    interest = Interest(name = it)
                }

                // InterestRelation Linking
                val rel = InterestRelations()
                rel.interest = interest
                rel.extraDetail = detail
                detail.interestRelations.add(rel)
                interest.interestRelations.add(rel)
            }
        }

        // TIP: save 호출하지 않아도 영속성 context 에서 엔티티를 저장한다.
        contentRepository.save(content)

        return ResponseEntity.ok().body("success - modified content extra detail")
    }

    @Transactional
    override fun increaseContentView(id: Long, token: String): ResponseEntity<String> {
        val contentView = contentRepository.getContentView(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found content")

        val phone: String = jwtUtil.extractUsername(token)
        val user = userRepository.findByPhone(phone)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found User")

        val viewer = contentView.viewers.find { it.id == user.id }

        if (viewer != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Already have seen content")
        }

        contentView.count += 1
        contentView.viewers.add(user)
        contentViewRepository.save(contentView)
        return ResponseEntity.status(HttpStatus.OK).body("New content view")
    }
}