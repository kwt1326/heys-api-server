package com.api.heys.domain.content

import com.api.heys.domain.channel.dto.PutChannelRemoveRemarksData
import com.api.heys.domain.content.dto.*
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface IContentService {
    fun createExtraContent(dto: CreateExtraContentData, token: String): ResponseEntity<CreateContentResponse>
    fun putExtraContentDetail(id: Long, dto: PutExtraContentData): ResponseEntity<String>
    fun increaseContentView(id: Long, token: String): ResponseEntity<String>
    fun addBookmark(id: Long, token: String): ResponseEntity<String>
    fun removeBookmark(id: Long, token: String): ResponseEntity<String>
    fun removeBookmarks(params: PutContentRemoveRemarksData, token: String): ResponseEntity<String>
    fun getExtraContentDetail(id: Long, token: String): ResponseEntity<GetExtraContentDetailResponse>
    fun getExtraContents(params: GetExtraContentsParam): ResponseEntity<GetExtraContentsResponse>

    fun createExtraContentFromExcel(file: MultipartFile, token: String): ResponseEntity<String>
}