package com.dmonster.darling.honey.common.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.common.command.TwoBtnSwitch

class TwoBtnSwitchVM(var firstBtnTitle : String, var secondBtnTitle : String, var twoBtnInter: TwoBtnSwitch? = null) : ViewModel() {

    public var isSwitched = MutableLiveData<Boolean>().also {
        it.value = false
    }

    fun onClickFirst() {
        isSwitched.value = false
        twoBtnInter?.firstBtnClicked()
    }

    fun onClickSecond() {
        isSwitched.value = true
        twoBtnInter?.secondBtnClicked()
    }

}
