package com.dmonster.darling.honey.intro.view

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import kotlinx.android.synthetic.main.item_slide.view.*


abstract class SlideAdapter(var context: Context, var itemList: ArrayList<Int>) :
    PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
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
            }
            true
        }

        Glide.with(context).load(itemList.get(position)).apply(RequestOptions().centerCrop())
            .into(view.iv_item_slide)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == `object` as View)
    }

    abstract fun onTouchLast()
}