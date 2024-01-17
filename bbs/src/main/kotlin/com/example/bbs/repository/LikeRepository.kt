package com.example.bbs.repository

import com.example.bbs.domain.Like
import org.springframework.data.jpa.repository.JpaRepository

interface LikeRepository: JpaRepository<Like, Long> {
    fun countByPostId(postId: Long): Long
}
