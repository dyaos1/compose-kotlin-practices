package com.example.bbs.service

import com.example.bbs.domain.Like
import com.example.bbs.domain.Post
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.repository.LikeRepository
import com.example.bbs.repository.PostRepository
import com.example.bbs.util.RedisUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val redisUtil: RedisUtil,
) {
    @Transactional
    fun createLike(postId: Long, createdBy: String) : Long {
        val post: Post = postRepository.findByIdOrNull(postId) ?: throw PostNotFoundException()
        redisUtil.increment(redisUtil.getLikeCountKey(postId))
        return likeRepository.save(Like(post, createdBy)).id
    }

    fun countLike(postId: Long): Long {
        redisUtil.getCount(redisUtil.getLikeCountKey(postId))?.let { return it }

        with(likeRepository.countByPostId(postId)) {
            redisUtil.setData(redisUtil.getLikeCountKey(postId), this)
            return this
        }

    }
}
