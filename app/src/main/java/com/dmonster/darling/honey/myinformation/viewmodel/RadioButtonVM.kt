package com.dmonster.darling.honey.myinformation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RadioButtonVM : ViewModel() {

    var isFirstRadioChecked =  MutableLiveData<Boolean>().also { it.value = true }
}