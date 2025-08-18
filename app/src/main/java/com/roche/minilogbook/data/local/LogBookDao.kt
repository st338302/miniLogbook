package com.roche.minilogbook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogBookDao {

    @Insert
    suspend fun insert(entry: LogBookEntity)

    @Query("SELECT * from log_book ORDER BY timestamp DESC")
    fun getAll(): Flow<List<LogBookEntity>>

    @Query("SELECT AVG(valueInMgPerDl) FROM log_book")
    fun getAverageMgPerDl(): Flow<Double?>
}