package com.example.bbs.service

import com.example.bbs.domain.Post
import com.example.bbs.exception.PostNotDeletableException
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.exception.PostNotUpdatableException
import com.example.bbs.repository.PostRepository
import com.example.bbs.service.dto.PostCreateRequestDto
import com.example.bbs.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
): BehaviorSpec({
    given("게시글 생성시") {
        When("게시글 인풋이 정상적으로 들어오면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                title="제목",
                content="내용",
                createdBy="작성자",
            )
            )
            then("게시글이 정상적으로 생성됨을 확인") {
                postId shouldBeGreaterThan 0L
                val post = postRepository.findByIdOrNull(postId)
                post shouldNotBe null
                post?.title shouldBe "제목"
                post?.content shouldBe "내용"
            }
        }
    }
    given("게시글 수정") {
        val saved = postRepository.save(
            Post(
            title="title", content="content", createdBy="spark"
        )
        )
        When("정상 수정시") {
            val updatedId = postService.updatePost(saved.id, PostUpdateRequestDto(
                title = "update title",
                content = "update content",
                updatedBy = "spark",
            )
            )
            then("게시글이 정상적으로 수정됨을 확인") {
                saved.id shouldBe updatedId
                val updated: Post? = postRepository.findByIdOrNull(updatedId)
                updated shouldNotBe null
                updated?.title shouldBe "update title"
                updated?.content shouldBe "update content"
                updated?.updatedBy shouldBe "spark"
            }
        }

        When("게시글이 없을때") {

            then("게시글을 찾을 수 없다는 에러 발생") {
                shouldThrow<PostNotFoundException> { postService.updatePost(99L, PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "update spark",
                )) }
            }
        }

        When("작성자가 동일하지 않으면") {
            then("수정할수 없는 게시글이라고 에러 발생") {
                shouldThrow<PostNotUpdatableException> { postService.updatePost(1L, PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "update spark",
                ))

                }
            }
        }
    }

    given("게시글 삭제") {
        val saved = postRepository.save(
            Post(
                title="title", content="content", createdBy="spark"
            ))
        When("정상 삭제") {
            val postId = postService.deletePost(saved.id, "spark")
            then("이상 무") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }
        val saved2 = postRepository.save(
            Post(
                title="title", content="content", createdBy="spark"
            ))
        When("작성자와 동일하지 않을 때"){
            then("삭제 할 수 없는 게시글입니다 라고 오류 발생") {
                shouldThrow<PostNotDeletableException> { postService.deletePost(saved2.id, "no spark")}
            }
        }
    }
}) {

}
