package com.api.heys.domain.content

import com.api.heys.constants.DefaultString
import com.api.heys.constants.MessageString
import com.api.heys.domain.content.dto.*
import com.api.heys.domain.content.repository.IContentsRepository
import com.api.heys.domain.interest.repository.InterestRelationRepository
import com.api.heys.domain.interest.repository.InterestRepository
import com.api.heys.domain.user.repository.UserRepository
import com.api.heys.entity.*
import com.api.heys.helpers.SpreadSheetManager
import com.api.heys.helpers.findUserByToken
import com.api.heys.utils.CommonUtil
import com.api.heys.utils.JwtUtil
import org.springframework.transaction.annotation.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ContentService(
    @Autowired private val contentRepository: IContentsRepository,
    @Autowired private val interestRepository: InterestRepository,
    @Autowired private val interestRelationRepository: InterestRelationRepository,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val commonUtil: CommonUtil,
    @Autowired private val jwtUtil: JwtUtil,
) : IContentService {
    @Transactional
    private fun pureCreateExtraContent(dto: CreateExtraContentData): Contents {
        val newContents = Contents(contentType = dto.type)
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

        return contentRepository.save(newContents)
    }

    /**
     * 컨텐츠 생성
     * Contents, ContentDetail 생성 및 Interest(관심분야 테이블) Associate Table 관계 설정 포함
     */
    @Transactional
    override fun createExtraContent(dto: CreateExtraContentData, token: String): ResponseEntity<CreateContentResponse> {
        val response = CreateContentResponse(contentId = null, message = MessageString.SUCCESS_EN)
        val user = findUserByToken(token, jwtUtil, userRepository)

        if (user == null) {
            response.message = MessageString.INVALID_USER
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }

        response.contentId = pureCreateExtraContent(dto).id

        return ResponseEntity.ok().body(response)
    }

    @Transactional
    override fun createExtraContentFromExcel(file: MultipartFile, token: String): ResponseEntity<String> {
        val user = findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found user")

        if (user.authentications.find { it.role == DefaultString.adminRole } == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only Using Admin User")
        }

        SpreadSheetManager(file).excelDataConvertToExtraContentDTOs().forEach { pureCreateExtraContent(it) }
        return ResponseEntity.ok("success")
    }

    @Transactional(readOnly = true)
    override fun getExtraContentDetail(id: Long, token: String): ResponseEntity<GetExtraContentDetailResponse> {
        val result = contentRepository.getExtraContent(id)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(GetExtraContentDetailResponse(data = null, message = "Not found extra content"))

        val user = findUserByToken(token, jwtUtil, userRepository) ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(GetExtraContentDetailResponse(data = null, message = "Not found user"))
        val detail = result.extraDetail ?: return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(GetExtraContentDetailResponse(data = null, message = "Not found extra content detail"))
        val views = result.contentViews
        val bookmarks = result.contentBookMarks
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
                    dDay = commonUtil.calculateDday(detail.endDate),
                    viewCount = views.count().toLong(),
                    channelCount = channels.count().toLong(),
                    linkUrl = detail.linkUrl,
                    thumbnailUri = detail.thumbnailUri,
                    interests = interests,
                    isBookMarked = bookmarks.find { it.users!!.id == user.id } != null
                ),
                message = MessageString.SUCCESS_EN
            )
        )
    }

    @Transactional(readOnly = true)
    override fun getExtraContents(params: GetExtraContentsParam): ResponseEntity<GetExtraContentsResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(
            GetExtraContentsResponse(
                data = contentRepository.findExtraContents(params),
                message = MessageString.SUCCESS_EN
            )
        )
    }

    @Transactional
    override fun putExtraContentDetail(id: Long, dto: PutExtraContentData): ResponseEntity<String> {
        val content = contentRepository.findById(id)

        if (!content.isPresent)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found extra content")

        val contentEntity = content.get()
        val detail = contentEntity.extraDetail
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
            detail.interestRelations.clear()
            dto.interests.map {
                // Create Interest Categories
                var interest: Interest? = interestRepository.findByName(it)
                if (interest == null) {
                    interest = Interest(name = it)
                }

                // InterestRelation Linking
                val rel = interestRelationRepository.findByExtraContentDetailIdAndInterestId(detail.id, interest.id)
                    ?: InterestRelations()
                rel.interest = interest
                rel.extraDetail = detail
                detail.interestRelations.add(rel)
                interest.interestRelations.add(rel)
            }
        }

        contentRepository.save(contentEntity)

        return ResponseEntity.ok().body("success - modified content extra detail")
    }

    @Transactional
    override fun increaseContentView(id: Long, token: String): ResponseEntity<String> {
        val content = contentRepository.findById(id)
        if (!content.isPresent)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found content")

        val user = findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found User")

        val contentEntity = content.get()

        val isExisted = contentRepository.getContentView(id, user.id)
        if (isExisted != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Already have seen content")
        }

        contentEntity.contentViews.add(ContentView(contentEntity, user))
        contentRepository.save(contentEntity)

        return ResponseEntity.status(HttpStatus.OK).body("New content view")
    }

    @Transactional
    override fun addBookmark(id: Long, token: String): ResponseEntity<String> {
        val user = findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found User")

        val content = contentRepository.findById(id)
        if (!content.isPresent) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found Content")
        }

        val isExisted = contentRepository.getContentBookMark(id, user.id)
        if (isExisted != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Already added content bookmark")
        }

        val contentEntity = content.get()

        contentEntity.contentBookMarks.add(ContentBookMark(contentEntity, user))
        contentRepository.save(contentEntity)

        return ResponseEntity.status(HttpStatus.OK).body("New content bookmarked! : ${contentEntity.id}")
    }

    @Transactional
    override fun removeBookmark(id: Long, token: String): ResponseEntity<String> {
        val user = findUserByToken(token, jwtUtil, userRepository)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found User")

        val content = contentRepository.findById(id)
        if (!content.isPresent) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found Content")
        }

        val contentEntity = content.get()

        val bookmark = contentEntity.contentBookMarks.find { it.users!!.id == user.id }
        if (bookmark != null) contentEntity.contentBookMarks.removeIf { it.id == bookmark.id }

        contentRepository.save(contentEntity)

        return ResponseEntity.status(HttpStatus.OK).body("Removed content bookmark : ${contentEntity.id}")
    }
}