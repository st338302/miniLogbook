package com.roche.minilogbook.ui

import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.roche.minilogbook.R
import com.roche.minilogbook.databinding.ActivityMainBinding
import com.roche.minilogbook.domain.UnitType
import com.roche.minilogbook.ui.adapter.HistoryAdapter
import com.roche.minilogbook.ui.viewmodel.LogBookViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LogBookViewModel by viewModels()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intiView()
    }

    private fun intiView() {
        binding.rbMgDl.isChecked = true
        observeAverage()
        observeUnit()

        binding.radioGroup.setOnCheckedChangeListener { _: RadioGroup, checkedId: Int ->
            val selectedUnit =
                if (checkedId == binding.rbMgDl.id) UnitType.MG_DL else UnitType.MMOL_L
            viewModel.onUnitChanged(selectedUnit)
        }

        binding.btnSave.setOnClickListener {
            saveData()
        }

        initRecyclerView()

        observeEntries()
    }

    private fun initRecyclerView() {
        historyAdapter = HistoryAdapter()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = historyAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun saveData() {
        val measurement = binding.etMeasurement.text.toString()

        if (measurement.isEmpty()) {
            Toast.makeText(this, getString(R.string.enter_measurement), Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.saveInput(measurement, onClearedCallBack = {
            binding.etMeasurement.text?.clear()
            Toast.makeText(this, getString(R.string.measurement_saved), Toast.LENGTH_SHORT).show()
        }, onError = { msg -> Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() })
    }

    private fun observeUnit() {
        lifecycleScope.launch {
            viewModel.unit.collectLatest { unitType ->
                binding.tvUnit.text = unitType.rawValue
            }
        }
    }

    private fun observeAverage() {
        lifecycleScope.launch {
            viewModel.averageText.collectLatest { avg ->
                binding.tvAverage.text = getString(R.string.average_is, avg)
            }
        }
    }

    private fun observeEntries() {
        lifecycleScope.launch {
            viewModel.entries.collectLatest { list ->
                historyAdapter.submitList(list.map { e -> viewModel.formatValueForList(e.valueInMgPerDl) })
            }
        }
    }

}
