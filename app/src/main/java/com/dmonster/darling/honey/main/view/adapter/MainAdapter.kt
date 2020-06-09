package com.dmonster.darling.honey.main.view.adapter

import android.content.res.ColorStateList
import android.location.Location
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.main.data.MainListData
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.GpsManager
import com.dmonster.darling.honey.util.Utility
import de.hdodenhof.circleimageview.CircleImageView

class MainAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var data = arrayListOf<MainListData>()
    var itemClick: View.OnClickListener? = null
    var talkClick: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var profileImg: CircleImageView = view.findViewById(R.id.civ_item_main_image)
        var profileImgCnt : TextView = view.findViewById(R.id.tv_item_main_pic_size)
        var talkLayout: LinearLayout = view.findViewById(R.id.ll_item_main_talk)
        var marryText: TextView = view.findViewById(R.id.tv_item_main_marry)
        var talkIdText: TextView = view.findViewById(R.id.tv_item_main_talk_id)
        var introduceText: TextView = view.findViewById(R.id.tv_item_main_introduce)
        var nameText: TextView = view.findViewById(R.id.tv_item_main_name)
        var ageText: TextView = view.findViewById(R.id.tv_item_main_age)
        var areaText: TextView = view.findViewById(R.id.tv_item_main_area)
        var zodiacText: TextView = view.findViewById(R.id.tv_item_main_zodiac)
        var imageCountText: TextView = view.findViewById(R.id.tv_item_main_image_count)
        var talkText: TextView = view.findViewById(R.id.tv_item_main_talk)
        var distanceText: TextView = view.findViewById(R.id.tv_item_main_distance)
        var talkImage: ImageView = view.findViewById(R.id.iv_item_main_talk)
//        var topLayout: RelativeLayout = view.findViewById(R.id.rl_item_main_top)
//        var bottomLayout: RelativeLayout = view.findViewById(R.id.rl_item_main_bottom)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)

        holder.talkText.tag = position
        holder.talkText.setOnClickListener(talkClick)

        val items = data[position]

        holder.view.context?.let {
            items.apply {
                val profileImg = mbImgThumb
                if (!TextUtils.isEmpty(profileImg)) {
                    Glide.with(holder.view.context).load(profileImg).into(holder.profileImg)
                    //성별에 따라 이미지 프로필 보더라인 색 정의
                    if (mbSex == "M")
                        holder.profileImg.borderColor = ContextCompat.getColor(
                            holder.view.context,
                            R.color.color_profile_border_male
                        )
                    else if (mbSex == "F")
                        holder.profileImg.borderColor = ContextCompat.getColor(
                            holder.view.context,
                            R.color.color_profile_border_female
                        )

                }
                mbImgCnt?.let {
                    holder.profileImgCnt.text = it
                    if(it.toInt() >0){
                        holder.profileImgCnt.visibility = View.VISIBLE
                    }else{
                        holder.profileImgCnt.visibility = View.GONE
                    }
                }

                holder.marryText.text = mbChar
                holder.talkIdText.text = mbNick
                holder.introduceText.text = mbProfile


                mbProfileColor?.let { it1 ->
                    if (!TextUtils.isEmpty(it1)) {
                        val colorArray = arrayOf(
                            R.color.color_profile_select01,
                            R.color.color_profile_select02,
                            R.color.color_profile_select03,
                            R.color.color_profile_select04,
                            R.color.color_profile_select05,
                            R.color.color_profile_select06,
                            R.color.color_profile_select07,
                            R.color.color_profile_select08,
                            R.color.color_profile_select09,
                            R.color.color_profile_select10,
                            R.color.color_profile_select11
                        )
                        if (it1.toInt() < 11) {
                            holder.introduceText.setTextColor(
                                ContextCompat.getColor(
                                    it,
                                    colorArray[it1.toInt()]
                                )
                            )
                        }
                    }
                }
                holder.nameText.text = mbName
                holder.ageText.text = mbAge + "/"
                holder.areaText.text = mbAddr1
                holder.zodiacText.text = mbAnimal
                holder.imageCountText.text =
                    String.format(it.resources.getString(R.string.main_item_image), mbImgCnt)

                if (!mb_lat.isNullOrBlank() && !mb_lng.isNullOrBlank()) {

                    val locationUser = GpsManager.instance.getLocation(it)
                    val locationOther = Location("Other")
                    if (mb_lat?.isNotBlank()!! && mb_lng?.isNotBlank()!!) {
                        locationOther.latitude = mb_lat?.toDoubleOrNull()!!
                        locationOther.longitude = mb_lng?.toDoubleOrNull()!!
                    }
                    locationUser?.let {
                        val distance = GpsManager.instance.calDistance(
                            it.latitude,
                            it.longitude,
                            locationOther.latitude,
                            locationOther.longitude
                        ).div(1000)
                        holder.distanceText.text = String.format("%.2f", distance) + " km"

                    }
                } else {
                    holder.distanceText.text = ""
                }

                if (mbSex == "F") {
//                    holder.topLayout.setBackgroundResource(R.drawable.item_main_top_female)
//                    holder.bottomLayout.setBackgroundResource(R.drawable.item_main_bottom_female)
//                    holder.talkText.setBackgroundResource(R.drawable.item_main_talk_female)
                    ContextCompat.getColor(it, R.color.color_gender_female).apply {
                        ViewCompat.setBackgroundTintList(
                            holder.talkLayout,
                            ColorStateList.valueOf(this)
                        )
                    }
                } else {
                    ContextCompat.getColor(it, R.color.color_gender_male).apply {
                        ViewCompat.setBackgroundTintList(
                            holder.talkLayout,
                            ColorStateList.valueOf(this)
                        )
                    }
                }

                if (mbChar == it.resources.getString(R.string.information_member_marry)) {
//                    ViewCompat.setBackgroundTintList(holder.marryText, ColorStateList.valueOf(ContextCompat.getColor(it,R.color.color_main_type_marry)))
                    holder.marryText.setTextColor(
                        ContextCompat.getColor(
                            it,
                            R.color.color_main_type_marry
                        )
                    )
                    holder.marryText.background =
                        ContextCompat.getDrawable(it, R.drawable.bg_border_round_marry)
                } else {
                    holder.marryText.setTextColor(
                        ContextCompat.getColor(
                            it,
                            R.color.color_main_type_remarry
                        )
                    )
                    holder.marryText.background =
                        ContextCompat.getDrawable(it, R.drawable.bg_border_round_remarry)
                }

                val id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID)
                if (mbId == id) {
                    holder.talkText.text = it.resources.getString(R.string.main_item_edit)
                    holder.talkImage.visibility = View.GONE
                } else {
                    holder.talkText.text = it.resources.getString(R.string.main_item_talk)
                    holder.talkImage.visibility = View.VISIBLE
                }
            }
        }
    }

}
