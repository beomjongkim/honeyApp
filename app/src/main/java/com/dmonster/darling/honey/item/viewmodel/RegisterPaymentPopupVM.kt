package com.dmonster.darling.honey.item.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterPaymentPopupVM(var price : Int)  : ViewModel() {

    var name = MutableLiveData<String>().also {
        it.value = ""
    }

}