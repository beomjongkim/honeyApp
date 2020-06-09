package com.dmonster.darling.honey.main.viewmodel

import android.location.Location
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerVM
import com.dmonster.darling.honey.util.GpsManager
import com.dmonster.darling.honey.util.Utility

abstract class FilterAreaVM(val strings: Array<String>?, val customAdapter: CustomAdapter) :
    RecyclerVM(customAdapter) {
    var id: String? = null
    var radioDistanceChecked = MutableLiveData<Boolean>().also { it.value = false }
    var latitude: String = ""
    var longitude: String = ""

    init {
        if (strings != null) {
            for (i in strings.indices) {

                customAdapter.dataList.add(
                    RecyclerItemData(
                        0,
                        FilterCheckBoxItemVM(strings[i]),
                        BR.itemVM
                    )
                )

            }
        }
    }

    fun getSeletedArea(): String {
        val dataList = customAdapter.dataList
        var selectedArea = ""
        for (i in dataList.indices) {
            val isChecked = (dataList[i].viewItemVM as FilterCheckBoxItemVM).isChecked.value
            if (isChecked!!) {
                if (i == 0)
                    selectedArea += dataList[i].viewItemVM.title
                else
                    selectedArea += "," + dataList[i].viewItemVM.title
            }
        }
        return selectedArea
    }

    fun onClickInit(view: View) {
        adapter.dataList.clear()
        if (strings != null) {
            for (i in strings.indices) {
                if (i != 0) {
                    adapter.dataList.add(
                        RecyclerItemData(
                            0,
                            FilterCheckBoxItemVM(strings[i]),
                            BR.itemVM
                        )
                    )
                }
            }
        }
        adapter.notifyDataSetChanged()
        afterClickInit()
    }

    abstract fun onClickSearch()
    abstract fun afterClickInit()
    abstract fun onClickBack()

    fun onClickArrangeDistance(view: View) {
        val context = view.context
        val location = GpsManager.instance.getLocation(context)
        location?.let {
            latitude = location.latitude.toString()
            longitude = location.longitude.toString()
        }
    }

    fun onClickAddress(view: View) {
        latitude = ""
        longitude = ""
    }
}