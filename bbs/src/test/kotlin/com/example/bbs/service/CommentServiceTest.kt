package com.example.bbs.service

import com.example.bbs.domain.Comment
import com.example.bbs.domain.Post
import com.example.bbs.exception.CommentNotDeletableException
import com.example.bbs.exception.CommentNotUpdatableException
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.repository.CommentRepository
import com.example.bbs.repository.PostRepository
import com.example.bbs.service.dto.CommentCreateRequestDto
import com.example.bbs.service.dto.CommentUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
class CommentServiceTest(
    private val commentService: CommentService,
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
) : BehaviorSpec({
    given("댓글 생성시") {
        val post: Post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "게시글 생성자"
            )
        )
        When("인풋이 성상적으로 들어오면") {
            val commentId: Long = commentService.createComment(
                post.id,
                CommentCreateRequestDto(
                    content = "댓글내용",
                    createdBy = "댓글 작성자"
                )
            )!!
            then("정상 생섬됨을 확인한다") {
                commentId shouldBeGreaterThan 0L
                val comment: Comment? = commentRepository.findByIdOrNull(commentId)
                comment shouldNotBe null
                comment?.content shouldBe "댓글내용"
                comment?.createdBy shouldBe "댓글 작성자"
            }
        }
        When("게시글이 존재하지 않으면") {
            then("게시글 존재하지 않음 에러 발생") {
                shouldThrow<PostNotFoundException> {
                    commentService.createComment(
                        999L,
                        CommentCreateRequestDto(
                            content = "댓글내용",
                            createdBy = "작성자"
                        )
                    )
                }
            }
        }
    }
    given("댓글 수정시") {
        val post: Post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "게시글 생성자"
            )
        )
        val savedComment = commentRepository.save(
            Comment(
                content = "hello world",
                post = post,
                createdBy = "spark"
            )
        )
        When("정상적으로 수정될 경우") {
            val updatedCommentId: Long = commentService.updateComment(
                savedComment.id,
                CommentUpdateRequestDto(
                    content = "hi there",
                    updatedBy = "spark"
                )
            )
            then("정상 수정됨을 확인") {
                updatedCommentId shouldBe savedComment.id
                val updatedComment: Comment? = commentRepository.findByIdOrNull(savedComment.id)
                updatedComment shouldNotBe null
                updatedComment?.content = "hi there"
                updatedComment?.updatedBy = "spark"
            }
        }
        When("댓글 작성자가 불일치 할 경우") {
            then("수정할 수 없는 게시글이라고 오류 발생") {
                shouldThrow<CommentNotUpdatableException> {
                    commentService.updateComment(
                        savedComment.id,
                        CommentUpdateRequestDto(
                            content = "hi there",
                            updatedBy = "not spark"
                        )
                    )
                }
            }
        }
    }
    given("댓글 삭제시") {
        val post: Post = postRepository.save(
            Post(
                title = "게시글 제목",
                content = "게시글 내용",
                createdBy = "게시글 생성자"
            )
        )
        val savedComment = commentRepository.save(
            Comment(
                content = "hello world",
                post = post,
                createdBy = "spark"
            )
        )
        val savedComment2 = commentRepository.save(
            Comment(
                content = "hello world2",
                post = post,
                createdBy = "spark2"
            )
        )
        When("인풋이 정상적이라면,") {
            val commentId = commentService.deleteComment(savedComment.id, "spark")
            then("잘 삭제된다") {
                commentId shouldBe savedComment.id
                commentRepository.findByIdOrNull(savedComment.id) shouldBe null
            }
        }
        When("작성자와 삭제자가 다르다면") {
            then("삭제할 수 없는 댓글 오류 발생") {
                shouldThrow<CommentNotDeletableException> { commentService.deleteComment(savedComment2.id, "삭제자") }
            }
        }
    }
})
