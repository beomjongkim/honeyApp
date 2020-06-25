package com.dmonster.darling.honey.point.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.common.command.TwoBtnSwitch
import com.dmonster.darling.honey.customview.CustomDialogInterface

class ReservePaymentPopupVM()  : ViewModel() {

    var name = MutableLiveData<String>().also {
        it.value = ""
    }
    var price : Int = 0
    lateinit var twoBtnSwitch: CustomDialogInterface
    var won = MutableLiveData<String>().also {
        it.value  =  price.toString() + "원"
    }

    var isChecked = MutableLiveData<Boolean>().also{
        it.value = false
    }

}