package com.hashim.instagram.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "liked_user_entity",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class LikedUserEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "t_id")
    val t_id : Long,

    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "postId")
    var postId: String,


    @ColumnInfo(name = "name")
    var name: String,


    @ColumnInfo(name = "profilePicUrl")
    var profilePicUrl: String?
)