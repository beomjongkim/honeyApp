package com.dmonster.darling.honey.customview

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import androidx.annotation.RequiresApi

class ClickableImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init()

    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        this.setOnTouchListener { v, event ->

            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    v.scaleX = 1.2f
                    v.scaleY = 1.2f
                }
                MotionEvent.ACTION_UP -> {
                    v.scaleX = 1f
                    v.scaleY = 1f
                    v.performClick()
                }
            }
            false
        }
    }
}