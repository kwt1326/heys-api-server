package com.api.heys.domain.content

import com.api.heys.domain.content.dto.CreateContentData
import com.api.heys.entity.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class ContentService(
        @Autowired private val contentRepository: IContentsRepository,
        @Autowired private val interestRepository: IInterestRepository,
): IContentService {
    /**
     * 컨텐츠 생성
     * Contents, ContentDetail 생성 및 Interest(관심분야 테이블) Associate Table 관계 설정 포함
     */
    @Transactional
    override fun createContent(dto: CreateContentData): Contents? {
        val newContents = Contents(contentType = dto.contentType)
        val newContentView = ContentView(contents = newContents)
        val newContentsDetail = ContentDetail(
                contents = newContents,
                name = dto.name,
                purpose = dto.purpose,
                contentText = dto.contentText,
                online = dto.online,
                location = dto.location ?: "",
                limitPeople = dto.limitPeople,
                recruitPeriod = dto.recruitPeriod,
                recruitMethod = dto.recruitMethod,
        )

        dto.interests.map {
            // Create Interest Categories
            var interest: Interest? = interestRepository.findByName(it)
            if (interest == null) {
                interest = Interest(name = it)
            }

            // InterestRelation Linking
            val rel = InterestRelations()
            rel.interest = interest
            rel.contentDetail = newContentsDetail
            newContentsDetail.interestRelations.add(rel)
            interest.interestRelations.add(rel)
        }

        newContents.contentView = newContentView

        return contentRepository.save(newContents)
    }
}