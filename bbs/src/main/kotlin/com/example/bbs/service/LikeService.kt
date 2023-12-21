package com.example.bbs.service

import com.example.bbs.domain.Like
import com.example.bbs.domain.Post
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.repository.LikeRepository
import com.example.bbs.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository, private val postRepository: PostRepository
) {
    @Transactional
    fun createLike(postId: Long, createdBy: String) : Long {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        return likeRepository.save(Like(post, createdBy)).id
    }

    fun countLike(postId: Long): Long {
        return likeRepository.countByPostId(postId)
    }
}
