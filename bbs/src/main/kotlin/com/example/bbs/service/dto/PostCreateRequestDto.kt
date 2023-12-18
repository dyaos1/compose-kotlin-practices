package com.example.bbs.service.dto

import com.example.bbs.domain.Post

data class PostCreateRequestDto(
    val title: String,
    val content: String,
    val createdBy: String,
    val tags: List<String> = emptyList(),
)

fun PostCreateRequestDto.toEntity() = Post(
    createdBy = createdBy,
    title = title,
    content = content,
    tags = tags,
)
