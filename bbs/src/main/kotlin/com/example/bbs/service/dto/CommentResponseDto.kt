package com.example.bbs.service.dto

import com.example.bbs.domain.Comment

data class CommentResponseDto(
    val id: Long,
    val content: String,
    val createdBy: String,
    val createdAt: String,
)

fun Comment.toResponseDto() = CommentResponseDto(
    id = id,
    content = content,
    createdBy = createdBy,
    createdAt = createdAt.toString()
)
