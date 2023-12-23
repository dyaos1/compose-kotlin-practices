package com.example.bbs.repository

import com.example.bbs.domain.QTag.tag
import com.example.bbs.domain.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport


interface TagRepository : JpaRepository<Tag, Long>, CustomRepository {
    fun findByPostId(postId: Long): List<Tag>
}

interface CustomRepository {
    fun findPageBy(pageRequest: Pageable, tagName: String): Page<Tag>
}

class CustomRepositoryImpl : CustomRepository, QuerydslRepositorySupport(Tag::class.java) {
    override fun findPageBy(pageRequest: Pageable, tagName: String): Page<Tag> {
        return from(tag)
            .join(tag.post).fetchJoin()
            .where(tag.name.eq(tagName))
            .orderBy(tag.post.createdAt.desc())
            .offset(pageRequest.offset)
            .limit(pageRequest.pageSize.toLong())
            .fetchResults()
            .let { PageImpl(it.results, pageRequest, it.total) }
    }
}
