package com.dmonster.darling.honey.dialog

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import java.util.ArrayList

class SelectorMemberAdapter(val type: String?): androidx.recyclerview.widget.RecyclerView.Adapter<SelectorMemberAdapter.ViewHolder>() {

    var data = arrayListOf<String>()
    var dataSelected = arrayListOf<SelectorMemberData>()
    var itemClick: View.OnClickListener? = null

    private var selectArray: ArrayList<String>? = null
    private var adapterPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_selector, null)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun setSelectedClear(position: Int) {
        for(i in 0 until data.size) {
            dataSelected[i].isSelected = false
        }
        dataSelected[position].isSelected = true
        selectArray?.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(var view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        var selectText: TextView = view.findViewById(R.id.tv_item_selector_check)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(selectArray == null) {
            selectArray = ArrayList<String>()
        }

        val items = data[position]
        val itemSelected = dataSelected[position]

        holder.selectText.isSelected = itemSelected.isSelected
        holder.selectText.text = items
        holder.selectText.setOnClickListener {
            selectArray?.let { it1 ->
                holder.view.context?.let { it2 ->
                    if(holder.selectText.isSelected) {
                        holder.selectText.isSelected = false
                        itemSelected.isSelected = false
                        for(i in it1.indices) {
                            if(it1[i] == items) {
                                it1.removeAt(i)
                                return@let
                            }
                        }
                    }
                    else {
                        when(type) {
                            AppKeyValue.instance.memberTypeNotLimit -> {
                                setSelectedClear(position)
                                adapterPosition = position
                                holder.selectText.isSelected = true
                                items.let { it3 -> it1.add(it3) }
                            }

                            AppKeyValue.instance.memberTypeLimit -> {
                                if(it1.size < 3) {
                                    holder.selectText.isSelected = true
                                    itemSelected.isSelected = true
                                    items.let { it3 -> it1.add(it3) }
                                }
                                else {
                                    Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_member_limit))
                                }
                            }

                            AppKeyValue.instance.memberTypeAgeLimit -> {
                                if(it1.size < 4) {
                                    holder.selectText.isSelected = true
                                    itemSelected.isSelected = true
                                    items.let { it3 -> it1.add(it3) }
                                }
                                else {
                                    Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_member_age_limit))
                                }
                            }

                            AppKeyValue.instance.memberTypeIncomeLimit -> {
                                if(it1.size < 3) {
                                    holder.selectText.isSelected = true
                                    itemSelected.isSelected = true
                                    items.let { it3 -> it1.add(it3) }
                                }
                                else {
                                    Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_member_income_limit))
                                }
                            }

                            AppKeyValue.instance.memberTypeEducationLimit -> {
                                if(it1.size < 2) {
                                    holder.selectText.isSelected = true
                                    itemSelected.isSelected = true
                                    items.let { it3 -> it1.add(it3) }
                                }
                                else {
                                    Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_member_education_limit))
                                }
                            }

                            AppKeyValue.instance.memberTypeReligionLimit -> {
                                if(it1.size < 1) {
                                    holder.selectText.isSelected = true
                                    itemSelected.isSelected = true
                                    items.let { it3 -> it1.add(it3) }
                                }
                                else {
                                    Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_member_religion_limit))
                                }
                            }

                            AppKeyValue.instance.memberTypeBloodLimit -> {
                                if(it1.size < 1) {
                                    holder.selectText.isSelected = true
                                    itemSelected.isSelected = true
                                    items.let { it3 -> it1.add(it3) }
                                }
                                else {
                                    Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_member_blood_limit))
                                }
                            }

                            AppKeyValue.instance.memberTypeMyStyleLimit -> {
                                if(it1.size < 5) {
                                    holder.selectText.isSelected = true
                                    itemSelected.isSelected = true
                                    items.let { it3 -> it1.add(it3) }
                                }
                                else {
                                    Utility.instance.showToast(it2, it2.resources.getString(R.string.msg_popup_selector_member_mystyle_limit))
                                }
                            }
                        }
                    }
                }
                it1.sort()
            }
        }
    }

    fun getSelectArray(): ArrayList<String>? {
        return selectArray
    }

    fun getAdapterPosition(): Int {
        return adapterPosition
    }

}
