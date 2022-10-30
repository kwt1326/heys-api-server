package com.api.heys.domain.content.dto

import com.api.heys.constants.enums.Online
import com.api.heys.constants.enums.RecruitMethod
import com.api.heys.entity.ChannelUsers

data class GetContentDetailData (
        val viewCount: Long,
        val title: String,
        val purpose: String,
        val location: String,
        val contentText: String,
        val online: Online,
        val limitPeople: Int,
        val dDay: Long,
        val recruitMethod: RecruitMethod,
        val channelCount: Int,
        val usersJoined: List<ChannelUsers>,
        val usersWaitingApprove: List<ChannelUsers>,
)