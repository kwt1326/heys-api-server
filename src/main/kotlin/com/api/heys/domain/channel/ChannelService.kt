package com.api.heys.domain.channel

import com.api.heys.domain.channel.dto.CreateChannelData
import com.api.heys.entity.*
import com.api.heys.utils.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
class ChannelService(
        @Autowired private val channelsRepository: IChannelsRepository,
        @Autowired private val contentsRepository: IContentsRepository,
        @Autowired private val userRepository: IUserRepository,
        @Autowired private val jwtUtil: JwtUtil,
): IChannelService {
    @Transactional
    override fun createChannel(dto: CreateChannelData, token: String): Channels? {
        val content: Optional<Contents> = contentsRepository.findById(dto.contentId)
        val phone: String = jwtUtil.extractUsername(token)
        val user: Users? = userRepository.findByPhone(phone)

        if (user != null && content.isPresent) {
            val contentEntity = content.get()
            val newChannel = Channels(
                    content = contentEntity,
                    leader = user,
            )
            val newChannelView = ChannelView(newChannel)

            newChannel.channelView = newChannelView
            contentEntity.channels.add(newChannel)

            contentsRepository.save(contentEntity)

            return channelsRepository.save(newChannel)
        }
        return null
    }
}