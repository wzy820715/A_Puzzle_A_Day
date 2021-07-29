package com.demo.apuzzleaday.view.play

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
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
        recordRect()
    }
    private val pieceRectList = mutableListOf<Pair<Pair<Int,Int>, RectF>>()

    private lateinit var animator :ObjectAnimator
    private val reverseInterpolator = ReverseInterpolator()

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

    fun rotatePiece(touchX: Float, touchY: Float, callback: (Float, Float) -> Unit){
        if(this::animator.isInitialized && animator.isRunning)
            return
        val relX = touchX - left
        val relY = touchY - top
        var centerX = 0
        var centerY = 0
        for (pair in pieceRectList) {
            if(pair.second.contains(relX, relY)){
                centerX = pair.first.first
                centerY = pair.first.second
                break
            }
        }
        val moveViewX = (centerX - (pieceArray.size - 1 - centerY)) * gridWidth
        val moveViewY = (centerY - centerX) * gridWidth

        pivotX = centerX*gridWidth + gridWidth/2
        pivotY = centerY*gridWidth + gridWidth/2
        animator = ObjectAnimator.ofFloat(this, "rotation", 0f, 90f)
            .apply {
                interpolator = LinearInterpolator()
                duration = 100
                doOnEnd {
                    this@PieceView.postDelayed({
                        removeAllListeners()
                        interpolator = reverseInterpolator
                        duration = 0
                        start()
                        callback(moveViewX, moveViewY)
                        rotateArray()
                        requestLayout()
                    },2)
                }
                start()
            }
    }

    fun flipPiece(touchX: Float, touchY: Float, callback: (Float, Float) -> Unit){
        if(this::animator.isInitialized && animator.isRunning)
            return
        val relX = touchX - left
        val relY = touchY - top
        var centerX = 0
        for (pair in pieceRectList) {
            if(pair.second.contains(relX, relY)){
                centerX = pair.first.first
                break
            }
        }
        val moveViewX = (centerX - (pieceArray[0].size - 1 - centerX)) * gridWidth

        pivotX = centerX*gridWidth + gridWidth/2
        animator = ObjectAnimator.ofFloat(this, "rotationY", 0f, 180f)
            .apply {
                interpolator = LinearInterpolator()
                duration = 100
                doOnEnd{
                    this@PieceView.postDelayed({
                        removeAllListeners()
                        interpolator = reverseInterpolator
                        duration = 0
                        start()
                        callback(moveViewX, 0f)
                        flipArray()
                        requestLayout()
                    },2)
                }
                start()
            }

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
                    (x * gridWidth + gridWidth),
                    (y * gridWidth + gridWidth),
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

    private fun recordRect(): MutableList<RectF>{
        val rectList = mutableListOf<RectF>()
        val pieceList = mutableListOf<Pair<Pair<Int,Int>, RectF>>()
        for ((y, row) in pieceArray.withIndex()) {
            for ((x, col) in row.withIndex()) {
                if(col == '0'){
                    rectList.add(RectF(x * gridWidth, y * gridWidth,
                        x * gridWidth + gridWidth, y * gridWidth + gridWidth))
                }else{
                    pieceList.add(Pair(Pair(x, y), RectF(x * gridWidth, y * gridWidth,
                        x * gridWidth + gridWidth, y * gridWidth + gridWidth)))
                }
            }
        }
        pieceRectList.clear()
        pieceRectList.addAll(pieceList)
        return rectList
    }

    private fun updateBlankRectList(){
        blankRectList.clear()
        blankRectList.addAll(recordRect())
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

    class ReverseInterpolator : Interpolator {
        override fun getInterpolation(input: Float) = 1 - input
    }

}