package com.dmonster.darling.honey.main.viewmodel

import android.view.View
import android.widget.RadioGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.AppKeyValue
import kotlinx.android.synthetic.main.layout_filter_marry.view.*

abstract class FilterMarryVM : ViewModel() {
    private var res = ""
    var checkIndex = MutableLiveData<Int>().also { it.value = 0 }
    var listener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        when (group.checkedRadioButtonId) {
            R.id.rb_marry -> {
                checkIndex.value = 1
                res = "결혼"
            }
            R.id.rb_marry_again -> {
                checkIndex.value = 2
                res = "재혼"
            }
            else -> {
                checkIndex.value = 0
                res = ""
            }
        }
    }

    fun onClickInit(view: View) {
        afterClickInit()
    }

    fun getMarry(): String {
        return res
    }

    abstract fun onClickBack()
    abstract fun onClickSearch()
    abstract fun afterClickInit()
}