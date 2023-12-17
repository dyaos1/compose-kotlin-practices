package com.example.bbs.service

import com.example.bbs.domain.Comment
import com.example.bbs.domain.Post
import com.example.bbs.exception.PostNotDeletableException
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.exception.PostNotUpdatableException
import com.example.bbs.repository.CommentRepository
import com.example.bbs.repository.PostRepository
import com.example.bbs.service.dto.PostCreateRequestDto
import com.example.bbs.service.dto.PostSearchRequestDto
import com.example.bbs.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
) : BehaviorSpec({
    beforeSpec {
        postRepository.saveAll(
            listOf(
                Post(title = "title1", content = "content1", createdBy = "spark1"),
                Post(title = "title12", content = "content2", createdBy = "spark1"),
                Post(title = "title13", content = "content3", createdBy = "spark1"),
                Post(title = "title14", content = "content4", createdBy = "spark1"),
                Post(title = "title5", content = "content5", createdBy = "spark1"),
                Post(title = "title6", content = "content6", createdBy = "spark2"),
                Post(title = "title7", content = "content7", createdBy = "spark2"),
                Post(title = "title8", content = "content8", createdBy = "spark2"),
                Post(title = "title9", content = "content9", createdBy = "spark2"),
                Post(title = "title10", content = "content10", createdBy = "spark2")
            )
        )
    }
    given("게시글 생성시") {
        When("게시글 인풋이 정상적으로 들어오면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "작성자"
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
                title = "title",
                content = "content",
                createdBy = "spark"
            )
        )
        When("정상 수정시") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "spark"
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
                shouldThrow<PostNotFoundException> {
                    postService.updatePost(
                        99L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update spark"
                        )
                    )
                }
            }
        }

        When("작성자가 동일하지 않으면") {
            then("수정할수 없는 게시글이라고 에러 발생") {
                shouldThrow<PostNotUpdatableException> {
                    postService.updatePost(
                        1L,
                        PostUpdateRequestDto(
                            title = "update title",
                            content = "update content",
                            updatedBy = "update spark"
                        )
                    )
                }
            }
        }
    }

    given("게시글 삭제") {
        val saved = postRepository.save(
            Post(
                title = "title",
                content = "content",
                createdBy = "spark"
            )
        )
        When("정상 삭제") {
            val postId = postService.deletePost(saved.id, "spark")
            then("이상 무") {
                postId shouldBe saved.id
                postRepository.findByIdOrNull(postId) shouldBe null
            }
        }
        val saved2 = postRepository.save(
            Post(
                title = "title",
                content = "content",
                createdBy = "spark"
            )
        )
        When("작성자와 동일하지 않을 때") {
            then("삭제 할 수 없는 게시글입니다 라고 오류 발생") {
                shouldThrow<PostNotDeletableException> { postService.deletePost(saved2.id, "no spark") }
            }
        }
    }

    given("게시글 상세조회") {
        val saved = postRepository.save(
            Post(
                title = "title",
                content = "content",
                createdBy = "spark"
            )
        )
        When("정상조회시") {
            val post = postService.getPost(saved.id)
            then("게시글의 내용이 정상적으로 반환 됨을 확인") {
                post.id shouldBe saved.id
                post.title shouldBe "title"
                post.content shouldBe "content"
                post.createdBy shouldBe "spark"
            }
        }
        When("게시글이 없을때") {
            then("게시글을 찾을수 없다는 오류 리턴") {
                shouldThrow<PostNotFoundException> { postService.getPost(999L) }
            }
        }
        When("댓글 추가시") {
            commentRepository.save(Comment(content = "댓글 내용1", post = saved, createdBy = "댓글 작성자1"))
            commentRepository.save(Comment(content = "댓글 내용2", post = saved, createdBy = "댓글 작성자2"))
            commentRepository.save(Comment(content = "댓글 내용3", post = saved, createdBy = "댓글 작성자3"))
            val post = postService.getPost(saved.id)
            then("댓글이 함께 조회됨을 확인") {
                post.comments.size shouldBe 3
                post.comments[0].content shouldBe "댓글 내용1"
                post.comments[1].content shouldBe "댓글 내용2"
                post.comments[2].content shouldBe "댓글 내용3"
                post.comments[0].createdBy shouldBe "댓글 작성자1"
                post.comments[1].createdBy shouldBe "댓글 작성자2"
                post.comments[2].createdBy shouldBe "댓글 작성자3"
            }
        }
    }

    given("게시글 목록 조회시") {
        When("정상 조회") {
            val postPage = postService.findPageBy(
                PageRequest.of(0, 5),
                PostSearchRequestDto()
            )
            then("게시글 페이지 반환") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title"
                postPage.content[0].createdBy shouldBe "spark2"
            }
        }
        When("타이틀로 검색") {
            val postPage = postService.findPageBy(
                PageRequest.of(0, 5),
                PostSearchRequestDto(title = "title1")
            )
            then("타이틀에 해당하는 게시글 반환") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title1"
                postPage.content[0].createdBy shouldBe "spark2"
            }
        }
        When("작성자로 검색") {
            val postPage = postService.findPageBy(
                PageRequest.of(0, 5),
                PostSearchRequestDto(createdBy = "spark1")
            )
            then("작성자에 해당하는 게시글 반환") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldContain "title5"
                postPage.content[0].createdBy shouldBe "spark1"
            }
        }
    }
})
