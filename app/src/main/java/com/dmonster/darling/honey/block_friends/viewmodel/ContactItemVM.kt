package com.dmonster.darling.honey.block_friends.viewmodel

import androidx.lifecycle.MutableLiveData
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerViewItemVM

class ContactItemVM(var nick : String? , var phoneNumber : String?, var id : String?, var urlPhoto : String? = null ) : RecyclerViewItemVM(nick){
    var isChecked  = MutableLiveData<Boolean>().also {
        it.value = true
    }
}