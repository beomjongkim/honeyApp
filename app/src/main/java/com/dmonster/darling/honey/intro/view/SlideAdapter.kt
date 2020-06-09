package com.dmonster.darling.honey.intro.view

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import kotlinx.android.synthetic.main.item_slide.view.*
import pyxis.uzuki.live.rollingbanner.RollingViewPagerAdapter


abstract class SlideAdapter(context: Context, itemList: ArrayList<Int>) :
    RollingViewPagerAdapter<Int>(context, itemList) {

    override fun getView(position: Int, item: Int): View {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_slide, null, false)
        view.fl_item_slide.setOnTouchListener { v, event ->
            if (position == itemList.size - 1) {
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        view.alpha = 0.9f
                    }
                    MotionEvent.ACTION_UP -> {
                        onTouchLast()
                        view.alpha = 1f
                    }
                }
            } else {
                when (event.actionMasked) {
                    MotionEvent.ACTION_UP -> {
                        onTouch()
                    }
                }
            }
            true
        }

        Glide.with(context).load(item).apply(RequestOptions().centerCrop())
            .into(view.iv_item_slide)
        return view
    }

    abstract fun onTouchLast()
    abstract fun onTouch()
}