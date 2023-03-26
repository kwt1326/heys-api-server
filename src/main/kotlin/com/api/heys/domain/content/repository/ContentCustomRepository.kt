package com.api.heys.domain.content.repository

import com.api.heys.domain.content.dto.ExtraContentListItemData
import com.api.heys.domain.content.dto.GetExtraContentDetailData
import com.api.heys.domain.content.dto.GetExtraContentsParam
import com.api.heys.domain.content.dto.GetExtraContentsResponse
import com.api.heys.entity.ContentBookMark
import com.api.heys.entity.ContentView
import com.api.heys.entity.Contents
import com.api.heys.entity.ExtraContentDetail

interface ContentCustomRepository {
    fun findExtraContents(params: GetExtraContentsParam): GetExtraContentsResponse

    fun getExtraContent(contentId: Long): Contents?

    fun getContentView(contentId: Long, userId: Long): ContentView?

    fun getContentBookMark(contentId: Long, userId: Long): ContentBookMark?
}