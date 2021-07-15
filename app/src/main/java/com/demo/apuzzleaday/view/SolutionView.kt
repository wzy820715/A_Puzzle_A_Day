package com.demo.apuzzleaday.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.demo.apuzzleaday.calculate.BoundaryMap
import com.demo.apuzzleaday.dp

class SolutionView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val dataList = mutableMapOf<Char, MutableList<IntArray>>()
    private val textBounds = Rect()
    private var gridWidth = 0f
    private var gridHeight = 0f
    private var targetMonth = -1
    private var targetDate = -1
    private val emptyGrids = listOf(
        intArrayOf(0, 6),
        intArrayOf(1, 6),
        intArrayOf(6, 3),
        intArrayOf(6, 4),
        intArrayOf(6, 5),
        intArrayOf(6, 6),
    )

    private val monthsArray = arrayOf(
        "Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug",
        "Sep", "Oct", "Nov", "Dec"
    )

    companion object {
        private const val GRID_COUNT = 7
    }

    private val paint_text = Paint().apply {
        isDither = false
        isAntiAlias = true
        style = Paint.Style.FILL
        textSize = 14f.dp
        color = 0xcc333333.toInt()
    }

    private val paint_text_bold = Paint(paint_text).apply {
        typeface = Typeface.DEFAULT_BOLD
        color = 0xdd000000.toInt()
    }

    private val paint_grid = Paint().apply {
        isDither = false
        isAntiAlias = true
        style = Paint.Style.FILL
        color = 0xaa8e7437.toInt()
    }

    private val paint_empty = Paint(paint_grid).apply {
        color = Color.WHITE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gridWidth = (width / GRID_COUNT).toFloat()
        gridHeight = (height / GRID_COUNT).toFloat()
    }

    fun showSolution(result: BoundaryMap,
                     process: ((MutableMap<Char, MutableList<IntArray>>) ->
                     MutableMap<Char, MutableList<IntArray>>)? = null) {
        val transferMap = mutableMapOf<Char, MutableList<IntArray>>()
        for ((i, row) in result.withIndex()) {
            for ((j, col) in row.withIndex()) {
                if (col == 'm' || col == 'd')
                    continue
                if (!transferMap.containsKey(col))
                    transferMap[col] = mutableListOf()
                transferMap[col]?.add(intArrayOf(i, j))
            }
        }
        dataList.clear()
        if (process != null) {
            dataList.putAll(process(transferMap))
        }else{
            dataList.putAll(transferMap)
        }
        postInvalidateOnAnimation()
    }

    fun setNewDate(month: Int, date: Int){
        targetMonth = month
        targetDate = date
        dataList.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制日期
        drawMonthAndDate(canvas)
        //绘制线框
//        drawGuides(canvas)
        //绘制拼图
        drawSolution(canvas)
    }

    private fun drawSolution(canvas: Canvas) {
        for (entry in dataList) {
            paint_grid.color = when (entry.key) {
                'I' -> 0xaaef5b9c.toInt()
                'V' -> 0xaaed1941.toInt()
                'U' -> 0xaaf47920.toInt()
                'S' -> 0xaaffd400.toInt()
                'L' -> 0xaa33a3dc.toInt()
                'P' -> 0xaa9b95c9.toInt()
                'T' -> 0xaa45b97c.toInt()
                'Z' -> 0xaa8e7437.toInt()
                else -> Color.TRANSPARENT
            }

            for (ints in entry.value) {
                val x_pos = ints.last()
                val y_pos = ints.first()
                canvas.drawRect(
                    x_pos.toFloat() * gridWidth,
                    y_pos.toFloat() * gridHeight,
                    x_pos * gridWidth + gridWidth,
                    y_pos * gridHeight + gridHeight,
                    paint_grid
                )
            }
        }
    }

    private fun drawMonthAndDate(canvas: Canvas) {
        var col: Int
        var row: Int
        //绘制月份
        for ((i, month) in monthsArray.withIndex()) {
            paint_text.getTextBounds(month, 0, month.length, textBounds)
            if (i < 6) {
                row = 0
                col = i
            } else {
                row = 1
                col = i - 6
            }
            canvas.drawText(
                month,
                col * gridWidth + gridWidth / 2 - (textBounds.left + textBounds.right) / 2,
                row * gridWidth + gridHeight / 2 - (textBounds.top + textBounds.bottom) / 2,
                if(targetMonth == i) paint_text_bold else paint_text
            )
        }
        //绘制日期
        for(i in 1..31){
            val date = i.toString()
            paint_text.getTextBounds(date, 0, date.length, textBounds)
            col = if (i % 7 == 0) 7 else i % 7
            row = if (i % 7 == 0) i / 7 - 1 else i / 7
            canvas.drawText(
                date,
                (col-1) * gridWidth + gridWidth / 2 - (textBounds.left + textBounds.right) / 2,
                (row+2) * gridWidth + gridHeight / 2 - (textBounds.top + textBounds.bottom) / 2,
                if(targetDate == i) paint_text_bold else paint_text
            )
        }
        val offset = 5f
        //覆盖空白区
        for (emptyGrid in emptyGrids) {
            val x_pos = emptyGrid.last()
            val y_pos = emptyGrid.first()
            canvas.drawRect(
                x_pos.toFloat() * gridWidth,
                y_pos.toFloat() * gridHeight,
                x_pos * gridWidth + gridWidth + offset,
                y_pos * gridHeight + gridHeight + offset,
                paint_empty
            )
        }
    }

    private fun drawGuides(canvas: Canvas) {
        for (i in 0..7) {
            canvas.drawLine(0f, i * gridHeight, width.toFloat(), i * gridHeight, paint_text)
            canvas.drawLine(i * gridWidth, 0f, i * gridWidth, height.toFloat(), paint_text)
        }
    }

}