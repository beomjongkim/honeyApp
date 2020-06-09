package com.dmonster.darling.honey.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.TextWatcher
import android.widget.ArrayAdapter

//주요기능
//1. 스크롤 되는 아이템을 세팅하고 특정 이벤트에 따라 표시할 수 있다.
//2. 에딧텍스트로 글자를 넣을 수 있다.
//3. 에딧텍스트의 글자가 변할 시에, 조건 검사를 할 수 있음.
open class SpinnerVM(
    open var arrayAdapter: ArrayAdapter<String>
) : ViewModel(){
    var text : MutableLiveData<String> = MutableLiveData()
    var passed : MutableLiveData<Boolean> = MutableLiveData()
    init {
        text.value=""
        passed.value= false
    }

}
