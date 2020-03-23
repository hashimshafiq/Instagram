package com.hashim.instagram.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hashim.instagram.data.local.db.dao.DummyDao
import com.hashim.instagram.data.local.db.entity.DummyEntity
import javax.inject.Singleton

@Singleton
@Database(
    entities = [
        DummyEntity::class
    ],
    exportSchema = false,
    version = 1
)
abstract class DatabaseService : RoomDatabase() {

    abstract fun dummyDao(): DummyDao
}