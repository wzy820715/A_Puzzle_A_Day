package com.demo.apuzzleaday.calculate

import com.demo.apuzzleaday.entity.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureTimeMillis

typealias Piece = Array<CharArray>
typealias BoundaryMap = List<CharArray>
typealias onProcess = (boundaryResult: BoundaryMap) -> Unit

private val boundaryArray = listOf(
    charArrayOf('0', '0', '0', '0', '0', '0', 'x', 'x'),
    charArrayOf('0', '0', '0', '0', '0', '0', 'x', 'x'),
    charArrayOf('0', '0', '0', '0', '0', '0', '0', 'x'),
    charArrayOf('0', '0', '0', '0', '0', '0', '0', 'x'),
    charArrayOf('0', '0', '0', '0', '0', '0', '0', 'x'),
    charArrayOf('0', '0', '0', '0', '0', '0', '0', 'x'),
    charArrayOf('0', '0', '0', 'x', 'x', 'x', 'x', 'x'),
    charArrayOf('x', 'x', 'x', 'x', 'x', 'x', 'x', 'x')
)

private val puzzlePieces = arrayOf(
    PieceS(),
    PieceV(), PieceT(),
    PieceP(), PieceU(),
    PieceL(), PieceI()
)

private val solutionList = mutableListOf<BoundaryMap>()

const val MONTH = 3
const val DATE = 6

fun main() = runBlocking {
    val time = measureTimeMillis {
        calculate(MONTH, DATE){
//            println("--------------------------")
//            for (chars in it) {
//                println(chars.contentToString())
//            }
        }
//        val outPutData = solveList.let {
//            if(it.size>=3) it.take(3) else it
//        }
        for (it in solutionList) {
            println("-------------result-------------")
            for (chars in it) {
                println(printColour(chars))
            }
        }
    }
    println("cost: ${time}s total size: ${solutionList.size}")
}

fun setMonthAndDate(boundary: BoundaryMap, month: Int, date: Int) {
    if (month > 12 || date > 31)
        return
    boundary[month / 7][if (month < 7) month % 7 - 1 else month % 7] = 'm'
    val rel_date_x = if (date % 7 == 0) date / 7 - 1 else date / 7
    val rel_date_y = if (date % 7 == 0) 7 else date % 7
    boundary[rel_date_x + 2][rel_date_y - 1] = 'd'
}

suspend fun calculate(month: Int, date: Int,
                      process: (onProcess)? = null): MutableList<BoundaryMap>{
    solutionList.clear()
    val boundary = boundaryArray.copy()
    setMonthAndDate(boundary, month, date)
    for (piece_z in PieceZ().rotatedTypes) {
        withContext(Dispatchers.Default) {
            for (row in boundaryArray.indices) {
                launch {
                    for (col in boundaryArray[0].indices) {
                        if (checkFinishStop())
                            break
                        launch {
                            println("----------($row, $col)----------${Thread.currentThread().name}")
                            var boundaryCopy = boundary.copy()
                            //先放Z型拼图(因为Z型拼图只有一种方向的排法)
                            if (try2Place(boundaryCopy, piece_z, row, col)) {
                                scanPieceGroup(boundaryCopy.copy(), process = process)
                            }
                        }
                    }
                }
            }
        }
    }
    return solutionList
}

fun scanPieceGroup(boundary: BoundaryMap, index: Int = 0, process: (onProcess)? = null) {
    if (checkFinishStop()) {
        return
    }
    if (index >= puzzlePieces.size) {
        solutionList.add(boundary)
        return
    }

    val pieceGroup = puzzlePieces[index]

    for (type in pieceGroup.rotatedTypes) {
        var workCopy = boundary.copy()
        check(workCopy, type) {
            if (process != null) {
                process(it)
            }
            scanPieceGroup(it, index + 1, process)
        }
    }
}

fun check(
    boundary: BoundaryMap,
    piece: Piece,
    _startRow: Int = 0, _startCol: Int = -1,
    onSuccess: (boundaryResult: BoundaryMap) -> Unit
) {

    var startRow = _startRow
    var startCol = _startCol
    if (startCol >= boundary[0].size - 1) {
        startRow += 1
        startCol = 0
    } else {
        startCol += 1
    }
    if (startRow >= boundary.size - 1 && startCol >= boundary[0].size - 1)
        return
    if (checkFinishStop()) {
        return
    }
    var workCopy = boundary.copy()
    val isPutSuc = try2Place(workCopy, piece, startRow, startCol) {
        workCopy = boundary
    }
    if (isPutSuc) {
        onSuccess(workCopy)
        check(boundary.copy(), piece, startRow, startCol) {
            onSuccess(it)
        }
    } else {
        check(workCopy, piece, startRow, startCol) {
            onSuccess(it)
        }
    }
}

