package com.demo.apuzzleaday.view.play

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import com.demo.apuzzleaday.R
import com.demo.apuzzleaday.getScreenWidth

class PieceView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mPaint: Paint
    private val gridWidth = getScreenWidth(context) / 9
    private var curAngle = 0
    private var pieceArray: Array<CharArray>
    private var originOutlineArray : Array<CharArray?>
    private var rotateOutlineArray : Array<CharArray?>
    private val animator by lazy {
        ObjectAnimator.ofFloat(this, "rotation",0f, 0f).apply {
            duration = 200
            doOnEnd {
                curAngle = (curAngle+90)%360
                rotateArray()
                requestLayout()
            }
        }
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
//        animator.setFloatValues(curAngle.toFloat(), (curAngle+90).toFloat())
//        animator.start()
//        curAngle = (curAngle+90)%360
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
        setMeasuredDimension(width, height)
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
    }

}