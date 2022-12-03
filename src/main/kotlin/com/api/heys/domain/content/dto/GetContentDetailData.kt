package com.api.heys.domain.content.dto

import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import com.api.heys.domain.channel.dto.ChannelUserData
import com.api.heys.entity.ChannelUsers
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "getContentDetail 반환 데이터")
data class GetContentDetailData (
        val viewCount: Long,
        val title: String,
        val company: String,
        val purpose: String,
        val location: String,
        val contentText: String,
        val online: Online,
        val limitPeople: Int,
        val dDay: Long,
        val recruitMethod: RecruitMethod,
        val channelCount: Int,
        val thumbnailUri: String,
        val usersJoined: List<ChannelUserData>,
        val usersWaitingApprove: List<ChannelUserData>,
)