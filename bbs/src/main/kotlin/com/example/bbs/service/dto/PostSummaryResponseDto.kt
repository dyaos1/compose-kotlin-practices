package com.example.bbs.service.dto

import com.example.bbs.domain.Post
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

data class PostSummaryResponseDto(
    val id: Long,
    val title: String,
    val createdAt: String,
    val createdBy: String,
)

fun Page<Post>.toSummaryResponseDto() = PageImpl(
    content.map { it.toSummaryResponseDto() },
    pageable,
    totalElements
)

fun Post.toSummaryResponseDto() = PostSummaryResponseDto(
    id = id,
    title = title,
    createdBy = createdBy,
    createdAt = createdAt.toString()
)
