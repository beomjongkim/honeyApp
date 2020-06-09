package com.dmonster.darling.honey.profile.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.GlideApp
import com.github.mmin18.widget.RealtimeBlurView
import jp.wasabeef.glide.transformations.BlurTransformation

class ProfilePictureAdapter(private val itemHeight: Int): androidx.recyclerview.widget.RecyclerView.Adapter<ProfilePictureAdapter.ViewHolder>() {

    var dataImg = arrayListOf<String>()
    var dataThumb = arrayListOf<String>()
    var itemClick: View.OnClickListener? = null

    private var isSetBlur: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_profile_picture, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return dataImg.size
    }

    fun setBlurView(isBlur: Boolean) {
        isSetBlur = isBlur
        notifyDataSetChanged()
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var pictureImg: ImageView = view.findViewById(R.id.iv_item_profile_picture_image)
        var blurImg: RealtimeBlurView = view.findViewById(R.id.rbv_item_profile_picture_image)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.setOnClickListener(itemClick)
        holder.view.tag = position

        val itemsThumb = dataThumb[position]
        holder.view.context?.let {
            holder.pictureImg.layoutParams.height = itemHeight
            Glide.with(it).load(itemsThumb).into(holder.pictureImg)
        }

        holder.blurImg.setBlurRadius(25f)
        holder.blurImg.setOverlayColor(R.color.color_gray)
        if(position > 0) {
            if(isSetBlur) {
                holder.blurImg.visibility = View.VISIBLE
            }
            else {
                holder.blurImg.visibility = View.GONE
            }
        }
    }

}
