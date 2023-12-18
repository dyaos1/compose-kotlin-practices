package com.example.bbs.repository

import com.example.bbs.domain.Tag
import org.springframework.data.jpa.repository.JpaRepository


interface TagRepository : JpaRepository<Tag, Long> {
    fun findByPostId(postId: Long): List<Tag>
}