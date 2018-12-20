package com.an.dagger.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.an.dagger.data.local.dao.MovieDao
import com.an.dagger.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
