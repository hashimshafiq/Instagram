package com.hashim.instagram.data.local.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "post_entity")
data class PostEntity(

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "imgUrl")
    val imageUrl: String,

    @ColumnInfo(name = "imgWidth")
    val imageWidth: Int?,

    @ColumnInfo(name = "imgHeight")
    val imageHeight: Int?,

    @ColumnInfo(name = "createdAt")
    val createdAt: Date,

    @ColumnInfo(name = "creator")
    val creator: String


)