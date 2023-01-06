package com.b18dccn562.phonemanager.presentation.custom_view.lock_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.sqrt

class LockView(context: Context, attrs: AttributeSet) : View(
    context,
    attrs
) {

    interface OnCompleteDrawingListener {
        fun onCompleteDrawing(patterns: List<Int>)
    }

    //variables
    private var viewSize: Float = 0F
    private var rowColumnWidth: Float = 0F
    private var rectPadding: Float = 0F
    private var innerCircleWidth: Float = 0F

    private var buttons: List<Button> = listOf(
        Button(1),
        Button(2),
        Button(3),
        Button(4),
        Button(5),
        Button(6),
        Button(7),
        Button(8),
        Button(9)
    )

    private var pattern = mutableListOf<Button>()
    private var isDrawing: Boolean = false
    private var onCompleteListener: OnCompleteDrawingListener? = null

    private var currentTouchCoordinate: Coordinate? = null

    //paint
    private val dotCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dotInnerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectedDotCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val selectedDotInnerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mDesiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        val mDesiredHeight = MeasureSpec.getSize(heightMeasureSpec)

        viewSize = max(
            mDesiredHeight.toFloat(),
            mDesiredWidth.toFloat()
        )

        rowColumnWidth = viewSize / 3
        rectPadding = rowColumnWidth / 4
        innerCircleWidth = rectPadding / 12

        createRect(0, 0, buttons[0])
        createRect(0, 1, buttons[1])
        createRect(0, 2, buttons[2])
        createRect(1, 0, buttons[3])
        createRect(1, 1, buttons[4])
        createRect(1, 2, buttons[5])
        createRect(2, 0, buttons[6])
        createRect(2, 1, buttons[7])
        createRect(2, 2, buttons[8])

        setMeasuredDimension(
            viewSize.toInt(),
            viewSize.toInt()
        )

    }

    private fun createRect(row: Int, column: Int, button: Button) {
        val outerRect = RectF()
        val innerRect = RectF()

        outerRect.set(
            rowColumnWidth * column + rectPadding,
            rowColumnWidth * row + rectPadding,
            rowColumnWidth * (column + 1) - rectPadding,
            rowColumnWidth * (row + 1) - rectPadding
        )

        val touchRange = TouchRange()
        touchRange.centerCoordinate.xCoordinate = outerRect.centerX()
        touchRange.centerCoordinate.yCoordinate = outerRect.centerY()
        touchRange.distant = (outerRect.right - outerRect.left) / 2

        button.touchRange = touchRange
        button.outerRectF = outerRect

        val centerX = outerRect.centerX()
        val centerY = outerRect.centerY()

        innerRect.set(
            centerX - innerCircleWidth,
            centerY - innerCircleWidth,
            centerX + innerCircleWidth,
            centerY + innerCircleWidth
        )
        button.innerRectF = innerRect
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                handleActionDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                handleActionMove(event)
            }
            MotionEvent.ACTION_UP -> {
                handleActionUp()
            }
        }
        invalidate()
        return true
    }

    private fun handleActionUp() {
        if (isDrawing) {
            val selectedPattern = mutableListOf<Int>()
            for (button in pattern) {
                selectedPattern.add(button.number)
            }
            onCompleteListener?.onCompleteDrawing(selectedPattern)
            isDrawing = false
            currentTouchCoordinate = null
        }
    }

    private fun handleActionMove(event: MotionEvent) {
        if (isDrawing) {
            currentTouchCoordinate!!.xCoordinate = event.x
            currentTouchCoordinate!!.yCoordinate = event.y
            if (buttons[4].containTouchEvent(event) && !pattern.contains(buttons[4])) {
                pattern.add(buttons[4])
            } else {
                var selected = -1
                if (
                    event.x >= buttons[4].touchRange.centerCoordinate.xCoordinate &&
                    event.y >= buttons[4].touchRange.centerCoordinate.yCoordinate
                ) {
                    selected =
                        checkInsideRect(event, buttons[5], buttons[7], buttons[8])
                } else if (
                    event.x >= buttons[4].touchRange.centerCoordinate.xCoordinate &&
                    event.y <= buttons[4].touchRange.centerCoordinate.yCoordinate
                ) {
                    selected =
                        checkInsideRect(event, buttons[1], buttons[2], buttons[5])
                } else if (
                    event.x <= buttons[4].touchRange.centerCoordinate.xCoordinate &&
                    event.y >= buttons[4].touchRange.centerCoordinate.yCoordinate
                ) {
                    selected =
                        checkInsideRect(event, buttons[3], buttons[6], buttons[7])
                } else if (
                    event.x <= buttons[4].touchRange.centerCoordinate.xCoordinate &&
                    event.y <= buttons[4].touchRange.centerCoordinate.yCoordinate
                ) {
                    selected =
                        checkInsideRect(event, buttons[0], buttons[1], buttons[3])
                }
                if (selected != -1 && !pattern.contains(buttons[selected])) {
                    pattern.add(buttons[selected])
                }
            }
        }
    }

    fun setOnCompleteListener(listener: OnCompleteDrawingListener) {
        onCompleteListener = listener
    }

    private fun checkInsideRect(event: MotionEvent, vararg buttons: Button): Int {
        for (button in buttons) {
            if (button.containTouchEvent(event)) {
                return button.number - 1
            }
        }
        return -1
    }

    private fun handleActionDown(event: MotionEvent) {
        for (button in buttons) {
            if (isInButton(button, event)) {
                pattern.add(button)
                isDrawing = true
                currentTouchCoordinate = Coordinate(xCoordinate = event.x, yCoordinate = event.y)
                break
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (button in buttons) {
            drawBaseCircle(button, canvas)
        }
        drawSelectedPatternCircles(canvas)
        drawLines(canvas)
        drawCurrentLine(canvas)
    }

    private fun drawCurrentLine(canvas: Canvas?) {
        if (pattern.isNotEmpty() && currentTouchCoordinate != null) {
            val lastButton = pattern[pattern.size - 1]
            linePaint.color = Color.WHITE
            linePaint.style = Paint.Style.FILL_AND_STROKE
            linePaint.strokeWidth = 10F

            canvas?.drawLine(
                lastButton.touchRange.centerCoordinate.xCoordinate,
                lastButton.touchRange.centerCoordinate.yCoordinate,
                currentTouchCoordinate!!.xCoordinate,
                currentTouchCoordinate!!.yCoordinate,
                linePaint
            )
        }
    }

    private fun drawLines(canvas: Canvas?) {
        linePaint.color = Color.WHITE
        linePaint.style = Paint.Style.FILL_AND_STROKE
        linePaint.strokeWidth = 10F
        for (button in pattern) {
            val index = pattern.indexOf(button)
            if (index != pattern.size - 1) {
                canvas?.drawLine(
                    button.touchRange.centerCoordinate.xCoordinate,
                    button.touchRange.centerCoordinate.yCoordinate,
                    pattern[index + 1].touchRange.centerCoordinate.xCoordinate,
                    pattern[index + 1].touchRange.centerCoordinate.yCoordinate,
                    linePaint
                )
            }
        }
    }

    private fun drawBaseCircle(
        button: Button,
        canvas: Canvas?
    ) {
        dotCirclePaint.color = Color.WHITE
        dotCirclePaint.style = Paint.Style.STROKE
        dotCirclePaint.strokeWidth = 1F

        dotInnerCirclePaint.color = Color.WHITE
        dotInnerCirclePaint.style = Paint.Style.FILL_AND_STROKE
        dotInnerCirclePaint.strokeWidth = 12F

        canvas?.drawArc(
            button.innerRectF,
            0F,
            360F,
            true,
            dotInnerCirclePaint
        )
    }

    private fun drawSelectedPatternCircles(
        canvas: Canvas?
    ) {
        selectedDotCirclePaint.color = Color.WHITE
        selectedDotCirclePaint.style = Paint.Style.STROKE
        selectedDotCirclePaint.strokeWidth = 1F

        selectedDotInnerCirclePaint.color = Color.WHITE
        selectedDotInnerCirclePaint.style = Paint.Style.FILL_AND_STROKE
        selectedDotInnerCirclePaint.strokeWidth = 12F

        for (button in pattern) {
            canvas?.drawArc(
                button.innerRectF,
                0F,
                360F,
                true,
                selectedDotInnerCirclePaint
            )
        }
    }

    private fun calculateDistant(button: Button, event: MotionEvent?): Float {
        event?.let {
            return sqrt(
                (button.touchRange.centerCoordinate.xCoordinate - event.x) *
                        (button.touchRange.centerCoordinate.xCoordinate - event.x) +
                        (button.touchRange.centerCoordinate.yCoordinate - event.y) *
                        (button.touchRange.centerCoordinate.yCoordinate - event.y)
            )
        }
        return 0F
    }

    private fun isInButton(button: Button, event: MotionEvent?): Boolean {
        return calculateDistant(button, event) <= button.touchRange.distant
    }

    fun clearPattern() {
        pattern.clear()
    }
}