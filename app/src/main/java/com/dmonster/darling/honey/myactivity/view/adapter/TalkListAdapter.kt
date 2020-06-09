package com.dmonster.darling.honey.myactivity.view.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.myactivity.data.TalkListData
import com.dmonster.darling.honey.util.Utility
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class TalkListAdapter(val context: Context?): androidx.recyclerview.widget.RecyclerView.Adapter<TalkListAdapter.ViewHolder>() {

    var data = arrayListOf<TalkListData>()
    var itemClick: View.OnClickListener? = null
    var checkArray: ArrayList<Int>? = null

    private var adapterPosition: Int = 0
    private var isEdit: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_my_act_talk, null)
        if(checkArray == null) {
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

    fun setEdit(isSelected: Boolean) {
        isEdit = isSelected
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
        var dateLayout: LinearLayout = view.findViewById(R.id.ll_item_my_talk_date)
        var dateText: TextView = view.findViewById(R.id.tv_item_my_talk_date)
        var selectCheck: CheckBox = view.findViewById(R.id.cb_item_my_talk_check)
        var profileImg: CircleImageView = view.findViewById(R.id.civ_item_my_talk_image)
        var talkIdText: TextView = view.findViewById(R.id.tv_item_my_talk_talk_id)
        var ageText: TextView = view.findViewById(R.id.tv_item_my_talk_age)
        var timeText: TextView = view.findViewById(R.id.tv_item_my_talk_time)
        var messageText: TextView = view.findViewById(R.id.tv_item_my_talk_message)
        var messageNotRead : TextView = view.findViewById(R.id.tv_item_my_talk_notRead)
        var sendArrow : ImageView = view.findViewById(R.id.iv_item_my_talk_arrow_send)
        var receiveArrow : ImageView = view.findViewById(R.id.iv_item_my_talk_arrow_receive)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        adapterPosition = position
        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)
        val items = data[position]

        if(items.notRead?.toInt()!! > 0){
            holder.messageNotRead.visibility = View.VISIBLE
            holder.messageNotRead.text = items.notRead
        }else{
            holder.messageNotRead.visibility = View.GONE
        }

//        if(isEdit) {
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
                        checkArray?.indices?.let { it1 ->
                            for(i in it1) {
                                if(checkArray?.get(i) == position) {
                                    checkArray?.removeAt(i)
                                    return@let
                                }
                            }
                        }
                    }
                }
            }
//        }

        var dateStr = ""
        var timeStr = ""
        val dateArray = items.chatSendDate?.split(" ".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
        if (dateArray != null) {
            if(dateArray.size ==2){
                 dateStr = dateArray[0]
                 timeStr = dateArray[1]
            }
        }

        if(position == 0) {
            dateCompare(items.chatSendDate, items.chatSendDate, holder.dateLayout, holder.dateText)
        }
        else if(position > 0) {
            dateCompare(items.chatSendDate, data[position.minus(1)].chatSendDate, holder.dateLayout, holder.dateText)
        }


        when(items.chatType){
            "카드보내기" -> {
                holder.messageText.text = "관심있어요~"
            }
            else ->  holder.messageText.text = items.chatMsg
        }

        if(items.chatSender != items.mbId){//메세지를 보낸 사람과, 현재 회원이 대화하고 있는 상대 회원의 아이디가 다른 경우 -> 해당 회원 메세지를 보낸 경우
            holder.sendArrow.visibility = View.VISIBLE
            holder.receiveArrow.visibility = View.GONE
        }else{//해당 회원이 메세지를 받은 경우
            holder.receiveArrow.visibility = View.VISIBLE
            holder.sendArrow.visibility = View.GONE
        }


        val profileImg = items.mbImgThumb
        if(!TextUtils.isEmpty(profileImg)) {
            Glide.with(holder.view.context).load(profileImg).into(holder.profileImg)
        }


        holder.talkIdText.text = items.mbNick
        if(!TextUtils.isEmpty(items.mbAge)) {
            holder.ageText.text = context?.resources?.getString(R.string.bracket01)?.let { String.format(it, items.mbAge) }
        }

        if(timeStr != ""){
            val beforeSimpleDateFormat = SimpleDateFormat(context?.resources?.getString(R.string.utility_time_second), java.util.Locale.getDefault())
            val beforeDateFormat = timeStr.run { beforeSimpleDateFormat.parse(this) }
            val afterSimpleDateFormat = SimpleDateFormat(context?.resources?.getString(R.string.utility_time_format_minute), java.util.Locale.getDefault())
            val afterDateFormat = beforeDateFormat.run { afterSimpleDateFormat.format(this) }
            holder.timeText.text = afterDateFormat
        }



    }

    private fun dateCompare(dateStr1: String?, dateStr2: String?, dateLayout: LinearLayout, dateText: TextView) {
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
