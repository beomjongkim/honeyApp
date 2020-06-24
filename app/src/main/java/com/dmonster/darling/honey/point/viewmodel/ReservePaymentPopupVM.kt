package com.dmonster.darling.honey.point.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.common.command.TwoBtnSwitch

class ReservePaymentPopupVM(var twoBtnSwitch: TwoBtnSwitch)  : ViewModel() {

    var name = MutableLiveData<String>().also {
        it.value = ""
    }
    var price= MutableLiveData<Int>().also {
        it.value = 0
    }

    var won :String = price.value.toString() + "원"

}