package com.roche.minilogbook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_book")
data class LogBookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val valueInMgPerDl: Double,
    val timestamp: Long
)