fun try2Place(
    workCopy: BoundaryMap,
    piece: Piece, startRow: Int, startCol: Int, rollback: (() -> Unit)? = null
): Boolean {
    val rowSize = workCopy.size
    val colSize = workCopy[0].size
    if (rowSize - startRow < piece.size - 1 ||
        colSize - startCol < piece[0].size - 1
    )
        return false
    if (checkFinishStop()) {
        return false
    }

    var isPutSuc = true
    var rowStartMark = -1
    var colStartMark = -1
    loop@ for (i in startRow until rowSize) {
        for (j in startCol until colSize) {
            val pieceX = i - startRow
            val pieceY = j - startCol
            if (pieceX in piece.indices &&
                pieceY in piece[0].indices &&
                piece[pieceX][pieceY] != '0'
            ) {
                if (workCopy[i][j] == '0') {
                    if (rowStartMark < 0) {
                        rowStartMark = i
                        colStartMark = j
                    }
                    workCopy[i][j] = piece[pieceX][pieceY]
                } else {
                    isPutSuc = false
                    if (rollback != null) {
                        rollback()
                    }
                    break@loop
                }
            }
        }
    }
    //如果存在<=2x2被包裹住的0，说明此种排布错误，直接跳过后续递归
    if (!checkClosedArea(workCopy, rowStartMark, colStartMark)) {
        isPutSuc = false
        if (rollback != null) {
            rollback()
        }
    }
    return isPutSuc
}

fun checkFinishStop(): Boolean {
    return solutionList.size > 0
}

/**
 * 检查是否存在被包裹住的0(ps: 从起点开始周围<=2x2的区域)
 */
fun checkClosedArea(
    workCopy: BoundaryMap,
    checkStartRow: Int,
    checkStartCol: Int
): Boolean {
    for (i in max(0, checkStartRow - 1) until min(workCopy.size, checkStartRow + 2)) {
        for (j in max(0, checkStartCol - 1) until min(workCopy[0].size, checkStartCol + 2)) {
            if (isClosed(doCheckClosed(workCopy.copy(), i + 1, j)))
                return false
            if (isClosed(doCheckClosed(workCopy.copy(), i - 1, j)))
                return false
            if (isClosed(doCheckClosed(workCopy.copy(), i, j + 1)))
                return false
            if (isClosed(doCheckClosed(workCopy.copy(), i, j - 1)))
                return false
        }
    }
    return true
}

fun doCheckClosed(
    tempCopy: BoundaryMap,
    checkRow: Int,
    checkCol: Int,
    markNum: Int = 0
): Int {
    if (checkRow < 0 || checkCol < 0 || checkRow >= tempCopy.size || checkCol >= tempCopy[0].size)
        return markNum
    var _markNum = markNum
    if (tempCopy[checkRow][checkCol] == '0') {
        _markNum++
        tempCopy[checkRow][checkCol] = '*'
        _markNum = doCheckClosed(tempCopy, checkRow + 1, checkCol, _markNum)
        _markNum = doCheckClosed(tempCopy, checkRow - 1, checkCol, _markNum)
        _markNum = doCheckClosed(tempCopy, checkRow, checkCol + 1, _markNum)
        _markNum = doCheckClosed(tempCopy, checkRow, checkCol - 1, _markNum)
    }
    return _markNum
}

fun isClosed(count: Int): Boolean {
    return count in 1..4
}

fun BoundaryMap.copy(): BoundaryMap {
    val result = mutableListOf<CharArray>()
    for (chars in this) {
        result.add(chars.copyOf())
    }
    return result.toList()
}

fun printColour(chars: CharArray): String {
    val str = StringBuffer()
    str.append("[")
    for ((i, char) in chars.withIndex()) {
        if (char != '0' && char != 'x' && char != 'm' && char != 'd') {
            val colorInt = when (char) {
                'I' -> 30
                'V' -> 31
                'U' -> 32
                'S' -> 33
                'L' -> 34
                'P' -> 35
                'T' -> 36
                'Z' -> 37
                else -> 39
            }
            str.append("\u001b[$colorInt;4m▆\u001b[0m")
        } else {
            str.append(char.toString())
        }
        if (i < chars.lastIndex)
            str.append(",")
    }
    str.append("]")
    //val aa = "\u001b[33;4m"+"char"+"\u001b[0m"+"123"
    return str.toString()
}