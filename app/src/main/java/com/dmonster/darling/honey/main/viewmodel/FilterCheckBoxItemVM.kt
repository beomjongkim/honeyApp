package com.dmonster.darling.honey.main.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM

class FilterCheckBoxItemVM(override val title : String) : RecyclerViewItemVM(title) {
    val isChecked = MutableLiveData<Boolean>().also { it.value=false }
}