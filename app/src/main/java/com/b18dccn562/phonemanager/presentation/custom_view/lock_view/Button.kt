package com.b18dccn562.phonemanager.presentation.custom_view.lock_view

import android.graphics.RectF
import android.view.MotionEvent

class Button(var number: Int = 0) {
    var touchRange = TouchRange()
    var innerRectF = RectF()
    var outerRectF = RectF()

    fun containTouchEvent(event: MotionEvent): Boolean {
        return innerRectF.left < innerRectF.right && innerRectF.top < innerRectF.bottom &&
                (innerRectF.left - innerRectF.width() * 5) <= event.x &&
                (innerRectF.right + innerRectF.width() * 5) >= event.x &&
                (innerRectF.top - innerRectF.height() * 5) <= event.y &&
                (innerRectF.bottom + innerRectF.height() * 5) >= event.y

    }
}