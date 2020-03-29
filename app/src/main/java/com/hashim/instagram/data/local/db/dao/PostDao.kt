package com.hashim.instagram.data.local.db.dao

import androidx.room.*
import com.hashim.instagram.data.local.db.entity.PostEntity
import io.reactivex.Single


@Dao
interface PostDao {

    @Query("SELECT * FROM post_entity ORDER BY createdAt DESC")
    fun getAll(): Single<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: PostEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(entity: List<PostEntity>)

    @Delete
    fun delete(entity: PostEntity)
}