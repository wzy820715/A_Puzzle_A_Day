package com.demo.apuzzleaday.entity

import com.demo.apuzzleaday.calculate.BoundaryMap

sealed class PuzzleResult {
    data class Process(val process: BoundaryMap) : PuzzleResult()
    data class Success(val list: List<BoundaryMap>) : PuzzleResult()
}