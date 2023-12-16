package com.example.bbs.service

import com.example.bbs.domain.Comment
import com.example.bbs.domain.Post
import com.example.bbs.exception.CommentNotDeletableException
import com.example.bbs.exception.CommentNotFoundException
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.repository.CommentRepository
import com.example.bbs.repository.PostRepository
import com.example.bbs.service.dto.CommentCreateRequestDto
import com.example.bbs.service.dto.CommentUpdateRequestDto
import com.example.bbs.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) {
    @Transactional
    fun createComment(postId: Long, commentCreateRequestDto: CommentCreateRequestDto): Long? {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        return commentRepository.save(commentCreateRequestDto.toEntity(post)).id
    }

    @Transactional
    fun updateComment(commentId: Long, commentUpdateRequestDto: CommentUpdateRequestDto): Long {
        val comment: Comment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        comment.update(commentUpdateRequestDto)
        return comment.id
    }

    @Transactional
    fun deleteComment(commentId: Long, deleteBy: String): Long {
        val comment: Comment = commentRepository.findByIdOrNull(commentId) ?: throw CommentNotFoundException()
        if (comment.createdBy != deleteBy) {
            throw CommentNotDeletableException()
        }
        commentRepository.delete(comment)
        return commentId
    }
}
