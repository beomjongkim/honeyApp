package com.dmonster.darling.honey.myinformation.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.block.data.BlockData
import com.dmonster.darling.honey.myinformation.data.RecommendData
import de.hdodenhof.circleimageview.CircleImageView

class RecommendAdapter(): androidx.recyclerview.widget.RecyclerView.Adapter<RecommendAdapter.ViewHolder>() {

    var data = arrayListOf<RecommendData>()
    var itemClick: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var profileImg: CircleImageView = view.findViewById(R.id.civ_item_recommend_image)
        var marryText: TextView = view.findViewById(R.id.tv_item_recommend_marry)
        var talkIdText: TextView = view.findViewById(R.id.tv_item_recommend_talk_id)
        var introduceText: TextView = view.findViewById(R.id.tv_item_recommend_introduce)
        var nameText: TextView = view.findViewById(R.id.tv_item_recommend_name)
        var ageText: TextView = view.findViewById(R.id.tv_item_recommend_age)
        var areaText: TextView = view.findViewById(R.id.tv_item_recommend_area)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)
        val items = data[position]

        val profileImg = items.mbImgThumb
        if(!TextUtils.isEmpty(profileImg)) {
            Glide.with(holder.view.context).load(profileImg).into(holder.profileImg)
        }
        holder.talkIdText.text = items.mbNick
        holder.introduceText.text = items.mbProfile
        holder.nameText.text = items.mbName
        holder.ageText.text = items.mbAge
        holder.areaText.text = items.mbAddr1

        holder.view.context?.let {
            if(items.mbChar == it.resources.getString(R.string.information_member_marry)) {
                holder.marryText.setBackgroundColor(it.resources.getColor(R.color.color_main_type_marry))
            }
            else {
                holder.marryText.setBackgroundColor(it.resources.getColor(R.color.color_main_type_remarry))
            }
        }
        holder.marryText.text = items.mbChar
    }

}
