package com.demo.apuzzleaday.viewmodel

import androidx.lifecycle.*
import com.demo.apuzzleaday.calculate.calculate
import com.demo.apuzzleaday.entity.PuzzleResult
import kotlinx.coroutines.launch

class SolveViewModel : ViewModel() {

    private val optLiveData = MutableLiveData<PuzzleResult>()
    val resultLiveData : LiveData<PuzzleResult> = optLiveData

    fun solve(month: Int, date: Int, showProcess: Boolean){
        viewModelScope.launch {
//            val time = measureTimeMillis {
                val resultList = calculate(month, date){
                    if(showProcess){
                        optLiveData.postValue(PuzzleResult.Process(it))
                    }
                }
                optLiveData.value = PuzzleResult.Success(resultList)
//            }
//            println("cost: ${time}s total size: ${solutionList.size}")
        }
    }

}


