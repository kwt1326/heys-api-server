package com.api.heys.domain.content.repository

import com.api.heys.domain.content.dto.GetContentsParam
import com.api.heys.entity.Contents

interface ContentCustomRepository {
    fun findContents(params: GetContentsParam): List<Contents>
}