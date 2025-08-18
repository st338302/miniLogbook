package com.roche.minilogbook.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LogBookEntity::class], version = 1)
abstract class LogBookDataBase : RoomDatabase() {
    abstract fun logDao(): LogBookDao
}