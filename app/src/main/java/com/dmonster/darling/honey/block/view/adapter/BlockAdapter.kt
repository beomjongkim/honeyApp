package com.dmonster.darling.honey.block.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.block.data.BlockData
import de.hdodenhof.circleimageview.CircleImageView

class BlockAdapter(): androidx.recyclerview.widget.RecyclerView.Adapter<BlockAdapter.ViewHolder>() {

    var data = arrayListOf<BlockData>()
    var itemClick: View.OnClickListener? = null
    var checkArray: ArrayList<Int>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_block, null)
        if(checkArray == null) {
            checkArray = ArrayList()
        }

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun removeItem() {
        checkArray?.size?.let {
            for(i in it.minus(1) downTo 0) {
                val position = checkArray?.get(i)
                position?.let { it1 -> data.removeAt(it1) }
            }
        }

        checkArray?.clear()
        notifyDataSetChanged()
    }

    fun removeAllItem() {
        for(i in 0 until data.size) {
            data.removeAt(i)
        }
        notifyDataSetChanged()
    }

    fun setAllChecked(isChecked: Boolean) {
        for(i in 0 until data.size) {
            checkArray?.add(i)
            data[i].isChecked = isChecked
        }
        if(!isChecked) {
            checkArray?.clear()
        }
        notifyDataSetChanged()
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var selectCheck: CheckBox = view.findViewById(R.id.cb_item_block_check)
        var profileImg: ImageView = view.findViewById(R.id.civ_item_block_image)
        var talkIdText: TextView = view.findViewById(R.id.tv_item_block_talk_id)
        var ageText: TextView = view.findViewById(R.id.tv_item_block_age)
        var introduceText: TextView = view.findViewById(R.id.tv_item_block_introduce)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)
        val items = data[position]

        holder.selectCheck.apply {
            visibility = View.VISIBLE
            isChecked = items.isChecked
            setOnCheckedChangeListener(null)
            setOnClickListener {
                if(this.isChecked) {
                    items.isChecked = true
                    checkArray?.add(position)
                }
                else {
                    items.isChecked = false
                    checkArray?.indices?.let {
                        for(i in it) {
                            if(checkArray?.get(i) == position) {
                                checkArray?.removeAt(i)
                                return@let
                            }
                        }
                    }
                }
            }
        }

        val profileImg = items.mbImgThumb
        if(!TextUtils.isEmpty(profileImg)) {
            Glide.with(holder.view.context).load(profileImg) .apply(RequestOptions().centerCrop().circleCrop())
                .thumbnail(0.5f).into(holder.profileImg)
        }

        holder.talkIdText.text = items.mbNick
        holder.ageText.text = items.mbAge

        holder.introduceText.text = items.mbProfile
    }

}
