package com.hashim.instagram.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
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
    val creator: UserEntity,

    @NotNull
    @ColumnInfo(name = "likedBy")
    val likedBy: MutableList<UserEntity>?,

    @NotNull
    @ColumnInfo(name = "createdAt")
    val createdAt: Date

){
    @Entity(tableName = "user_entity",
            foreignKeys = [
                ForeignKey(
                    entity = PostEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["postId"],
                    onDelete = CASCADE
                )
            ])
    data class UserEntity(
        @PrimaryKey(autoGenerate = false)
        @NotNull
        @ColumnInfo(name = "id")
        val id: String,

        @ColumnInfo(name = "postId")
        @NotNull
        val postId: Long,

        @NotNull
        @ColumnInfo(name = "name")
        val name: String,

        @NotNull
        @ColumnInfo(name = "profilePicUrl")
        val profilePicUrl: String?
    )

}