package com.dmonster.darling.honey.join.viewmodel

import androidx.lifecycle.MutableLiveData
import android.widget.EditText
import com.dmonster.darling.honey.common.viewmodel.BaseEditRegexVM
import com.dmonster.darling.honey.util.RegexStr
import com.dmonster.darling.honey.util.Utility
import java.util.*

class BirthYearVM : BaseEditRegexVM() {
    val mPassed : MutableLiveData<Boolean> = MutableLiveData()

    override fun onTextChange(editText: EditText, s: CharSequence?, start: Int, before: Int, count: Int) {
        //수동 입력된 연도를 현재 날짜와 비교해서 성인이 아닐 경우 사용 못하도록 막기.
        val cal = Calendar.getInstance()
        mPassed.value =false
        //1900~2099 && 현 시점에서 성인이 아닌 경우
        if (s?.contains(Regex(RegexStr.instance.birthYear))!! && (cal.get(Calendar.YEAR) - s.toString().toInt() > 18)) {
            mPassed.value = true
        } else if (s.contains(Regex(RegexStr.instance.fourDigit))) {
            editText.setText("")
            Utility.instance.showToast(editText.context, "자기야는 성인만 이용 가능합니다.")
        }
    }

}