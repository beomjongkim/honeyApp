package com.dmonster.darling.honey.myinformation.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.myinformation.data.PictureData
import java.util.ArrayList

class MyInfoPictureAdapter(private val itemHeight: Int): androidx.recyclerview.widget.RecyclerView.Adapter<MyInfoPictureAdapter.ViewHolder>() {

    var dataImg = arrayListOf<String>()
    var dataImgNo = arrayListOf<String>()
    var itemClick: View.OnClickListener? = null
    var deleteArray: ArrayList<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_picture, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataImg.size
    }

    fun addItem(pictureData: String) {
        dataImg.add(pictureData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        dataImg.removeAt(position)
        if(dataImgNo.size > position) {
            dataImgNo.removeAt(position)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var pictureImg: ImageView = view.findViewById(R.id.iv_item_picture_image)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(deleteArray == null) {
            deleteArray = ArrayList<String>()
        }

        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)

        val items = dataImg[position]
        holder.pictureImg.layoutParams.height = itemHeight
        Glide.with(holder.view.context).load(items).into(holder.pictureImg)
    }

}
