package com.example.bbs.controller.dto

import com.example.bbs.service.dto.PostDetailResponseDto
import com.example.bbs.service.dto.PostSummaryResponseDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

data class PostSummaryResponse(
    val id: Long,
    val title: String,
    val createdBy: String,
    val createdAt: String,
)

fun Page<PostSummaryResponseDto>.toResponse() = PageImpl(
    content.map{ it.toResponse() },
    pageable,
    totalElements
)

fun PostSummaryResponseDto.toResponse() = PostSummaryResponse(
    id = id,
    title = title,
    createdBy = createdBy,
    createdAt = createdAt,
)
