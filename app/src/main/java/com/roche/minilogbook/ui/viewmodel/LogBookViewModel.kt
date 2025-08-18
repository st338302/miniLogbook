package com.roche.minilogbook.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roche.minilogbook.data.repository.LogRepository
import com.roche.minilogbook.domain.Conversion
import com.roche.minilogbook.domain.UnitType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogBookViewModel @Inject constructor(
    private val repo: LogRepository
) : ViewModel() {

    private val _unit = MutableStateFlow(UnitType.MG_DL)
    val unit: StateFlow<UnitType> = _unit

    val entries = repo.entries()

    val averageText = combine(repo.averageMgPerDl(), unit) { avgMg, unitType ->
        val avg = avgMg ?: 0.0
        val shown = when (unitType) {
            UnitType.MG_DL -> avg
            UnitType.MMOL_L -> Conversion.toMmolL(avg)
        }
        "%.2f %s".format(shown, unitType.rawValue)
    }

    fun onUnitChanged(newUnit: UnitType) {
        _unit.value = newUnit
    }


    fun convertBetweenUnits(currentText: String, from: UnitType, to: UnitType): String {
        val number = currentText.toDoubleOrNull() ?: return ""
        val value = when (from) {
            to -> number
            UnitType.MG_DL -> Conversion.toMmolL(number)
            else -> Conversion.toMgPerDl(number)
        }
        val pattern = "%.2f"
        return pattern.format(value)
    }

    fun formatValueForList(entry: Double):String{
        val convertedValue = when (_unit.value) {
            UnitType.MG_DL -> entry
            UnitType.MMOL_L -> Conversion.toMmolL(entry)
        }
        return "%.2f %s".format(convertedValue, _unit.value.rawValue)
    }

    fun saveInput(
        text: String,
        onClearedCallBack: () -> Unit,
        onError: (String) -> Unit
    ) {
        val raw = text.toDoubleOrNull()
        if (raw == null) {
            onError("Please enter a number")
            return
        }
        if (raw < 0) {
            onError("Value must be ≥ 0")
            return
        }
        val unitValue = if (_unit.value == UnitType.MG_DL) raw else Conversion.toMgPerDl(raw)
        viewModelScope.launch {
            repo.addLog(unitValue)
            onClearedCallBack()
        }
    }
}
