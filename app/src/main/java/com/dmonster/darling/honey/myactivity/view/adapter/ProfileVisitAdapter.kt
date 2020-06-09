package com.dmonster.darling.honey.myactivity.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dmonster.darling.honey.R
import com.facebook.internal.Utility
import kotlinx.android.synthetic.main.item_visit_log.view.*
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class ProfileVisitAdapter : RecyclerView.Adapter<ProfileVisitAdapter.ViewHolder>() {
    var data = arrayListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_visit_log, null)

        v.layoutParams = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var singleData = data[position]

        if (!singleData.isBlank()) {
            val splittedData = singleData.split(" ")
            if (splittedData.size == 2) {
                var listDateSplit = splittedData[0].split("-")
                if (listDateSplit.size == 3)
                    holder.date.text =
                        listDateSplit[0] + "년 " + listDateSplit[1] + "월 " + listDateSplit[2] + "일"
                var listTimeSplit = splittedData[1].split(":")
                if (listTimeSplit.size == 3) {
                    holder.time.text = listTimeSplit[0] + "시 " + listTimeSplit[1] + "분 "
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var time = view.tv_item_visit_log_time
        var date = view.tv_item_visit_log_date
    }
}
