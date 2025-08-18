package com.roche.minilogbook.data.repository

import com.roche.minilogbook.data.local.LogBookDao
import com.roche.minilogbook.data.local.LogBookEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogRepository @Inject constructor(private val dao: LogBookDao) {

    fun entries() = dao.getAll()

    fun averageMgPerDl() = dao.getAverageMgPerDl()

    suspend fun addLog(valueMgPerDl: Double) {
        dao.insert(
            LogBookEntity(valueInMgPerDl = valueMgPerDl, timestamp = System.currentTimeMillis())
        )

    }

}