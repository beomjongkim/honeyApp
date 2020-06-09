package com.dmonster.darling.honey.dialog

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import java.util.*

class SelectorAdapter(val type: String?): androidx.recyclerview.widget.RecyclerView.Adapter<SelectorAdapter.ViewHolder>() {

    var data = arrayListOf<String>()
    var itemClick: View.OnClickListener? = null

    private var selectArray: ArrayList<String>? = null
    private var isSelectedBtn: BooleanArray? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_selector, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setSelectedClear() {
        for(i in 0 until data.size) {
            isSelectedBtn?.set(i, false)
        }
        selectArray?.clear()
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var selectText: TextView = view.findViewById(R.id.tv_item_selector_check)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(selectArray == null) {
            selectArray = ArrayList<String>()
        }

        if(isSelectedBtn == null) {
            isSelectedBtn = BooleanArray(data.size)
        }

        holder.view.tag = position
        holder.view.setOnClickListener(itemClick)
        val items = data[position]

        isSelectedBtn?.let { holder.selectText.isSelected = it[position] }
        holder.selectText.text = items
        if(position == 0) {
            holder.selectText.tag = position
            holder.selectText.setOnClickListener(itemClick)
        }
        else {
            holder.selectText.setOnClickListener {
                selectArray?.let { it1 ->
                    holder.view.context?.let { it2 ->
                        if(holder.selectText.isSelected) {
                            holder.selectText.isSelected = false
                            for(i in it1.indices) {
                                if(it1[i] == items) {
                                    it1.removeAt(i)
                                    return@let
                                }
                            }
                        }
                        else {
                            when(type) {
                                AppKeyValue.instance.searchTypeArea -> {
                                    if(it1.size < 3) {
                                        holder.selectText.isSelected = true
                                        it1.add(items)
                                    }
                                    else {
                                        Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_area_limit))
                                    }
                                }

                                AppKeyValue.instance.searchTypeGender -> {
                                    if(it1.size < 1) {
                                        holder.selectText.isSelected = true
                                        it1.add(items)
                                    }
                                    else {
                                        Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_gender_limit))
                                    }
                                }

                                AppKeyValue.instance.searchTypeAge -> {
                                    if(it1.size < 4) {
                                        holder.selectText.isSelected = true
                                        it1.add(items)
                                    }
                                    else {
                                        Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_age_limit))
                                    }
                                }
                            }
                        }
                    }
                    it1.sort()
                }
            }
        }
    }

    fun getSelectArray(): ArrayList<String>? {
        return selectArray
    }

}
