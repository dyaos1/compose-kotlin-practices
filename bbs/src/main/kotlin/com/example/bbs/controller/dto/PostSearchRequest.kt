package com.example.bbs.controller.dto

import org.springframework.web.bind.annotation.RequestParam

data class PostSearchRequest(
    @RequestParam
    val title: String?,

    @RequestParam
    val createdBy: String?,
)
