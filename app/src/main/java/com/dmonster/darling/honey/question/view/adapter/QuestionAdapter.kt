package com.dmonster.darling.honey.question.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.question.data.QuestionData
import java.text.SimpleDateFormat

class QuestionAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    var data = arrayListOf<QuestionData>()
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

        items.apply {
            holder.titleText.text = faSubject
            holder.contentTitleText.text = faSubject
            holder.contentText.text = faContent
        }

        if(showPosition == position) {
            holder.contentLayout.visibility = View.VISIBLE
            holder.arrowImg.setBackgroundResource(R.drawable.notice_up_arrow_img)
        }
        else {
            holder.contentLayout.visibility = View.GONE
            holder.arrowImg.setBackgroundResource(R.drawable.notice_down_arrow_img)
        }

        holder.view.setOnClickListener {
            showPosition = if(showPosition == position) {
                -1
            } else {
                position
            }
            notifyDataSetChanged()
        }
    }

}
