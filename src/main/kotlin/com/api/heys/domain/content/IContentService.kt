package com.api.heys.domain.content

import com.api.heys.constants.enums.ContentType
import com.api.heys.domain.content.dto.*
import com.api.heys.entity.Contents

interface IContentService {
    fun createContent(dto: CreateContentData): Contents?
    fun getContentDetail(id: Long): GetContentDetailData?
    fun getContents(params: GetContentsParam): List<ContentListItemData>
    fun putContentDetail(id: Long, dto: EditContentData): Boolean
    fun increaseContentView(id: Long, token: String): Boolean
}