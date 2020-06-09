package com.dmonster.darling.honey.main.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerVM
import com.dmonster.darling.honey.util.AppKeyValue

abstract class FilterAgeGenderVM(val strings: Array<String>?, val customAdapter: CustomAdapter) :
    RecyclerVM(customAdapter) {
    var radioMaleChecked = MutableLiveData<Boolean>().also { it.value = true }

    init {
        if (strings != null) {
            for (i in strings.indices) {
                if (i != 0) {
                    customAdapter.dataList.add(RecyclerItemData(0, FilterCheckBoxItemVM(strings[i]), BR.itemVM))
                }
            }
        }
    }

    fun getSelectedGender(): String {
        var gender = AppKeyValue.instance.keyYes
        radioMaleChecked.value?.let {
            if (it)
                gender = "M"
            else
                gender = "F"
        }

        return gender
    }

    fun getSelectedAge(): String {
        val dataList = customAdapter.dataList
        var selectedAge = ""
        for (i in dataList.indices) {
            val isChecked = (dataList[i].viewItemVM as FilterCheckBoxItemVM).isChecked.value
            if (isChecked!!) {
                if (i == 0)
                    selectedAge += dataList[i].viewItemVM.title
                else
                    selectedAge += "," + dataList[i].viewItemVM.title
            }
        }
        return selectedAge
    }

    fun onClickInit(view: View) {
        adapter.dataList.clear()
        if (strings != null) {
            for (i in strings.indices) {
                if (i != 0) {
                    adapter.dataList.add(RecyclerItemData(0, FilterCheckBoxItemVM(strings[i]), BR.itemVM))
                }
            }
        }
        adapter.notifyDataSetChanged()

        afterClickInit()
    }

    abstract fun onClickBack()
    abstract fun onClickSearch()
    abstract fun afterClickInit()

}