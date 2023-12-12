package com.example.bbs.controller.dto

import com.example.bbs.service.dto.PostCreateRequestDto
import com.example.bbs.service.dto.PostUpdateRequestDto

data class PostUpdateRequest(
    val title: String,
    val content: String,
    val updatedBy: String,
)

fun PostUpdateRequest.toDto() = PostUpdateRequestDto(
    title = title,
    content = content,
    updatedBy = updatedBy,
)
