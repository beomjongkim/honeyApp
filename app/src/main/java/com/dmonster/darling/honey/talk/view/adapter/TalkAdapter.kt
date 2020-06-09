package com.dmonster.darling.honey.talk.view.adapter

import android.content.Context
import android.content.Intent
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
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.talk.view.TalkImageDetailActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class TalkAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<TalkAdapter.ViewHolder>() {

    var data = arrayListOf<TalkData>()
    var itemClick: View.OnClickListener? = null

    private var adapterPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_talk, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addItemData(talkData: TalkData) {
        data.add(talkData)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        data.removeAt(position)
        notifyDataSetChanged()
    }

    fun removeAllItem() {
        for (i in 0 until data.size) {
            data.removeAt(i)
        }
        notifyDataSetChanged()
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var meDateLayout: RelativeLayout = view.findViewById(R.id.rl_item_talk_me_date)
        var meDateText: TextView = view.findViewById(R.id.tv_item_talk_me_date)
        var meMsgLayout: LinearLayout = view.findViewById(R.id.ll_item_talk_me_message)
        var meMsgTimeText: TextView = view.findViewById(R.id.tv_item_talk_me_message_time)
        var meMsgText: TextView = view.findViewById(R.id.tv_item_talk_me_message_content)
        var mePictureLayout: LinearLayout = view.findViewById(R.id.ll_item_talk_me_picture)
        var mePictureTimeText: TextView = view.findViewById(R.id.tv_item_talk_me_picture_time)
        var mePictureImage: ImageView = view.findViewById(R.id.iv_item_talk_me_picture_content)
        var meMsgReadText: TextView = view.findViewById(R.id.tv_item_talk_me_read_msg)
        var meImgReadText: TextView = view.findViewById(R.id.tv_item_talk_me_read_img)

        var youDateLayout: RelativeLayout = view.findViewById(R.id.rl_item_talk_you_date)
        var youDateText: TextView = view.findViewById(R.id.tv_item_talk_you_date)
        var youMsgLayout: LinearLayout = view.findViewById(R.id.ll_item_talk_you_message)
        var youMsgProfileImage: CircleImageView =
            view.findViewById(R.id.civ_item_talk_you_message_profile)
        var youMsgTimeText: TextView = view.findViewById(R.id.tv_item_talk_you_message_time)
        var youMsgText: TextView = view.findViewById(R.id.tv_item_talk_you_message_content)
        var youPictureLayout: LinearLayout = view.findViewById(R.id.ll_item_talk_you_picture)
        var youPictureProfileImage: CircleImageView =
            view.findViewById(R.id.civ_item_talk_you_picture_profile)
        var youPictureTimeText: TextView = view.findViewById(R.id.tv_item_talk_you_picture_time)
        var youPictureImage: ImageView = view.findViewById(R.id.iv_item_talk_you_picture_content)
        var youMsgReadText: TextView = view.findViewById(R.id.tv_item_talk_you_read_msg)
        var youImgReadText: TextView = view.findViewById(R.id.tv_item_talk_you_read_img)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterPosition = position
        val items = data[position]

        holder.view.context?.let {
            val id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID)
            val talkType = items.chatType
            val sender = items.chatSender


            val dateArray =
                items.chatSendDate?.split(" ".toRegex())?.dropLastWhile { it1 -> it1.isEmpty() }
                    ?.toTypedArray()

            var dateStr = ""
            var timeStr = ""
            if (dateArray?.size == 2) {
                dateStr = dateArray?.get(0)
                timeStr = dateArray?.get(1)
            }


            val beforeSimpleDateFormat =
                SimpleDateFormat(
                    it.resources?.getString(R.string.utility_time_second),
                    Locale.getDefault()
                )
            val beforeDateFormat = timeStr.run { beforeSimpleDateFormat.parse(this) }
            val afterSimpleDateFormat =
                SimpleDateFormat(
                    it.resources?.getString(R.string.utility_time_minute_noon),
                    Locale.getDefault()
                )
            val afterDateFormat =
                beforeDateFormat.run { afterSimpleDateFormat.format(this) }.replace("오후", "PM")
                    .replace("오전", "AM")

            //상대방이 확인했는지 체크(현재 실시간으로 체크는 불가능)
            if (items.chatReadDate != AppKeyValue.instance.talkCheckTime) {
                holder.meImgReadText.visibility = View.GONE
                holder.meMsgReadText.visibility = View.GONE
                holder.youMsgReadText.visibility = View.GONE
                holder.youImgReadText.visibility = View.GONE
            } else {
                holder.meImgReadText.visibility = View.VISIBLE
                holder.meMsgReadText.visibility = View.VISIBLE
                holder.youMsgReadText.visibility = View.VISIBLE
                holder.youImgReadText.visibility = View.VISIBLE
            }

            if (talkType == it.resources.getString(R.string.talk_type_info)) {
                holder.meDateLayout.visibility = View.GONE
                holder.meMsgLayout.visibility = View.GONE
                holder.mePictureLayout.visibility = View.GONE

                holder.youDateLayout.visibility = View.VISIBLE
                holder.youMsgLayout.visibility = View.GONE
                holder.youPictureLayout.visibility = View.GONE

                holder.youDateText.text = items.chatMsg
            } else if (talkType == it.resources.getString(R.string.talk_type_logout)) {
                holder.meDateLayout.visibility = View.GONE
                holder.meMsgLayout.visibility = View.GONE
                holder.mePictureLayout.visibility = View.GONE

                holder.youDateLayout.visibility = View.VISIBLE
                holder.youMsgLayout.visibility = View.GONE
                holder.youPictureLayout.visibility = View.GONE

                holder.youDateText.text = items.chatMsg
            } else if (talkType == it.resources.getString(R.string.talk_type_message)) {
                if (sender == id) {
                    if (!TextUtils.isEmpty(items.chatImg)) {
                        holder.meDateLayout.visibility = View.GONE
                        holder.meMsgLayout.visibility = View.GONE
                        holder.mePictureLayout.visibility = View.VISIBLE

                        holder.youDateLayout.visibility = View.GONE
                        holder.youMsgLayout.visibility = View.GONE
                        holder.youPictureLayout.visibility = View.GONE

                        Glide.with(it).load(items.chatImg).into(holder.mePictureImage)
                        holder.mePictureTimeText.text = afterDateFormat
                    } else {
                        holder.meDateLayout.visibility = View.GONE
                        holder.meMsgLayout.visibility = View.VISIBLE
                        holder.mePictureLayout.visibility = View.GONE

                        holder.youDateLayout.visibility = View.GONE
                        holder.youMsgLayout.visibility = View.GONE
                        holder.youPictureLayout.visibility = View.GONE

                        holder.meMsgTimeText.text = afterDateFormat
                        holder.meMsgText.text = items.chatMsg
                    }

                    if (position == 0) {
                        dateCompare(
                            it,
                            items.chatSendDate,
                            items.chatSendDate,
                            holder.meDateLayout,
                            holder.meDateText
                        )
                    } else if (position > 0) {
                        dateCompare(
                            it,
                            items.chatSendDate,
                            data[position.minus(1)].chatSendDate,
                            holder.meDateLayout,
                            holder.meDateText
                        )
                    }
                } else {
                    if (!TextUtils.isEmpty(items.chatImg)) {
                        holder.meDateLayout.visibility = View.GONE
                        holder.meMsgLayout.visibility = View.GONE
                        holder.mePictureLayout.visibility = View.GONE

                        holder.youDateLayout.visibility = View.GONE
                        holder.youMsgLayout.visibility = View.GONE
                        holder.youPictureLayout.visibility = View.VISIBLE

                        Glide.with(it).load(items.mbImgThumb).into(holder.youPictureProfileImage)
                        Glide.with(it).load(items.chatImg).into(holder.youPictureImage)
                        holder.youPictureTimeText.text = afterDateFormat
                    } else {
                        holder.meDateLayout.visibility = View.GONE
                        holder.meMsgLayout.visibility = View.GONE
                        holder.mePictureLayout.visibility = View.GONE

                        holder.youDateLayout.visibility = View.GONE
                        holder.youMsgLayout.visibility = View.VISIBLE
                        holder.youPictureLayout.visibility = View.GONE

                        Glide.with(it).load(items.mbImgThumb).into(holder.youMsgProfileImage)
                        holder.youMsgText.text = items.chatMsg
                        holder.youMsgTimeText.text = afterDateFormat
                    }

                    if (position == 0) {
                        dateCompare(
                            it,
                            items.chatSendDate,
                            items.chatSendDate,
                            holder.youDateLayout,
                            holder.youDateText
                        )
                    } else if (position > 0) {
                        dateCompare(
                            it,
                            items.chatSendDate,
                            data[position.minus(1)].chatSendDate,
                            holder.youDateLayout,
                            holder.youDateText
                        )
                    }
                }
            } else if (talkType == it.resources.getString(R.string.talk_type_good)) {
                if (sender == id) {
                    holder.meDateLayout.visibility = View.GONE
                    holder.meMsgLayout.visibility = View.GONE
                    holder.mePictureLayout.visibility = View.VISIBLE

                    holder.youDateLayout.visibility = View.GONE
                    holder.youMsgLayout.visibility = View.GONE
                    holder.youPictureLayout.visibility = View.GONE

                    Glide.with(it).load(items.chatImg).into(holder.mePictureImage)
                    holder.mePictureTimeText.text = afterDateFormat

                    holder.meMsgTimeText.text = afterDateFormat
                    holder.meMsgText.text = items.chatMsg

                    if (position == 0) {
                        dateCompare(
                            it,
                            items.chatSendDate,
                            items.chatSendDate,
                            holder.meDateLayout,
                            holder.meDateText
                        )
                    } else if (position > 0) {
                        dateCompare(
                            it,
                            items.chatSendDate,
                            data[position.minus(1)].chatSendDate,
                            holder.meDateLayout,
                            holder.meDateText
                        )
                    }
                } else {
                    holder.meDateLayout.visibility = View.GONE
                    holder.meMsgLayout.visibility = View.GONE
                    holder.mePictureLayout.visibility = View.GONE

                    holder.youDateLayout.visibility = View.GONE
                    holder.youMsgLayout.visibility = View.GONE
                    holder.youPictureLayout.visibility = View.VISIBLE

                    Glide.with(it).load(items.mbImgThumb).into(holder.youPictureProfileImage)
                    Glide.with(it).load(items.chatImg).into(holder.youPictureImage)
                    holder.youPictureTimeText.text = afterDateFormat

                    Glide.with(it).load(items.mbImgThumb).into(holder.youMsgProfileImage)
                    holder.youMsgText.text = items.chatMsg
                    holder.youMsgTimeText.text = afterDateFormat

                    if (position == 0) {
                        dateCompare(
                            it,
                            items.chatSendDate,
                            items.chatSendDate,
                            holder.youDateLayout,
                            holder.youDateText
                        )
                    } else if (position > 0) {
                        dateCompare(
                            it,
                            items.chatSendDate,
                            data[position.minus(1)].chatSendDate,
                            holder.youDateLayout,
                            holder.youDateText
                        )
                    }
                }
            }

            holder.mePictureImage.setOnClickListener { it1 ->
                val intent = Intent(it, TalkImageDetailActivity::class.java)
                intent.putExtra(AppKeyValue.instance.talkImage, items.chatImg)
                it.startActivity(intent)
            }

            holder.youPictureImage.setOnClickListener { it1 ->
                val intent = Intent(it, TalkImageDetailActivity::class.java)
                intent.putExtra(AppKeyValue.instance.talkImage, items.chatImg)
                it.startActivity(intent)
            }

            holder.youMsgProfileImage.setOnClickListener(itemClick)
            holder.youPictureProfileImage.setOnClickListener(itemClick)
        }


    }

    private fun dateCompare(
        context: Context?,
        dateStr1: String?,
        dateStr2: String?,
        dateLayout: RelativeLayout,
        dateText: TextView
    ) {
        if ((dateStr1 != "") && (dateStr2 != "")) {
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
                if (adapterPosition == 0 || it < 0) {
                    dateLayout.visibility = View.VISIBLE
                    dateText.text = afterDateFormat01
                } else {
                    dateLayout.visibility = View.GONE
                }
            }
        }
    }

}
