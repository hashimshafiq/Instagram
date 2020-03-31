package com.hashim.instagram.data.local.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CompletePost(

    @Embedded
    val userEntity : UserEntity,



    //@Embedded
    //val likedUser : MutableList<UserEntity>?,

    @Relation(
        parentColumn = "id",
        entityColumn = "creator"
    )
    val postEntity: List<PostEntity>

)
