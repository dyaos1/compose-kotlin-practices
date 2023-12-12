package com.example.bbs.service

import com.example.bbs.domain.Post
import com.example.bbs.exception.PostNotDeletableException
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.repository.PostRepository
import com.example.bbs.service.dto.PostCreateRequestDto
import com.example.bbs.service.dto.PostUpdateRequestDto
import com.example.bbs.service.dto.toEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PostService(
    private val postRepository: PostRepository,
) {
    @Transactional // readOnly 를 빼주어야 한다.
    fun createPost(requestDto: PostCreateRequestDto): Long {
        return postRepository.save(requestDto.toEntity()).id
    }

    @Transactional  // 마찬가지로, read가 아니라 update 이므로 readOnly를 빼주기 위해
    fun updatePost(id: Long, requestDto: PostUpdateRequestDto): Long {
        val post: Post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        post.update(requestDto)
        return id
    }

    @Transactional
    fun deletePost(id: Long, deletedBy: String): Long {
        val post: Post = postRepository.findByIdOrNull(id) ?: throw PostNotFoundException()
        if(post.createdBy != deletedBy) throw PostNotDeletableException()
        postRepository.delete(post)
        return id
    }
}
