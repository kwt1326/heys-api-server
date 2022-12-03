package com.api.heys.utils

import com.api.heys.constants.enums.Gender
import com.api.heys.domain.channel.dto.ChannelUserData
import com.api.heys.entity.ChannelUserRelations

open class ChannelUtil {
    /**
     * '채널 조인 테이블' 에서 '채널 유저 기본 정보' 만 추출합니다.
     * */
    open fun relationsToChannelUsersData(relations: MutableSet<ChannelUserRelations>): List<ChannelUserData> {
        return relations.mapNotNull {
            if (it.channelUser != null) {
                val channelUser = it.channelUser!!
                val userDetail = channelUser.user.detail
                ChannelUserData(
                        id = channelUser.id,
                        gender = userDetail?.gender ?: Gender.NonBinary,
                        status = channelUser.status,
                        exitMessage = channelUser.exitMessage,
                        refuseMessage = channelUser.refuseMessage
                )
            } else null
        }
    }
}