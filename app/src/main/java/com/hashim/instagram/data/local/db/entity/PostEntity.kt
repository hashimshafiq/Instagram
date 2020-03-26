package com.hashim.instagram.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hashim.instagram.data.model.Post
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "post_entity")
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    @NotNull
    val id: Long,

    @NotNull
    @ColumnInfo(name = "imgUrl")
    val imageUrl: String,

    @NotNull
    @ColumnInfo(name = "imgWidth")
    val imageWidth: Int?,

    @NotNull
    @ColumnInfo(name = "imgHeight")
    val imageHeight: Int?,

    @NotNull
    @ColumnInfo(name = "user")
    val creator: Post.User,

    @NotNull
    @ColumnInfo(name = "likedBy")
    val likedBy: MutableList<Post.User>?,

    @NotNull
    @ColumnInfo(name = "createdAt")
    val createdAt: Date



)