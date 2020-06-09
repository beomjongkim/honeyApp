package com.dmonster.darling.honey.inquiry.view.adapter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.inquiry.data.InquiryData

class InquiryAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<InquiryAdapter.ViewHolder>() {

    var data = arrayListOf<InquiryData>()
    private var showPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_inquiry, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var titleLayout: LinearLayout = view.findViewById(R.id.ll_item_inquiry_title)
        var titleStateText: TextView = view.findViewById(R.id.tv_item_inquiry_title_state)
        var titleTypeText: TextView = view.findViewById(R.id.tv_item_inquiry_title_type)
        var titleText: TextView = view.findViewById(R.id.tv_item_inquiry_title)
        var contentLayout: LinearLayout = view.findViewById(R.id.ll_item_inquiry_content)
        var contentTitleText: TextView = view.findViewById(R.id.tv_item_inquiry_content_title)
        var contentImg: ImageView = view.findViewById(R.id.iv_item_inquiry_content_image)
        var contentText: TextView = view.findViewById(R.id.tv_item_inquiry_content)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = data[position]

        if(!TextUtils.isEmpty(items.replyContent)) {
            holder.titleStateText.apply {
                holder.view.context.let {
                    text = it.resources.getString(R.string.inquiry_breakdown_reply_complete)
                    setTextColor(it.getColor(R.color.color_main))
                }
                setBackgroundResource(R.drawable.dialog_cancel_btn)
            }
        }
        else {
            holder.titleStateText.apply {
                holder.view.context.let {
                    text = it.resources.getString(R.string.inquiry_breakdown_reply)
                }
                setBackgroundResource(R.drawable.join_btn)
            }
        }
        holder.titleTypeText.text = String.format(holder.view.context.getString(R.string.inquiry_breakdown_type), items.caName)
        holder.titleText.text = items.wrContent

        holder.contentTitleText.text = items.wrContent
        val noticeImg = items.wrImg
        if(!TextUtils.isEmpty(noticeImg)) {
            Glide.with(holder.view.context).load(noticeImg).into(holder.contentImg)
        }
        if(!TextUtils.isEmpty(items.replyContent)) {
            holder.contentText.visibility = View.VISIBLE
            holder.contentText.text = items.replyContent
        }
        else {
            holder.contentText.visibility = View.GONE
        }

        if(showPosition == position) {
            holder.contentLayout.visibility = View.VISIBLE
        }
        else {
            holder.contentLayout.visibility = View.GONE
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
