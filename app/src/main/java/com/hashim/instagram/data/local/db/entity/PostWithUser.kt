package com.hashim.instagram.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PostWithUser(

    @Embedded
    val userEntity : UserEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "creator"
    )
    val postEntity: List<PostEntity>


)
