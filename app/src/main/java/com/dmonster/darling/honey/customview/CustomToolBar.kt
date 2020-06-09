package com.dmonster.darling.honey.customview

import android.app.Activity
import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.Utility

class CustomToolBar: ConstraintLayout {

    private var view: View? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        this.removeAllViews()
        view = View.inflate(context, R.layout.custom_toolbar, null)
        val lp = RelativeLayout.LayoutParams(-1, -2)
        view?.layoutParams = lp

        val leftImg = view?.findViewById<View>(R.id.iv_custom_toolbar_left) as ImageView
        leftImg.setOnClickListener {
            try {
                (context as Activity).finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        this.addView(view)
    }

    fun getTitle(): TextView {
        return view?.findViewById<View>(R.id.tv_custom_toolbar_title) as TextView
    }

    fun setTitle(title: String): CustomToolBar {
        val titleText = view?.findViewById<View>(R.id.tv_custom_toolbar_title) as TextView
        titleText.text = title

        return this
    }

    fun setTitleSize(size: Float): CustomToolBar {
        val titleText = view?.findViewById<View>(R.id.tv_custom_toolbar_title) as TextView
        titleText.textSize = Utility.instance.getPx(context, size).toFloat()

        return this
    }

    fun getLeftBtn(): ImageView {
        return view?.findViewById<View>(R.id.iv_custom_toolbar_left) as ImageView
    }

    fun setLeftBtn(visible: Int): CustomToolBar {
        val leftImg = view?.findViewById<View>(R.id.iv_custom_toolbar_left) as ImageView
        leftImg.visibility = visible

        return this
    }

    fun getRightBtn(): ImageView {
        return view?.findViewById<View>(R.id.iv_custom_toolbar_right) as ImageView
    }

    fun setRightBtn(backgroundImg: Int): CustomToolBar {
        val rightImg = view?.findViewById<View>(R.id.iv_custom_toolbar_right) as ImageView
        rightImg.setBackgroundResource(backgroundImg)

        return this
    }

}
