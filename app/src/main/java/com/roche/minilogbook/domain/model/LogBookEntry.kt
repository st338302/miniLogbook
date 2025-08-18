package com.roche.minilogbook.domain.model

data class LogBookEntry(
    val id: Long, val valueInMgPerDl: Double, val timestamp: Long = System.currentTimeMillis()
)