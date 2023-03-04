package com.api.heys.channels

import com.api.heys.constants.DefaultString
import com.api.heys.constants.MessageString
import com.api.heys.constants.enums.*
import com.api.heys.domain.channel.ChannelService
import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.domain.channel.dto.GetChannelsParam
import com.api.heys.domain.content.ContentService
import com.api.heys.domain.content.dto.CreateExtraContentData
import com.api.heys.domain.user.dto.CommonSignUpData
import com.api.heys.domain.user.service.UserService
import com.api.heys.utils.JwtUtil
import io.mockk.InternalPlatformDsl.toStr
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.reflect.typeOf

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class ChannelServiceTest(
    @Autowired private val userService: UserService,
    @Autowired private val contentService: ContentService,
    @Autowired private val channelService: ChannelService,
    @Autowired private val jwtUtil: JwtUtil,
) {
    private val testUrl = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg"

    private val commonSignUpData = CommonSignUpData(
        phone = "01012341234",
        username = "TESTER",
        password = "12341234",
        age = 29,
        gender = Gender.Male,
        interests = mutableSetOf("교육", "자기계발"),
    )

    private val commonLeaderSignUpData = CommonSignUpData(
        phone = "01012345678",
        username = "LEADER",
        password = "12341234",
        age = 29,
        gender = Gender.Female,
        interests = mutableSetOf("연애"),
    )

    private val extraContentData = CreateExtraContentData(
        title = "러브러브챌린지",
        company = "네이버",
        target = "아무나 지원 가능",
        benefit = "500만원",
        contact = "test@naver.com",
        contentText = "테스트 상세정보 입니다~!~!~!~!!~",
        startDate = LocalDateTime.now().minusDays(5),
        endDate = LocalDateTime.now().plusDays(4),
        linkUri = "https://www.naver.com",
        previewImgUri = testUrl,
        thumbnailUri = testUrl,
        interests = mutableSetOf(
            "연애",
            "챌린지"
        )
    )

    private val contentChannelData = CreateChannelData(
        name = "러브러브 챌린지 참여하실 분 구해요~!",
        purpose = "실력향상",
        online = Online.Offline,
        location = "서울특별시",
        limitPeople = 5,
        lastRecruitDate = LocalDateTime.now().plusDays(5),
        recruitMethod = RecruitMethod.Approval,
        contentText = "러브러브 챌린지 참여 채널입니다~! 승인제로 운영하며, 준비된 사람만 커몽~",
        recruitText = "이상한 사람만 아니면 되요~! (신천지, 홍보 목적 절대 사양)",
        thumbnailUri = testUrl,
        linkUri = mutableSetOf(testUrl),
        interests = mutableSetOf(
            "연애",
            "챌린지"
        )
    )

    private val studyChannelData = CreateChannelData(
        name = "환승연애학 공부하실 분 구해요~!",
        purpose = "계기확립",
        online = Online.Online,
        location = "진주특별시",
        limitPeople = 5,
        lastRecruitDate = LocalDateTime.now().plusDays(5),
        recruitMethod = RecruitMethod.Approval,
        contentText = "환승연애학 공부 채널입니다~! 승인제로 운영하며, 준비된 사람만 커몽~",
        recruitText = "공부 목적인 사람만 오시면 되요",
        thumbnailUri = testUrl,
        linkUri = mutableSetOf(testUrl),
        interests = mutableSetOf(
            "연애",
            "챌린지"
        )
    )

    private val filterData = GetChannelsParam(
        interests = listOf("연애"),
        lastRecruitDate = LocalDateTime.now().plusDays(6).toStr(),
        purpose = "실력향상",
        online = Online.Offline,
        location = "서울특별시",
        includeClosed = false,
        page = 1,
        limit = 10
    )

    private val studyFilterData = GetChannelsParam(
        interests = listOf("챌린지"),
        lastRecruitDate = LocalDateTime.now().plusDays(6).toStr(),
        purpose = "계기확립",
        online = Online.Online,
        location = "진주특별시",
        includeClosed = false,
        page = 1,
        limit = 10
    )

    private var token: String = ""
    private var leaderToken: String = ""

    @Transactional
    private fun createContentAndChannelByLeader(): HashMap<String, Long> {
        val createResponse = contentService.createExtraContent(extraContentData, leaderToken)
        assertThat(createResponse.statusCode).isEqualTo(HttpStatus.OK)

        assertThat(createResponse.body!!.contentId).isNotNull
        val contentId = createResponse.body!!.contentId!!

        val createChannelResponse = channelService.createChannel(contentChannelData, leaderToken, contentId)
        assertThat(createChannelResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(createChannelResponse.body!!.channelId).isNotNull

        val map = HashMap<String, Long>()
        map["contentId"] = contentId
        map["channelId"] = createChannelResponse.body!!.channelId!!

        return map
    }

    @BeforeEach
    internal fun beforeEach() {
        // Create Common User
        token = userService.signUp(commonSignUpData, DefaultString.commonRole) ?: ""
        assertThat(token).isNotEqualTo("")

        leaderToken = userService.signUp(commonLeaderSignUpData, DefaultString.commonRole) ?: ""
        assertThat(leaderToken).isNotEqualTo("")
    }

    /** 외부(공모전, 사이드 프로젝트 등) 컨텐츠 생성 후 컨텐츠 기반 채널 생성 및 필터 테스트 */
    @Test
    @Order(1)
    fun extraContentCreateAndCreateChannelTest() {
        val resultMap = createContentAndChannelByLeader()
        channelService.increaseChannelView(resultMap["channelId"] as Long, token)

        val getChannelResponse = channelService.getChannels(ChannelType.Content, filterData, resultMap["contentId"])
        assertThat(getChannelResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(getChannelResponse.body!!.data.count()).isEqualTo(1)
        assertThat(getChannelResponse.body!!.data.first().viewCount).isEqualTo(1)
    }

    /** 스터디 채널 생성 및 필터 테스트 */
    @Test
    @Order(2)
    fun createStudyChannelTest() {
        val createChannelResponse = channelService.createChannel(studyChannelData, token)
        assertThat(createChannelResponse.statusCode).isEqualTo(HttpStatus.OK)

        val getChannelResponse = channelService.getChannels(ChannelType.Study, studyFilterData, null)
        assertThat(getChannelResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(getChannelResponse.body!!.data.count()).isEqualTo(1)
    }

    /** 리더가 채널 생성 -> 멤버가 가입 요청 -> 리더 승인 -> 멤버 탈퇴 과정 테스트 */
    @Test
    @Order(3)
    fun joinChannelApproveToMemberExitProcess() {
        val resultMap = createContentAndChannelByLeader()

        val joinResponse = channelService.joinChannel(resultMap["channelId"] as Long, token)
        assertThat(joinResponse.body?.message ?: "").isEqualTo(MessageString.SUCCESS_EN)

        val resultMap2 = channelService.getJoinAndWaitingChannelCounts(token)

        assertThat(resultMap2[DefaultString.joinChannelKey]).isEqualTo(0)
        assertThat(resultMap2[DefaultString.waitChannelKey]).isEqualTo(1)

        val user = userService.findByPhone(jwtUtil.extractUsername(token))
        assertThat(user).isNotNull

        val approveResponse = channelService.requestAllowReject(
            true, user!!.id, resultMap["channelId"] as Long, "승인합니다", leaderToken
        )

        assertThat(approveResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(approveResponse.body!!.message).isEqualTo(MessageString.SUCCESS_EN)

        val resultMap3 = channelService.getJoinAndWaitingChannelCounts(token)

        assertThat(resultMap3[DefaultString.joinChannelKey]).isEqualTo(1)
        assertThat(resultMap3[DefaultString.waitChannelKey]).isEqualTo(0)

        val followersResponse = channelService.getChannelFollowers(
            resultMap["channelId"] as Long, ChannelMemberStatus.Approved)

        assertThat(followersResponse.body!!.data.count()).isEqualTo(1)
        assertThat(followersResponse.body!!.data.first().username).isEqualTo("TESTER")

        val exitResponse = channelService.memberExitChannel("탈퇴합니다~!", resultMap["channelId"] as Long, token)

        assertThat(exitResponse.statusCode).isEqualTo(HttpStatus.OK)

        val resultMap4 = channelService.getJoinAndWaitingChannelCounts(token)

        assertThat(resultMap4[DefaultString.joinChannelKey]).isEqualTo(0)
        assertThat(resultMap4[DefaultString.waitChannelKey]).isEqualTo(0)
    }

    @Test
    @Order(4)
    fun getChannelDetailTest() {
        val resultMap = createContentAndChannelByLeader()
        val channelId = resultMap["channelId"] as Long
        val joinResponse = channelService.joinChannel(channelId, token)
        val detailResponse = channelService.getChannelDetail(channelId, token)
        assertThat(detailResponse.body!!.data).isNotNull

        val data = detailResponse.body!!.data!!
        assertThat(data.interests.count()).isEqualTo(2)
        assertThat(data.WaitingUserList.count()).isEqualTo(1)
        assertThat(data.links.count()).isEqualTo(1)
        assertThat(data.leader.username).isEqualTo("LEADER")
        assertThat(data.contentData).isNotNull
        assertThat(data.contentData!!.company).isEqualTo("네이버")
        assertThat(data.relationshipWithMe).isEqualTo(ChannelRelationship.Applicant)
    }

    @Test
    @Order(5)
    fun bookmarkTest() {
        val resultMap = createContentAndChannelByLeader()
//        val createChannelResponse = channelService.createChannel(studyChannelData, leaderToken)
//        val channelId = createChannelResponse.body!!.channelId!!

        val channelId = resultMap["channelId"] as Long
        val bookmarkAddResponse = channelService.addBookmark(channelId, token)
        assertThat(bookmarkAddResponse.statusCode).isEqualTo(HttpStatus.OK)

        channelService.increaseChannelView(channelId, token)

        var detailResponse = channelService.getChannelDetail(channelId, token)
        assertThat(detailResponse.body).isNotNull
        assertThat(detailResponse.body!!.data).isNotNull
        assertThat(detailResponse.body!!.data!!.isBookMarked).isEqualTo(true)

        val bookmarkRemoveResponse = channelService.removeBookmark(channelId, token)
        assertThat(bookmarkRemoveResponse.statusCode).isEqualTo(HttpStatus.OK)

        detailResponse = channelService.getChannelDetail(channelId, token)
        assertThat(detailResponse.body).isNotNull
        assertThat(detailResponse.body!!.data).isNotNull
        assertThat(detailResponse.body!!.data!!.isBookMarked).isEqualTo(false)
    }
}