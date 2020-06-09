package com.dmonster.darling.honey.notice.view.adapter

import android.animation.ValueAnimator
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.notice.data.NoticeData
import java.text.SimpleDateFormat

class NoticeAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<NoticeAdapter.ViewHolder>() {

    var data = arrayListOf<NoticeData>()
    private var showPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_notice, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var titleLayout: RelativeLayout = view.findViewById(R.id.rl_item_notice_title)
        var titleTypeText: TextView = view.findViewById(R.id.tv_item_notice_title_type)
        var titleText: TextView = view.findViewById(R.id.tv_item_notice_title)
        var dateText: TextView = view.findViewById(R.id.tv_item_notice_date)
        var arrowImg: ImageView = view.findViewById(R.id.iv_item_notice_arrow)
        var contentLayout: LinearLayout = view.findViewById(R.id.ll_item_notice_content)
        var contentTypeText: TextView = view.findViewById(R.id.tv_item_notice_content_type)
        var contentTitleText: TextView = view.findViewById(R.id.tv_item_notice_content_title)
        var contentImg: ImageView = view.findViewById(R.id.iv_item_notice_image)
        var contentText: TextView = view.findViewById(R.id.tv_item_notice_content)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = data[position]

        holder.titleText.text = items.wrSubject
        holder.contentTitleText.text = items.wrSubject
        holder.contentText.text = items.wrContent

        val beforeSimpleDateFormat = SimpleDateFormat(holder.view.context.resources.getString(com.dmonster.darling.honey.R.string.board_date))
        val beforeDateFormat = beforeSimpleDateFormat.parse(items.wrDatetime)
        val afterSimpleDateFormat = SimpleDateFormat(holder.view.context.resources.getString(com.dmonster.darling.honey.R.string.board_date_format))
        val afterDateFormat = afterSimpleDateFormat.format(beforeDateFormat)
        holder.dateText.text = afterDateFormat

        val noticeImg = items.wrImg
        if(!TextUtils.isEmpty(noticeImg)) {
            Glide.with(holder.view.context).load(noticeImg).into(holder.contentImg)
        }

        if(showPosition == position) {
            holder.arrowImg.setBackgroundResource(R.drawable.notice_up_arrow_img)
            holder.contentLayout.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    holder.contentLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    changeVisibility(holder.contentLayout, true)
                }
            })
        }
        else {
            holder.arrowImg.setBackgroundResource(R.drawable.notice_down_arrow_img)
            holder.contentLayout.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    holder.contentLayout.viewTreeObserver.removeGlobalOnLayoutListener(this)
                    changeVisibility(holder.contentLayout, false)
                }
            })
        }

        holder.view.setOnClickListener {
            showPosition = if(showPosition == position) -1 else position
            notifyDataSetChanged()
        }
    }

    private fun changeVisibility(linearLayout: LinearLayout, isShow: Boolean) {
        val valueAnimator = if(isShow) ValueAnimator.ofInt(0, linearLayout.height) else ValueAnimator.ofInt(linearLayout.height, 0)
        valueAnimator.apply {
            duration = 500
            addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                linearLayout.apply {
                    layoutParams.height = value
                    requestLayout()
                    visibility = if(isShow) View.VISIBLE else View.GONE
                }
            }
            start()
        }
    }

}
