package com.demo.apuzzleaday

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.demo.apuzzleaday.databinding.ActivityMainBinding
import com.demo.apuzzleaday.entity.PuzzleResult
import com.demo.apuzzleaday.viewmodel.SolveViewModel
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private val mViewMode: SolveViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var isShowProcessGUI = true
    private var startTime = 0L

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
                        binding.tvTimer.text = "${System.currentTimeMillis() - startTime}ms"
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

    fun onPlayBtnClicked(@Suppress("UNUSED_PARAMETER") view: View) {
        startActivity(Intent(this, PuzzlePlayActivity::class.java))
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        startTime = System.currentTimeMillis()
        binding.tvTimer.text = "..."
        if(!isShowProcessGUI){
            binding.progressBar.visibility = View.VISIBLE
        }
        binding.btnDatePick.text = "${month + 1}月${dayOfMonth}日"
        binding.btnDatePick.isEnabled = false
        binding.solutionView.setNewDate(month, dayOfMonth)
        mViewMode.solve(month + 1, dayOfMonth, isShowProcessGUI)
    }
}