package com.example.bbs.service

import com.example.bbs.domain.Comment
import com.example.bbs.domain.Post
import com.example.bbs.domain.Tag
import com.example.bbs.exception.PostNotDeletableException
import com.example.bbs.exception.PostNotFoundException
import com.example.bbs.exception.PostNotUpdatableException
import com.example.bbs.repository.CommentRepository
import com.example.bbs.repository.PostRepository
import com.example.bbs.repository.TagRepository
import com.example.bbs.service.dto.PostCreateRequestDto
import com.example.bbs.service.dto.PostSearchRequestDto
import com.example.bbs.service.dto.PostSummaryResponseDto
import com.example.bbs.service.dto.PostUpdateRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.extensions.testcontainers.perSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.testcontainers.containers.GenericContainer

@SpringBootTest
class PostServiceTest(
    private val postService: PostService,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val tagRepository: TagRepository,
    private val likeService: LikeService,
) : BehaviorSpec({
    val redisContainer = GenericContainer<Nothing>("redis:5.0.3-alpine")

    beforeSpec {
        redisContainer.portBindings.add("16379:6379")
        redisContainer.start()
        listener(redisContainer.perSpec())

        postRepository.saveAll(
            listOf(
                Post(title = "title1", content = "content1", createdBy = "spark1", tags = listOf("tag1", "tag2")),
                Post(title = "title12", content = "content2", createdBy = "spark1", tags = listOf("tag1", "tag2")),
                Post(title = "title13", content = "content3", createdBy = "spark1", tags = listOf("tag1", "tag2")),
                Post(title = "title14", content = "content4", createdBy = "spark1", tags = listOf("tag1", "tag2")),
                Post(title = "title5", content = "content5", createdBy = "spark1", tags = listOf("tag1", "tag2")),
                Post(title = "title6", content = "content6", createdBy = "spark2", tags = listOf("tag1", "tag5")),
                Post(title = "title7", content = "content7", createdBy = "spark2", tags = listOf("tag1", "tag5")),
                Post(title = "title8", content = "content8", createdBy = "spark2", tags = listOf("tag1", "tag5")),
                Post(title = "title9", content = "content9", createdBy = "spark2", tags = listOf("tag1", "tag5")),
                Post(title = "title10", content = "content10", createdBy = "spark2", tags = listOf("tag1", "tag5"))
            )
        )
    }

    afterSpec{
        redisContainer.stop()
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
        When("태그를 추가하면") {
            val postId = postService.createPost(
                PostCreateRequestDto(
                    title = "제목",
                    content = "내용",
                    createdBy = "작성자",
                    tags = listOf("tag1", "tag2")
                )
            )
            then("태그가 정상적으로 추가됨을 확인") {
                val tags = tagRepository.findByPostId(postId)
                tags.size shouldBe 2
                tags[0].name shouldBe "tag1"
                tags[1].name shouldBe "tag2"
            }
        }
    }
    given("게시글 수정") {
        val saved = postRepository.save(
            Post(
                title = "title",
                content = "content",
                createdBy = "spark",
                tags = listOf("tag1", "tag2")
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
        When("태그가 수정 되었을때") {
            val updatedId = postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "spark",
                    tags = listOf("tag1", "tag2", "tag3")
                )
            )
            then("정상적으로 수정됨을 확인") {
                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tag3"
            }
            postService.updatePost(
                saved.id,
                PostUpdateRequestDto(
                    title = "update title",
                    content = "update content",
                    updatedBy = "spark",
                    tags = listOf("tag3", "tag2", "tag1")
                )
            )
            then("태그 순서가 정상적으로 변경됨을 확인") {
                val tags = tagRepository.findByPostId(updatedId)
                tags.size shouldBe 3
                tags[2].name shouldBe "tag1"
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
        tagRepository.saveAll(
            listOf(
                Tag(name = "tag1", post = saved, createdBy = "spark1"),
                Tag(name = "tag2", post = saved, createdBy = "spark2"),
                Tag(name = "tag3", post = saved, createdBy = "spark3")
            )
        )
        likeService.createLike(saved.id, "spark1")
        likeService.createLike(saved.id, "spark2")
        likeService.createLike(saved.id, "spark3")
        When("정상조회시") {
            val post = postService.getPost(saved.id)
            then("게시글의 내용이 정상적으로 반환 됨을 확인") {
                post.id shouldBe saved.id
                post.title shouldBe "title"
                post.content shouldBe "content"
                post.createdBy shouldBe "spark"
            }
            then("태그가 정상적으로 조회됨을 확인") {
                post.tags.size shouldBe 3
                post.tags[0] shouldBe "tag1"
                post.tags[1] shouldBe "tag2"
                post.tags[2] shouldBe "tag3"
            }
            then("좋아요 갯수가 조회됨을 확인"){
                post.likeCount shouldBe 3
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
                postPage.content[0].createdBy shouldBe "spark"
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
            val postPage: Page<PostSummaryResponseDto> = postService.findPageBy(
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
            then("첫번째 태그가 함께 조회됨") {
                postPage.content.forEach {
                    it.firstTag shouldBe "tag1"
                }
            }
        }
        When("태그로 검색") {
            val postPage = postService.findPageBy(
                PageRequest.of(0, 5),
                PostSearchRequestDto(tag = "tag5")
            )
            then("태그에 해당하는 게시글 반환") {
                postPage.number shouldBe 0
                postPage.size shouldBe 5
                postPage.content.size shouldBe 5
                postPage.content[0].title shouldBe "title10"
                postPage.content[1].title shouldBe "title9"
                postPage.content[4].title shouldBe "title6"
            }
        }
        When("좋아요가 2개씩 추가되었을때"){
            val postPage = postService.findPageBy(
                PageRequest.of(0, 5),
                PostSearchRequestDto(tag = "tag5")
            )
            postPage.content.forEach {
                likeService.createLike(it.id, "spark")
                likeService.createLike(it.id, "spark2")
            }
            val likedPostPage = postService.findPageBy(
                PageRequest.of(0,5),
                PostSearchRequestDto(tag = "tag5")
            )
            then("좋아요 개수가 정상적으로 조회됨을 확인"){
                likedPostPage.content.forEach{
                    it.likeCount shouldBe 2
                }
            }
        }
    }
})
