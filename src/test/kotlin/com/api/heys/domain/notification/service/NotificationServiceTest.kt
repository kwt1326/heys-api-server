package com.api.heys.domain.notification.service

import com.api.heys.constants.DefaultString
import com.api.heys.constants.enums.Gender
import com.api.heys.constants.enums.MessageType
import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import com.api.heys.domain.channel.ChannelService
import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.domain.channel.repository.IChannelsRepository
import com.api.heys.domain.devicetoken.service.DeviceTokenService
import com.api.heys.domain.notification.vo.NotificationRequestVo
import com.api.heys.domain.user.dto.CommonSignUpData
import com.api.heys.domain.user.repository.UserRepository
import com.api.heys.domain.user.service.UserService
import com.api.heys.entity.Users
import com.api.heys.utils.JwtUtil
import com.api.heys.utils.UserUtil
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@SpringBootTest
@Rollback(true)
@Transactional(readOnly = true)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationServiceTest(
    @Autowired private val jwtUtil: JwtUtil,
    @Autowired private val userUtil: UserUtil,
    @Autowired private val userService : UserService,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val channelService: ChannelService,
    @Autowired private val deviceTokenService: DeviceTokenService,
    @Autowired private val channelsRepository: IChannelsRepository,
    @Autowired private val notificationService: NotificationService
){
    private var senderToken : String = ""
    private var receiverToken : String = ""
    private var channelId : Long = 1
    private val testUrl = "https://res.cloudinary.com/dyfuiigbw/image/upload/v1670047057/heys-dev/test1_jnkego.jpg"
    private val deviceToken = "d3BAg43eMTgKXuz9CgGkJtJ:APA91bHUjO9RqgE8EAfABbZQNSKxqWA2xORV9duXkkLSPYwmjCV2J3euyPeUYRSXMtuPOotxWnvhG8pH1uqyJNL6xXUnbco6nAKSGCf0mvTKEY3CJ1wCTlHu-cD6A5fYXCiQmfAOSGPS"

    val sendDer = CommonSignUpData(
        phone = "01012341234",
        username = "sender",
        password = "12341234",
        birthDate =  LocalDate.of(2002, 9, 10),
        gender = Gender.Male,
        interests = mutableSetOf("자기계발", "스터디")
    )

    val receiver = CommonSignUpData(
        phone = "01012341235",
        username = "receiver",
        password = "12341234",
        birthDate =  LocalDate.of(2002, 9, 10),
        gender = Gender.Male,
        interests = mutableSetOf("자기계발", "스터디")
    )

    private val studyChannelData = CreateChannelData(
        name = "환승연애학 공부하실 분 구해요~!",
        purposes = setOf("계기확립"),
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


    @BeforeAll
    fun signUp() {
        println("송신자 생성")
        senderToken = userService.signUp(sendDer, DefaultString.commonRole) ?: ""
        println("수신자 생성")
        receiverToken = userService.signUp(receiver, DefaultString.commonRole) ?: ""
        deviceTokenService.saveDeviceToken(receiverToken, deviceToken)
        println("채널 생성")
        channelId = channelService.createChannel(studyChannelData, receiverToken).body!!.channelId ?: 0
    }


    @Test
    @Transactional
    @DisplayName("알림 저장하기")
    @Throws(Exception::class)
    fun saveNotification() {
        // given
        val optionalChannel = channelsRepository.findById(channelId)
        val channel = optionalChannel.get()
        val user: Users? = userUtil.findUserByToken(senderToken, jwtUtil, userRepository)
        val notificationRequestVo = NotificationRequestVo(
            messageType = MessageType.CHANNEL_APPROVAL_REQUEST,
            sender = user,
            receiver = channel.leader,
            channel = channel
        )
        // when
        notificationService.saveNotification(notificationRequestVo)
        // then
    }
}