package com.dmonster.darling.honey.agreement.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.agreement.data.UseData

class UseAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<UseAdapter.ViewHolder>() {

    var data = arrayListOf<UseData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_use, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        var titleText: TextView = view.findViewById(R.id.tv_item_use_title)
        var contentText: TextView = view.findViewById(R.id.tv_item_use_content)
        var useImg: ImageView = view.findViewById(R.id.iv_item_use_image)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = data[position]

        holder.view.context?.let {
            items.apply {
                holder.titleText.text = coSubject
                holder.contentText.text = coContent

                val useImg = coFile
                if (!TextUtils.isEmpty(useImg)) {

                        Glide.with(it).load(useImg).apply(RequestOptions().fitCenter())
                            .thumbnail(0.5f).into(holder.useImg)
                }
            }
        }
    }

}
