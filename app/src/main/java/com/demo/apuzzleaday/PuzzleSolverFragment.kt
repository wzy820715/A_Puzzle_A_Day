package com.demo.apuzzleaday

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.createDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.demo.apuzzleaday.calculate.BoundaryMap
import com.demo.apuzzleaday.databinding.LayoutPuzzleSolverBinding
import com.demo.apuzzleaday.entity.PuzzleResult
import com.demo.apuzzleaday.viewmodel.SolveViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class PuzzleSolverFragment: Fragment(), DatePickerDialog.OnDateSetListener{

    private var startTime = 0L
    private var isProcessing = false
    private var isShowProcessGUI = true
    private val mViewMode: SolveViewModel by viewModels({requireActivity()})
    private var result_part_pieces = mutableMapOf<Char, MutableList<IntArray>>()
    private lateinit var result_bounds: BoundaryMap
    private lateinit var result_all_pieces: MutableMap<Char, MutableList<IntArray>>
    private lateinit var binding: LayoutPuzzleSolverBinding
    private lateinit var dataStore: DataStore<Preferences>

    private val HINT_PIECES = charArrayOf('S'.uppercaseChar(), 'U'.uppercaseChar())

    companion object{
        private val SHOW_FULL_RESULT = booleanPreferencesKey("show_full_result")
    }

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
        dataStore = requireActivity().createDataStore("setting-pref")

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
                        isProcessing = false
                        binding.progressBar.visibility = View.GONE
                        binding.btnDatePick.isEnabled = true
                        binding.tvTimer.text = "${System.currentTimeMillis() - startTime}ms"
                        if (result.list.isNotEmpty()) {
                            result_bounds = result.list.first()
                            updateResult()
                        }
                    }
                }
            }
            dataStore.getValueFlow(SHOW_FULL_RESULT, false).collect {
                binding.checkBox.isChecked = it
            }
        }
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            lifecycleScope.launch {
                updateResult()
                dataStore.setValue(SHOW_FULL_RESULT, isChecked)
            }
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        startTime = System.currentTimeMillis()
        isProcessing = true
        binding.tvTimer.text = "..."
        if(!isShowProcessGUI){
            binding.progressBar.visibility = View.VISIBLE
        }
        binding.btnDatePick.text = "${month + 1}月${dayOfMonth}日"
        binding.btnDatePick.isEnabled = false
        binding.solutionView.setNewDate(month, dayOfMonth)
        mViewMode.solve(month + 1, dayOfMonth, isShowProcessGUI)
    }

    private fun updateResult(){
        if(!this::result_bounds.isInitialized || isProcessing)
            return
        binding.solutionView.showSolution(result_bounds){
            result_all_pieces = it
            if(binding.checkBox.isChecked){
                result_all_pieces
            }else{
                result_part_pieces.apply {
                    result_part_pieces.clear()
                    HINT_PIECES.forEach { hint ->
                        result_part_pieces[hint] = it.getValue(hint)
                    }
                }
            }
        }
    }

}