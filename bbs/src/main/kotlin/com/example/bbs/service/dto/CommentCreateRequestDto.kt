package com.example.bbs.service.dto

import com.example.bbs.domain.Comment
import com.example.bbs.domain.Post

data class CommentCreateRequestDto(
    val content: String,
    val createdBy: String,
)

fun CommentCreateRequestDto.toEntity(post: Post): Comment = Comment(
    content = content,
    createdBy = createdBy,
    post = post
)
