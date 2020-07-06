package com.dmonster.darling.honey.common.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dmonster.darling.honey.common.command.SpinnerInterface

//주요기능
//1. 스크롤 되는 아이템을 세팅하고 특정 이벤트에 따라 표시할 수 있다.
//2. 에딧텍스트로 글자를 넣을 수 있다.
//3. 에딧텍스트의 글자가 변할 시에, 조건 검사를 할 수 있음.
open class SpinnerVM(
    open var arrayAdapter: ArrayAdapter<String>
) : ViewModel(){
    var text : MutableLiveData<String> = MutableLiveData()
    var passed : MutableLiveData<Boolean> = MutableLiveData()
    var spinnerInterface = object :SpinnerInterface{
        override fun onItemSelected() {
        }

        override fun onNothingSelected() {
        }

    }
    var itemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            text.value = ""
            spinnerInterface.onNothingSelected()
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            text.value = arrayAdapter.getItem(position)
            spinnerInterface.onItemSelected()
        }
    }

    init {
        text.value=""
        passed.value= false
    }

}
