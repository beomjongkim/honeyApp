package com.dmonster.darling.honey.myactivity.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.widget.*
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.myactivity.data.MemberData
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat

class NewMemberAdapter(val context: Context?): androidx.recyclerview.widget.RecyclerView.Adapter<NewMemberAdapter.ViewHolder>() {

    var data = arrayListOf<MemberData>()
    var itemClick: View.OnClickListener? = null
    var itemGoodClick: View.OnClickListener? = null
    var itemTalkClick: View.OnClickListener? = null

    private var adapterPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_my_act_new_member, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getPosition(): Int {
        return adapterPosition
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var dateLayout: RelativeLayout = view.findViewById(R.id.rl_item_new_member_date)
        var dateText: TextView = view.findViewById(R.id.tv_item_new_member_date)
        var profileImg: CircleImageView = view.findViewById(R.id.civ_item_new_member_image)
        var marryText: TextView = view.findViewById(R.id.tv_item_new_member_marry)
        var talkIdText: TextView = view.findViewById(R.id.tv_item_new_member_talk_id)
        var areaAgeText: TextView = view.findViewById(R.id.tv_item_new_member_area_age)
        var timeText: TextView = view.findViewById(R.id.tv_item_new_member_time)
        var goodBtn: LinearLayout = view.findViewById(R.id.btn_item_new_member_good)
        var talkBtn: LinearLayout = view.findViewById(R.id.btn_item_new_member_talk)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterPosition = position
        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)
        holder.talkBtn.tag = position
        holder.talkBtn.setOnClickListener(itemTalkClick)
        holder.goodBtn.tag = position
        holder.goodBtn.setOnClickListener(itemGoodClick)
        val items = data[position]

        /*    톡하기 애니메이션    */
        /*val aniLogo = ScaleAnimation(1.0f, 0.99f, 1.0f, 0.99f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f)
        aniLogo.duration = 1000
        aniLogo.interpolator = OvershootInterpolator(150f)

        aniLogo.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                holder.visitBtn.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                aniLogo.repeatCount = Animation.INFINITE
                aniLogo.repeatMode = Animation.RESTART
                aniLogo.startOffset = 3000
                aniLogo.start()
            }
        })
        holder.visitBtn.animation = aniLogo
        aniLogo.start()*/

        val dateArray = items.mbDateTime?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        val dateStr = dateArray?.get(0)
        val timeStr = dateArray?.get(1)
        if(position == 0) {
            dateCompare(items.mbDateTime, items.mbDateTime, holder.dateLayout, holder.dateText)
        }
        else if(position > 0) {
            dateCompare(items.mbDateTime, data[position.minus(1)].mbDateTime, holder.dateLayout, holder.dateText)
        }

        val profileImg = items.mbImgThumb
        if(!TextUtils.isEmpty(profileImg)) {
            Glide.with(holder.view.context).load(profileImg).into(holder.profileImg)
        }

//        if(items.mbSex == "F") {
//            holder.goodBtn.setBackgroundResource(R.drawable.btn_select_good)
//        }
//        else {
//            holder.goodBtn.setBackgroundResource(R.drawable.btn_select_interest)
//        }

        holder.view.context?.let {
            if(items.mbChar == it.resources.getString(R.string.information_member_marry)) {
                holder.marryText.background = ContextCompat.getDrawable(it,R.drawable.bg_round_btn_marry)
            }
            else {
                holder.marryText.background = ContextCompat.getDrawable(it,R.drawable.bg_round_btn_remarry)
            }
        }
        holder.marryText.text = items.mbChar

        holder.talkIdText.text = items.mbNick
        holder.areaAgeText.text = context?.resources?.getString(R.string.bracket02)?.let { String.format(it, items.mbAddr1, items.mbAge) }

        val beforeSimpleDateFormat = SimpleDateFormat(context?.resources?.getString(R.string.utility_time_second), java.util.Locale.getDefault())
        val beforeDateFormat = timeStr.run { beforeSimpleDateFormat.parse(this) }
        val afterSimpleDateFormat = SimpleDateFormat(context?.resources?.getString(R.string.utility_time_format_minute), java.util.Locale.getDefault())
        val afterDateFormat = beforeDateFormat.run { afterSimpleDateFormat.format(this) }
        holder.timeText.text = afterDateFormat
    }

    private fun dateCompare(dateStr1: String?, dateStr2: String?, dateLayout: RelativeLayout, dateText: TextView) {
        val beforeSimpleDateFormat = SimpleDateFormat(context?.resources?.getString(R.string.utility_date_time_second), java.util.Locale.getDefault())
        val beforeDateFormat01 = dateStr1.run { beforeSimpleDateFormat.parse(this) }
        val beforeDateFormat02 = dateStr2.run { beforeSimpleDateFormat.parse(this) }

        val afterSimpleDateFormat = SimpleDateFormat(context?.resources?.getString(R.string.utility_date_format_day), java.util.Locale.getDefault())
        val afterDateFormat01 = beforeDateFormat01.run { afterSimpleDateFormat.format(this) }
        val afterDateFormat02 = beforeDateFormat02.run { afterSimpleDateFormat.format(this) }

        afterDateFormat02.compareTo(afterDateFormat01).let {
            if(adapterPosition == 0 || it > 0) {
                dateLayout.visibility = View.VISIBLE
                dateText.text = afterDateFormat01
            }
            else {
                dateLayout.visibility = View.GONE
            }
        }
    }

}
