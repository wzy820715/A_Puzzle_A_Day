package com.demo.apuzzleaday.view.play

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.demo.apuzzleaday.R
import com.demo.apuzzleaday.getScreenHeight
import com.demo.apuzzleaday.getScreenWidth
import kotlin.math.min

class PieceView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mPaint: Paint
    private val gridWidth = (min(getScreenWidth(context), getScreenHeight(context)) / PuzzleDragLayout.GRID_COUNT).toFloat()
    private var pieceArray: Array<CharArray>
    private var originOutlineArray : Array<CharArray?>
    private var rotateOutlineArray : Array<CharArray?>
    private val blankRectList: MutableList<RectF> by lazy {
        recordBlankRect()
    }

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PieceView)
        val shape = ta.getColor(R.styleable.PieceView_pieceShape, 0)
        val shapeColor = ta.getInt(R.styleable.PieceView_pieceColor, 0)
        ta.recycle()
        mPaint = Paint().apply {
            isDither = false
            isAntiAlias = true
            style = Paint.Style.FILL
            color = shapeColor
        }
        pieceArray = when (shape) {
            0 -> PuzzleData.frontZ
            1 -> PuzzleData.frontS
            2 -> PuzzleData.frontL
            3 -> PuzzleData.frontV
            4 -> PuzzleData.frontP
            5 -> PuzzleData.frontT
            6 -> PuzzleData.frontU
            7 -> PuzzleData.frontI
            else -> arrayOf()
        }

        originOutlineArray = arrayOfNulls(pieceArray.size)
        for (index in originOutlineArray.indices) {
            originOutlineArray[index] = CharArray(pieceArray[0].size)
        }
        rotateOutlineArray = arrayOfNulls(pieceArray[0].size)
        for (index in rotateOutlineArray.indices) {
            rotateOutlineArray[index] = CharArray(pieceArray.size)
        }
    }

    fun rotatePiece(){
        rotateArray()
        requestLayout()
    }

    fun flipPiece(){
        flipArray()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = gridWidth * pieceArray[0].size
        val height = gridWidth * pieceArray.size
        setMeasuredDimension(width.toInt(), height.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.style = Paint.Style.FILL
        for ((y, row) in pieceArray.withIndex()) {
            for ((x, col) in row.withIndex()) {
                if(col == '0')
                    continue
                canvas.drawRect(
                    x.toFloat() * gridWidth,
                    y.toFloat() * gridWidth,
                    (x * gridWidth + gridWidth).toFloat(),
                    (y * gridWidth + gridWidth).toFloat(),
                    mPaint
                )
            }
        }
    }

    private fun rotateArray(){
        val rotateArray = Array(pieceArray[0].size){
            CharArray(pieceArray.size)
        }

        for(j in pieceArray[0].indices){
            for(i in pieceArray.size-1 downTo 0){
                rotateArray[j][pieceArray.size-1-i] = pieceArray[i][j]
            }
        }
        pieceArray = rotateArray
        updateBlankRectList()
    }

    private fun flipArray(){
        val count = if(pieceArray[0].size%2==0) pieceArray[0].size/2-1 else pieceArray[0].size/2
        for (i in pieceArray.indices) {
            for(j in 0..count){
                pieceArray[i][pieceArray[i].size - j -1] = pieceArray[i][j].also {
                    pieceArray[i][j] = pieceArray[i][pieceArray[i].size - j -1]
                }
            }
        }
        updateBlankRectList()
    }

    private fun recordBlankRect(): MutableList<RectF>{
        val rectList = mutableListOf<RectF>()
        for ((y, row) in pieceArray.withIndex()) {
            for ((x, col) in row.withIndex()) {
                if(col == '0'){
                    rectList.add(RectF(x * gridWidth, y * gridWidth,
                            x * gridWidth + gridWidth, y * gridWidth + gridWidth))
                }
            }
        }
        return rectList
    }

    private fun updateBlankRectList(){
        blankRectList.clear()
        blankRectList.addAll(recordBlankRect())
    }

    fun isTouchBlankArea(touchX: Float, touchY: Float): Boolean{
        val relX = touchX - left
        val relY = touchY - top
        var flag = false
        for (rect in blankRectList) {
            if(rect.contains(relX, relY)){
                flag = true
                break
            }
        }
        return flag
    }

}