package com.demo.apuzzleaday

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.demo.apuzzleaday.viewmodel.SolveViewModel
import com.demo.apuzzleaday.databinding.ActivityMainBinding
import com.demo.apuzzleaday.entity.PuzzleResult
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val mViewMode: SolveViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var mTimerJob: Job
    private var isShowProcessGUI = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.btnDatePick.setOnLongClickListener {
            isShowProcessGUI = !isShowProcessGUI
            Toast.makeText(this@MainActivity,
                if(isShowProcessGUI) "GUI模式" else "无GUI模式", Toast.LENGTH_SHORT).show()
            false
        }
        lifecycleScope.launch {
            mViewMode.resultLiveData.observe(this@MainActivity) { result ->
                when (result) {
                    is PuzzleResult.Process -> {
                        binding.solutionView.showSolution(result.process)
                    }
                    is PuzzleResult.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnDatePick.isEnabled = true
                        mTimerJob.cancel()
                        if (result.list.isNotEmpty()) {
                            binding.solutionView.showSolution(result.list.first())
                        }
                    }
                }
            }
        }
    }

    fun onBtnClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        val calendar: Calendar = Calendar.getInstance()
        val dialog = DatePickerDialog(
            this, this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MARCH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        recordCostTime()
        if(!isShowProcessGUI){
            binding.progressBar.visibility = View.VISIBLE
        }
        binding.btnDatePick.text = "${month + 1}月${dayOfMonth}日"
        binding.btnDatePick.isEnabled = false
        binding.solutionView.setNewDate(month, dayOfMonth)
        mViewMode.solve(month + 1, dayOfMonth, isShowProcessGUI)
    }

    private fun recordCostTime() {
        val startTime = System.currentTimeMillis()
        mTimerJob = lifecycleScope.launch(Dispatchers.Default) {
            while (isActive) {
                launch(Dispatchers.Main) {
                    binding.tvTimer.text = "${System.currentTimeMillis() - startTime}ms"
                }
            }
        }
    }
}