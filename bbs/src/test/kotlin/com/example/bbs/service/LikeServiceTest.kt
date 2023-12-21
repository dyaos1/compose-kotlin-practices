package com.example.bbs.service

import com.example.bbs.domain.Like
import com.example.bbs.domain.Post
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.repository.LikeRepository
import com.example.bbs.repository.PostRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class LikeServiceTest(
    private val likeService: LikeService,
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository
) : BehaviorSpec({
    given("좋아요 생성시"){
        val post = postRepository.save(Post(
            title = "title",
            createdBy = "spark",
            content = "content"
        ))
        When("인풋이 정상적이라면"){
            val likeId:Long = likeService.createLike(post.id, "spark")
            then("좋아요 정상적으로 추가"){
                val like: Like? = likeRepository.findByIdOrNull(likeId)
                like shouldNotBe null
                like?.createdBy shouldBe "spark"
            }
        }
        When("게시글이 존재하지 않을때"){
            then("게시글 찾을수 없다고 오류 발생"){
                shouldThrow<PostNotFoundException> {
                    likeService.createLike(999L, "spark")
                }
            }
        }
    }
})
