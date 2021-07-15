package com.demo.apuzzleaday.view.play

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.customview.widget.ViewDragHelper
import com.demo.apuzzleaday.R
import com.demo.apuzzleaday.dp
import com.demo.apuzzleaday.getScreenWidth
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class PuzzleDragLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs),
    GestureDetector.OnGestureListener {

    companion object {
        const val GRID_COUNT = 10
    }

    private val textBounds = Rect()
    private val gridWidth = (getScreenWidth(context) / GRID_COUNT).toFloat()
    private val gridHeight = gridWidth
    private val puzzleWidth = gridWidth * PuzzleData.boundaryArray[0].size
    private val puzzleHeight = gridHeight * PuzzleData.boundaryArray.size
    private val tips1 = context.getString(R.string.gesture_tips_1)
    private val tips2 = context.getString(R.string.gesture_tips_2)
    private var downX = 0f
    private var downY = 0f
    private var startX = 0f
    private var startY = 0f
    private var targetMonth = -1
    private var targetDate = -1
    private lateinit var touchedView: PieceView
    private val childrenPosMap = mutableMapOf<PieceView, Pair<Int, Int>>()
    private val mDragHelper = ViewDragHelper.create(this, DragHelperCallback())
    private val gestureDetector = GestureDetectorCompat(context, this)

    init {
        setWillNotDraw(false)
        val calendar: Calendar = Calendar.getInstance()
        targetMonth = calendar.get(Calendar.MARCH)
        targetDate = calendar.get(Calendar.DAY_OF_MONTH)
    }

    private val monthsArray = arrayOf(
        "Jan", "Feb", "Mar", "Apr",
        "May", "Jun", "Jul", "Aug",
        "Sep", "Oct", "Nov", "Dec"
    )

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

    private val paint_tips = Paint(paint_text).apply {
        textSize = 12f.dp
        color = 0xffcccccc.toInt()
    }

    private val paint_grid = Paint(paint_text).apply {
        color = 0xfff6f5ec.toInt()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        for (child in children) {
            val pair = childrenPosMap[child]
            if (pair != null) {
                child.layout(
                    pair.first, pair.second,
                    pair.first + child.width, pair.second + child.height
                )
            } else {
                childrenPosMap[child as PieceView] = Pair(child.left, child.top)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startX = (width - puzzleWidth) / 2
        startY = (height - puzzleHeight) / 2
    }

    override fun computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.actionMasked
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel()
            return false
        }
        return mDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
            }
        }
        mDragHelper.processTouchEvent(event)
        if (!this::touchedView.isInitialized)
            return false
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(startX, startY)
        drawMonthAndDate(canvas)
        drawTipsText(canvas)
        canvas.restore()
    }

    private fun checkMagnetStop(targetView: PieceView) {
        val left = targetView.left
        val top = targetView.top
        val min_range_x = startX - gridWidth / 2
        val max_range_x = startX + puzzleWidth + gridWidth / 2
        val min_range_y = startY - gridHeight / 2
        val max_range_y = startY + puzzleHeight + gridHeight / 2
        if (left.toFloat() in (min_range_x..max_range_x) &&
            top.toFloat() in (min_range_y..max_range_y) &&
            (left + targetView.width).toFloat() in (min_range_x..max_range_x) &&
            (top + targetView.height).toFloat() in (min_range_y..max_range_y)
        ) {
            var stepX = 0
            var stepY: Int
            var settleLeft = -1
            var settleTop = -1
            loop@ for (i in PuzzleData.boundaryArray[0].indices) {
                stepX += gridWidth.toInt()
                stepY = 0
                for (j in PuzzleData.boundaryArray.indices) {
                    stepY += gridHeight.toInt()
                    if (left < stepX + startX &&
                        left + targetView.width < puzzleWidth + startX + gridWidth &&
                        top < stepY + startY &&
                        top + targetView.height < puzzleWidth + startY + gridHeight
                    ) {
                        if (left < stepX + startX - gridWidth.toInt() / 2) {
                            settleLeft = (stepX - gridWidth + startX).roundToInt()
                        } else {
                            if (stepX + startX >= puzzleWidth + startX - targetView.width + gridWidth) {
                                settleLeft = -1
                                settleTop = -1
                                break@loop
                            } else {
                                settleLeft = (stepX + startX).roundToInt()
                            }
                        }
                        if (top < stepY + startY - gridHeight.toInt() / 2) {
                            settleTop = (stepY - gridHeight + startY).roundToInt()
                        } else {
                            if (stepY + startY >= puzzleHeight + startY - targetView.height + gridHeight) {
                                settleLeft = -1
                                settleTop = -1
                                break@loop
                            } else {
                                settleTop = (stepY + startY).roundToInt()
                            }
                        }
                        break@loop
                    }
                }
            }
            if (settleLeft > -1 || settleTop > -1) {
//                mDragHelper.settleCapturedViewAt(settleLeft, settleTop)
                targetView.layout(settleLeft, settleTop,
                    settleLeft + targetView.width, settleTop+ targetView.height)
                childrenPosMap[targetView] = Pair(settleLeft, settleTop)
                return
            }
        }
        childrenPosMap[targetView] = Pair(targetView.left, targetView.top)
        invalidate()
    }

    private inner class DragHelperCallback : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            val suspendedView = child as PieceView
            val blankTouch = suspendedView.isTouchBlankArea(downX, downY)
            return if (blankTouch) {
                val index = indexOfChild(child)
                if (index >= 0) {
                    loop@ for (i in index - 1 downTo 0) {
                        val underView = getChildAt(i) as PieceView
                        if(mDragHelper.isViewUnder(underView, downX.toInt(), downY.toInt()) &&
                            !underView.isTouchBlankArea(downX, downY)){
                            removeViewAt(index)
                            addView(child, i)
                            mDragHelper.captureChildView(underView, pointerId)
                            break@loop
                        }
                    }
                }
                false
            } else {
                touchedView = suspendedView
                bringChildToFront(child)
                true
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            checkMagnetStop(releasedChild as PieceView)
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            val leftBound = paddingLeft
            val rightBound = width - touchedView.width
            return min(max(left, leftBound), rightBound)
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            val topBound = paddingTop
            val bottomBound = height - touchedView.height
            return min(max(top, topBound), bottomBound)
        }
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        if (mDragHelper.isViewUnder(touchedView, e.x.toInt(), e.y.toInt()) &&
            !touchedView.isTouchBlankArea(e.x, e.y)
        ) {
            childrenPosMap[touchedView] = Pair(touchedView.left, touchedView.top)
            touchedView.rotatePiece()
        }
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        if (mDragHelper.isViewUnder(touchedView, e.x.toInt(), e.y.toInt()) &&
            !touchedView.isTouchBlankArea(e.x, e.y)
        )
            touchedView.flipPiece()
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onScroll(
        e1: MotionEvent?, e2: MotionEvent?,
        distanceX: Float, distanceY: Float
    ): Boolean {
        return false
    }

    override fun onFling(
        e1: MotionEvent?, e2: MotionEvent?,
        velocityX: Float, velocityY: Float
    ): Boolean {
        return false
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
            canvas.drawRect(
                col * gridWidth,
                row * gridWidth,
                col * gridWidth + gridWidth,
                row * gridWidth + gridHeight,
                paint_grid
            )
            canvas.drawText(
                month,
                col * gridWidth + gridWidth / 2 - (textBounds.left + textBounds.right) / 2,
                row * gridWidth + gridHeight / 2 - (textBounds.top + textBounds.bottom) / 2,
                if (targetMonth == i) paint_text_bold else paint_text
            )
        }
        //绘制日期
        for (i in 1..31) {
            val date = i.toString()
            paint_text.getTextBounds(date, 0, date.length, textBounds)
            col = if (i % 7 == 0) 7 else i % 7
            row = if (i % 7 == 0) i / 7 - 1 else i / 7
            canvas.drawRect(
                (col - 1) * gridWidth,
                (row + 2) * gridWidth,
                (col - 1) * gridWidth + gridWidth,
                (row + 2) * gridWidth + gridHeight,
                paint_grid
            )
            canvas.drawText(
                date,
                (col - 1) * gridWidth + gridWidth / 2 - (textBounds.left + textBounds.right) / 2,
                (row + 2) * gridWidth + gridHeight / 2 - (textBounds.top + textBounds.bottom) / 2,
                if (targetDate == i) paint_text_bold else paint_text
            )
        }
    }

    private fun drawTipsText(canvas: Canvas) {
        paint_tips.getTextBounds(tips1, 0, tips1.length, textBounds)
        val tip_text_1_height = textBounds.bottom - textBounds.top
        paint_tips.getTextBounds(tips2, 0, tips2.length, textBounds)
        val tip_text_2_height = textBounds.bottom - textBounds.top
        val topStart = (gridWidth - tip_text_1_height - tip_text_2_height) / 2
        canvas.drawText(
            tips1,
            4 * gridWidth, 6 * gridWidth + topStart * 2,
            paint_tips)

        canvas.drawText(
            tips2,
            4 * gridWidth, 6 * gridWidth + topStart * 2 + tip_text_2_height,
            paint_tips)
    }

}