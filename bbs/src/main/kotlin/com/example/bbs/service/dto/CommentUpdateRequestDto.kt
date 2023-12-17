package com.example.bbs.service.dto

import com.example.bbs.domain.Comment

data class CommentUpdateRequestDto(
    val content: String,
    val updatedBy: String,
)

