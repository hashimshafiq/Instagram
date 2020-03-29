package com.hashim.instagram.data.local.db.dao

import androidx.room.*
import com.hashim.instagram.data.local.db.entity.PostEntity


@Dao
interface PostDao {

    @Query("SELECT * FROM post_entity ORDER BY createdAt DESC")
    fun getAll(): List<PostEntity>

    @Insert
    fun insert(entity: PostEntity)

    @Delete
    fun delete(entity: PostEntity)
}