package com.example.bbs.domain

import com.example.bbs.exception.CommentNotUpdatableException
import com.example.bbs.service.dto.CommentUpdateRequestDto
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
class Comment(
    content: String,
    post: Post,
    createdBy: String,
) : BaseEntity(createdBy = createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    var content: String = content

    @ManyToOne(fetch = FetchType.LAZY)
    var post = post
        protected set

    fun update(updateRequestDto: CommentUpdateRequestDto) {
        if (this.createdBy != updateRequestDto.updatedBy) {
            throw CommentNotUpdatableException()
        }
        this.content = updateRequestDto.content
        super.update(updateRequestDto.updatedBy)
    }
}
