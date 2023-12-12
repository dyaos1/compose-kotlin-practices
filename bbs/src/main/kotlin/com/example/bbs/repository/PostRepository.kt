package com.example.bbs.repository

import com.example.bbs.domain.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, Long> {
}
