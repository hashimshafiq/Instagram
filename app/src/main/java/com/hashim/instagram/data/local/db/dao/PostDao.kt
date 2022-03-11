package com.hashim.instagram.data.local.db.dao

import androidx.room.*
import com.hashim.instagram.data.local.db.entity.LikedUserEntity
import com.hashim.instagram.data.local.db.entity.PostEntity
import com.hashim.instagram.data.local.db.entity.PostWithUser
import com.hashim.instagram.data.local.db.entity.UserEntity
import com.hashim.instagram.data.model.Post


@Dao
abstract class PostDao {

    @Transaction
    @Query("SELECT * FROM user_entity")
    abstract suspend fun getAll(): List<PostWithUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entity: List<PostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPostCreator(entity: UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPostCreatorAll(entity: List<UserEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPostLiked(entity: LikedUserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertPostLikedAll(entity: List<LikedUserEntity>)

    @Delete
    abstract suspend fun delete(entity: PostEntity)

    suspend fun preparePostAndCreator(posts: List<Post>){
        for(item in posts){
            insert(PostEntity(item.id,item.imageUrl,item.imageWidth,item.imageHeight,item.createdAt,item.creator.id))
            insertPostCreator(UserEntity(item.creator.id,item.creator.name,item.creator.profilePicUrl))
            for(likes in item.likedBy!!){
                insertPostLiked(LikedUserEntity(0L,likes.id,item.id,likes.name,likes.profilePicUrl))
            }
        }
    }

}