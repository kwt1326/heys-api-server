package com.api.heys.domain.content

import com.api.heys.domain.content.dto.CreateContentData
import com.api.heys.entity.Contents

interface IContentService {
    fun createContent(dto: CreateContentData): Contents?
}