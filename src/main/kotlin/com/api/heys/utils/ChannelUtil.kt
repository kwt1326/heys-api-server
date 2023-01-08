package com.api.heys.utils

import com.api.heys.constants.enums.Gender
import com.api.heys.domain.channel.dto.ChannelUserData
import com.api.heys.entity.ChannelUserRelations

open class ChannelUtil {
    /**
     * '채널 조인 테이블' 에서 '채널 유저 기본 정보' 만 추출합니다.
     * */
    open fun relationsToChannelUsersData(relations: List<ChannelUserRelations>): List<ChannelUserData> {
        return relations.map {
                val user = it.user
                val userDetail = user.detail
                ChannelUserData(
                        id = user.id,
                        gender = userDetail?.gender ?: Gender.NonBinary,
                        status = it.status,
                        exitMessage = it.exitMessage,
                        refuseMessage = it.refuseMessage
                )
        }
    }
}