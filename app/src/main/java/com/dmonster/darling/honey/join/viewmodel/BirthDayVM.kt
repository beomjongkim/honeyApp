package com.dmonster.darling.honey.join.viewmodel

import androidx.lifecycle.MutableLiveData
import android.widget.EditText
import com.dmonster.darling.honey.common.viewmodel.BaseEditRegexVM
import com.dmonster.darling.honey.util.RegexStr
import com.dmonster.darling.honey.util.Utility

class BirthDayVM: BaseEditRegexVM()  {
    val mPassed : MutableLiveData<Boolean> = MutableLiveData()
    override fun onTextChange(editText: EditText, s: CharSequence?, start: Int, before: Int, count: Int) {
        mPassed.value=false
        s?.let {
            if (!s.isNullOrBlank()) {

                if (it.contains(Regex(RegexStr.instance.overOneDigit)) && (it.toString().toInt() <= 31) && (it.toString().toInt() > 0)) {
                    mPassed.value=true
                } else {
                    editText.setText("")
                    Utility.instance.showToast(editText.context, "1~31 사이의 값을 입력해주세요")
                }
            }
        }
    }
}