package com.example.bbs.service.dto

data class PostUpdateRequestDto(
    val title: String,
    val content: String,
    val updatedBy: String
)
