package com.hashim.instagram.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hashim.instagram.data.local.db.dao.Converters
import com.hashim.instagram.data.local.db.dao.PostDao
import com.hashim.instagram.data.local.db.entity.LikedUserEntity
import com.hashim.instagram.data.local.db.entity.PostEntity
import com.hashim.instagram.data.local.db.entity.UserEntity
import javax.inject.Singleton

@TypeConverters(Converters::class)
@Singleton
@Database(
    entities = [
        PostEntity::class,
        UserEntity::class,
        LikedUserEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class DatabaseService : RoomDatabase() {

    abstract fun postDao(): PostDao
}