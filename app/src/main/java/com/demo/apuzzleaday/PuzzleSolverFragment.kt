package com.demo.apuzzleaday

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.demo.apuzzleaday.databinding.LayoutPuzzleSolverBinding
import com.demo.apuzzleaday.entity.PuzzleResult
import com.demo.apuzzleaday.viewmodel.SolveViewModel
import kotlinx.coroutines.launch
import java.util.*

class PuzzleSolverFragment: Fragment(), DatePickerDialog.OnDateSetListener{

    private var startTime = 0L
    private var isShowProcessGUI = true
    private lateinit var binding: LayoutPuzzleSolverBinding
    private val mViewMode: SolveViewModel by viewModels({requireActivity()})

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = LayoutPuzzleSolverBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.btnDatePick.setOnLongClickListener {
            isShowProcessGUI = !isShowProcessGUI
            Toast.makeText(requireActivity(),
                if(isShowProcessGUI) "GUI模式" else "无GUI模式", Toast.LENGTH_SHORT).show()
            false
        }
        binding.btnDatePick.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val dialog = DatePickerDialog(
                requireActivity(), this@PuzzleSolverFragment,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MARCH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
        lifecycleScope.launch {
            mViewMode.resultLiveData.observe(requireActivity()) { result ->
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