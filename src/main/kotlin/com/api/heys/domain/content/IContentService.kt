package com.api.heys.domain.content

import com.api.heys.constants.enums.ContentType
import com.api.heys.domain.content.dto.CreateContentData
import com.api.heys.domain.content.dto.EditContentData
import com.api.heys.domain.content.dto.GetContentDetailData
import com.api.heys.entity.Contents

interface IContentService {
    fun createContent(dto: CreateContentData): Contents?
    fun getContentDetail(type: ContentType, id: Long): GetContentDetailData?

    fun editContentDetail(dto: EditContentData): Boolean
}