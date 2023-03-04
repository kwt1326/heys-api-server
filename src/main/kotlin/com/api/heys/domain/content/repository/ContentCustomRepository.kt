package com.api.heys.domain.content.repository

import com.api.heys.domain.content.dto.ExtraContentListItemData
import com.api.heys.domain.content.dto.GetExtraContentDetailData
import com.api.heys.domain.content.dto.GetExtraContentsParam
import com.api.heys.entity.ContentView
import com.api.heys.entity.Contents
import com.api.heys.entity.ExtraContentDetail

interface ContentCustomRepository {
    fun findExtraContents(params: GetExtraContentsParam): List<ExtraContentListItemData>

    fun getExtraContent(contentId: Long): Contents?

    fun getContentView(contentId: Long, userId: Long): ContentView?
}