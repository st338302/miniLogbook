package com.roche.minilogbook.domain

object Conversion {
    private const val FACTOR = 18.0182 // 1 mmol/L = 18.0182 mg/dL
    fun toMgPerDl(valueMmolL: Double): Double = valueMmolL * FACTOR
    fun toMmolL(valueMgDl: Double): Double = valueMgDl / FACTOR
}