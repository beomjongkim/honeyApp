package com.dmonster.darling.honey.point.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterPaymentPopupVM(var price : Int)  : ViewModel() {

    var name = MutableLiveData<String>().also {
        it.value = ""
    }

    var won :String = price.toString() + "원"

}