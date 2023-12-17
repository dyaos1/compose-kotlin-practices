package com.example.bbs.domain

import com.example.bbs.exception.PostNotUpdatableException
import com.example.bbs.service.dto.PostUpdateRequestDto
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Post(
    createdBy: String,
    title: String,
    content: String,
) : BaseEntity(createdBy) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0

    var title: String = title
        protected set
    var content: String = content
        protected set

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = [CascadeType.ALL])
    var comments: MutableList<Comment> = mutableListOf()
        protected set

    fun update(postUpdateRequestDto: PostUpdateRequestDto) {
        if (this.createdBy != postUpdateRequestDto.updatedBy) {
            throw PostNotUpdatableException()
        }
        this.title = postUpdateRequestDto.title
        this.content = postUpdateRequestDto.content
        this.updatedBy = postUpdateRequestDto.updatedBy
    }
}
