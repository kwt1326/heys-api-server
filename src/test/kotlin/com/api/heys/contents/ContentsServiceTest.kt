package com.api.heys.contents

import com.api.heys.domain.content.ContentService
import com.api.heys.domain.content.dto.CreateExtraContentData
import java.time.LocalDateTime

import com.api.heys.constants.DefaultString
import com.api.heys.constants.enums.ContentType
import com.api.heys.constants.enums.Gender
import com.api.heys.domain.content.dto.GetExtraContentsParam
import com.api.heys.domain.content.dto.PutContentRemoveRemarksData
import com.api.heys.domain.content.dto.PutExtraContentData
import com.api.heys.domain.user.dto.AdminSignUpData
import com.api.heys.domain.user.dto.CommonSignUpData
import com.api.heys.domain.user.service.UserService
import io.mockk.InternalPlatformDsl.toStr

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.springframework.test.context.ActiveProfiles
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@Transactional
internal class ContentsServiceTest(
    @Autowired private val userService: UserService,
    @Autowired private val contentService: ContentService,
) {
    private val commonSignUpData = CommonSignUpData(
        phone = "01012341234",
        username = "TESTER",
        password = "12341234",
        birthDate = LocalDate.of(1995, 10, 9),
        gender = Gender.Male,
        interests = mutableSetOf("교육", "자기계발"),
    )
    private val adminSignUpData = AdminSignUpData(
        phone = commonSignUpData.phone,
        username = commonSignUpData.username,
        password = commonSignUpData.password,
        birthDate = commonSignUpData.birthDate,
    )

    private val extraContentData = CreateExtraContentData(
        type = ContentType.Extracurricular,
        title = "러브러브챌린지",
        company = "네이버",
        target = "아무나 지원 가능",
        benefit = "500만원",
        contact = "test@naver.com",
        contentText = "테스트 상세정보 입니다~!~!~!~!!~",
        startDate = LocalDateTime.now().minusDays(5),
        endDate = LocalDateTime.now().plusDays(4),
        linkUri = "https://www.naver.com",
        previewImgUri = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg",
        thumbnailUri = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg",
        interests = mutableSetOf(
            "연애",
            "챌린지"
        )
    )

    private val extraContentData2 = extraContentData.copy(title = "러브러브챌린지2")

    private val extraContentModifyData = PutExtraContentData(
        title = "러브러브챌린지2",
        company = "네이버2",
        target = "100세미만",
        benefit = "500만원",
        contact = "test@google.com",
        contentText = "테스뚜",
        startDate = null,
        endDate = LocalDateTime.now().plusDays(3),
        linkUri = null,
        previewImgUri = null,
        thumbnailUri = "https://test.naver.com",
        interests = mutableSetOf("와인")
    )

    private final val extraContentsFilterParam = GetExtraContentsParam(
        type = ContentType.Extracurricular,
        interests = listOf("연애", "챌린지2"),
        lastRecruitDate = LocalDateTime.now().plusDays(5).toStr(), // format: "2023-02-21T08:53:17"
        includeClosed = false,
        page = 1,
        limit = 10,
    )

    private var token: String = ""
    private var adminToken: String = ""

    @BeforeEach
    internal fun beforeEach() {
        // Create Common User
        val userResponse = userService.signUp(commonSignUpData, DefaultString.commonRole)
        token = userResponse.body!!.token
        assertThat(token).isNotEqualTo("")
        // Add Admin Role
        val adminResponse = userService.signUp(adminSignUpData, DefaultString.adminRole)
        adminToken = adminResponse.body!!.token
        assertThat(adminToken).isNotEqualTo("")
    }

    /** 외부(공모전, 사이드 프로젝트 등) 컨텐츠 생성 후 필터링 테스트 */
    @Test
    @Order(1)
    fun extraContentCreateAndFilterTest() {
        val createResponse = contentService.createExtraContent(extraContentData, token)

        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createResponse.body!!.contentId).isNotNull

        val contentId = createResponse.body!!.contentId!!
        val publishResponse = contentService.putTogglePublishStateContent(contentId, adminToken)

        assertThat(publishResponse.statusCode).isEqualTo(HttpStatus.OK)

        val listResponse = contentService.getExtraContents(extraContentsFilterParam)

        assertThat(listResponse.body).isNotNull
        assertThat(listResponse.body!!.data.count()).isEqualTo(0)
        assertThat(listResponse.body!!.totalPage).isEqualTo(0)

        contentService.putTogglePublishStateContent(contentId, adminToken)

        val listResponse2 = contentService.getExtraContents(extraContentsFilterParam)

        assertThat(listResponse2.body!!.data.count()).isEqualTo(0)
        assertThat(listResponse2.body!!.totalPage).isEqualTo(0)
    }

    /** 외부 컨텐츠 생성 후 상세정보 가져오기 테스트 */
    @Test
    @Order(2)
    fun extraContentCreateAndGetDetailTest() {
        val createResponse = contentService.createExtraContent(extraContentData, token)

        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createResponse.body!!.contentId).isNotNull

        val contentId = createResponse.body!!.contentId!!
        val detailResponse = contentService.getExtraContentDetail(contentId, token)

        assertThat(detailResponse.body).isNotNull
        assertThat(detailResponse.body!!.data).isNotNull
        assertThat(detailResponse.body!!.data!!.interests.count()).isEqualTo(2)
    }

    /** 외부 컨텐츠 생성 후 상세정보 수정작업 테스트 */
    @Test
    @Order(3)
    fun putExtraContentDetailTest() {
        val createResponse = contentService.createExtraContent(extraContentData, token)

        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)

        val contentId = createResponse.body!!.contentId!!

        val putResponse = contentService.putExtraContentDetail(contentId, extraContentModifyData)

        assertThat(putResponse.statusCode).isEqualTo(HttpStatus.OK)

        val detailResponse = contentService.getExtraContentDetail(contentId, token)

        assertThat(detailResponse.body).isNotNull
        assertThat(detailResponse.body!!.data).isNotNull

        val data = detailResponse.body!!.data!!

        assertThat(data.interests.count()).isEqualTo(1)
        assertThat(data.title).isEqualTo(extraContentModifyData.title)
        assertThat(data.benefit).isEqualTo(extraContentModifyData.benefit)
        assertThat(data.target).isEqualTo(extraContentModifyData.target)
        assertThat(data.contact).isEqualTo(extraContentModifyData.contact)
        assertThat(data.endDate).isEqualTo(extraContentModifyData.endDate)
        assertThat(data.company).isEqualTo(extraContentModifyData.company)
        assertThat(data.thumbnailUri).isEqualTo(extraContentModifyData.thumbnailUri)
    }

    @Test
    @Order(4)
    fun bookmarkTest() {
        val createResponse = contentService.createExtraContent(extraContentData, token)
        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createResponse.body!!.contentId).isNotNull

        val contentId = createResponse.body!!.contentId!!
        val bookmarkAddResponse = contentService.addBookmark(contentId, token)
        assertThat(bookmarkAddResponse.statusCode).isEqualTo(HttpStatus.OK)

        var detailResponse = contentService.getExtraContentDetail(contentId, token)
        assertThat(detailResponse.body).isNotNull
        assertThat(detailResponse.body!!.data).isNotNull
        assertThat(detailResponse.body!!.data!!.isBookMarked).isEqualTo(true)

        val bookmarkRemoveResponse = contentService.removeBookmark(contentId, token)
        assertThat(bookmarkRemoveResponse.statusCode).isEqualTo(HttpStatus.OK)

        detailResponse = contentService.getExtraContentDetail(contentId, token)
        assertThat(detailResponse.body).isNotNull
        assertThat(detailResponse.body!!.data).isNotNull
        assertThat(detailResponse.body!!.data!!.isBookMarked).isEqualTo(false)
    }

    @Test
    @Order(5)
    fun removeAllBookmarksTest() {
        val createResponse = contentService.createExtraContent(extraContentData, token)
        val createResponse2 = contentService.createExtraContent(extraContentData2, token)
        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createResponse.body!!.contentId).isNotNull
        assertThat(createResponse2.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createResponse2.body!!.contentId).isNotNull

        val contentId = createResponse.body!!.contentId!!
        val contentId2 = createResponse2.body!!.contentId!!

        contentService.addBookmark(contentId, token)
        contentService.addBookmark(contentId2, token)
        val removeAllResponse =
            contentService.removeBookmarks(PutContentRemoveRemarksData(setOf(contentId, contentId2)), token)

        assertThat(removeAllResponse.body!!.message).isEqualTo("Removed content bookmarks num : 2")

        // 하나라도 북마크 되어 있으면 실패
        var detailResponse = contentService.getExtraContentDetail(contentId, token)
        assertThat(detailResponse.body).isNotNull
        assertThat(detailResponse.body!!.data).isNotNull
        assertThat(detailResponse.body!!.data!!.isBookMarked).isEqualTo(false)

        detailResponse = contentService.getExtraContentDetail(contentId2, token)
        assertThat(detailResponse.body).isNotNull
        assertThat(detailResponse.body!!.data).isNotNull
        assertThat(detailResponse.body!!.data!!.isBookMarked).isEqualTo(false)
    }
}