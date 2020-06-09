package com.dmonster.darling.honey.myactivity.view.adapter

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.myactivity.data.ProfileData
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ProfileAdapter(val context: Context?) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    var data = arrayListOf<ProfileData>()
    var itemClick: View.OnClickListener? = null
    var itemTalkClick: View.OnClickListener? = null
    var checkArray: ArrayList<Int>? = null
    var isHide = false

    private var adapterPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_my_act_profile, null)
        if (checkArray == null) {
            checkArray = ArrayList()
        }

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getPosition(): Int {
        return adapterPosition
    }

    fun removeItem() {
        checkArray?.size?.let {
            for (i in it.minus(1) downTo 0) {
                val position = checkArray?.get(i)
                position?.let { it1 -> data.removeAt(it1) }
            }
        }

        checkArray?.clear()
        notifyDataSetChanged()
    }


    fun setAllChecked(isChecked: Boolean) {
        for (i in 0 until data.size) {
            checkArray?.add(i)
            data[i].isChecked = isChecked
        }
        if (!isChecked) {
            checkArray?.clear()
        }
        notifyDataSetChanged()
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var dateLayout: LinearLayout = view.findViewById(R.id.ll_item_profile_date)
        var dateText: TextView = view.findViewById(R.id.tv_item_profile_date)
        var selectCheck: CheckBox = view.findViewById(R.id.cb_item_profile_check)
        var profileImg: CircleImageView = view.findViewById(R.id.civ_item_profile_image)
        var talkIdText: TextView = view.findViewById(R.id.tv_item_profile_talk_id)
        var areaAgeText: TextView = view.findViewById(R.id.tv_item_profile_area_age)
        var timeText: TextView = view.findViewById(R.id.tv_item_profile_time)
        var visitBtn: TextView = view.findViewById(R.id.tv_item_profile_visit_num)
        var openText: TextView = view.findViewById(R.id.tv_item_profile_open)
        var visitList: RecyclerView = view.findViewById(R.id.rv_item_profile)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterPosition = position
        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)
        holder.visitBtn.tag = position

        holder.visitBtn.setOnClickListener(View.OnClickListener {
            if (holder.visitList.visibility == View.VISIBLE)
                holder.visitList.visibility = View.GONE
            else
                holder.visitList.visibility = View.VISIBLE
        })
        val items = data[position]

        if (!isHide) {
            holder.visitBtn.visibility = View.VISIBLE
            holder.openText.visibility = View.VISIBLE

            var innerAdapter = ProfileVisitAdapter()
            var visitLogList = items.visitLog?.split(",")?.asReversed()


            visitLogList?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val dateTimeFormatter: DateTimeFormatter =
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val result = visitLogList.sortedByDescending {
                        LocalDate.parse(it, dateTimeFormatter)
                    }
                    innerAdapter.data.addAll(result)
                } else {
                    innerAdapter.data.addAll(visitLogList)

                }

            }

            holder.visitList.let {
                it.setHasFixedSize(true)
                it.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                it.adapter = innerAdapter
            }
        }
        holder.visitBtn.text = "방문횟수 " + items.visitNum + "회"
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

        holder.selectCheck.apply {
            visibility = View.VISIBLE
            isChecked = items.isChecked
            setOnCheckedChangeListener(null)
            setOnClickListener {
                if (this.isChecked) {
                    items.isChecked = true
                    checkArray?.add(position)
                } else {
                    items.isChecked = false
                    checkArray?.indices?.let { it1 ->
                        for (i in it1) {
                            if (checkArray?.get(i) == position) {
                                checkArray?.removeAt(i)
                                return@let
                            }
                        }
                    }
                }
            }
        }

        val dateArray =
            items.regDate?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        val dateStr = dateArray?.get(0)
        val timeStr = dateArray?.get(1)
        if (position == 0) {
            dateCompare(items.regDate, items.regDate, holder.dateLayout, holder.dateText)
        } else if (position > 0) {
            dateCompare(
                items.regDate,
                data[position.minus(1)].regDate,
                holder.dateLayout,
                holder.dateText
            )
        }

        val profileImg = items.mbImgThumb
        if (!TextUtils.isEmpty(profileImg)) {
            Glide.with(holder.view.context).load(profileImg).into(holder.profileImg)
        }


        holder.talkIdText.text = items.mbNick
        holder.areaAgeText.text = items.mbAge

        val beforeSimpleDateFormat = SimpleDateFormat(
            context?.resources?.getString(R.string.utility_time_second),
            java.util.Locale.getDefault()
        )
        val beforeDateFormat = timeStr.run { beforeSimpleDateFormat.parse(this) }
        val afterSimpleDateFormat = SimpleDateFormat(
            context?.resources?.getString(R.string.utility_time_format_minute),
            java.util.Locale.getDefault()
        )
        val afterDateFormat = beforeDateFormat.run { afterSimpleDateFormat.format(this) }
        holder.timeText.text = afterDateFormat

        context?.let {
            holder.openText.apply {
                val top = paddingTop
                val bottom = paddingBottom
                val left = paddingLeft
                val right = paddingRight
                if (items.type == "detail") {
                    text = it.resources?.getString(R.string.my_activity_profile_open_all)
                    setBackgroundResource(R.drawable.bg_btn_salmon)
                } else {
                    text = it.resources?.getString(R.string.my_activity_profile_open_basic)
                    setBackgroundResource(R.drawable.bg_btn_golden_yellow)
                }
                setPadding(left, top, right, bottom)
            }
        }
    }

    private fun dateCompare(
        dateStr1: String?,
        dateStr2: String?,
        dateLayout: LinearLayout,
        dateText: TextView
    ) {
        val beforeSimpleDateFormat = SimpleDateFormat(
            context?.resources?.getString(R.string.utility_date_time_second),
            java.util.Locale.getDefault()
        )
        val beforeDateFormat01 = dateStr1.run { beforeSimpleDateFormat.parse(this) }
        val beforeDateFormat02 = dateStr2.run { beforeSimpleDateFormat.parse(this) }

        val afterSimpleDateFormat = SimpleDateFormat(
            context?.resources?.getString(R.string.utility_date_format_day),
            java.util.Locale.getDefault()
        )
        val afterDateFormat01 = beforeDateFormat01.run { afterSimpleDateFormat.format(this) }
        val afterDateFormat02 = beforeDateFormat02.run { afterSimpleDateFormat.format(this) }

        afterDateFormat02.compareTo(afterDateFormat01).let {
            if (adapterPosition == 0 || it > 0) {
                dateLayout.visibility = View.VISIBLE
                dateText.text = afterDateFormat01
            } else {
                dateLayout.visibility = View.GONE
            }
        }
    }

}
