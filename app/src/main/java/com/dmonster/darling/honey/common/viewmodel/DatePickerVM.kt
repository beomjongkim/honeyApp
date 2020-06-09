package com.dmonster.darling.honey.common.viewmodel

import android.app.DatePickerDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Build
import android.view.View
import java.util.*


class DatePickerVM : ViewModel() {

    var year = MutableLiveData<String>().apply { value= "" }
    var month =  MutableLiveData<String>().apply { value= "" }
    var day= MutableLiveData<String>().apply { value= "" }
    var datePickerDialog: DatePickerDialog? = null





    fun onClickButton(v: View) {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, _year, _month, _dayOfMonth ->
           year.value =_year.toString()
            month.value  =_month.toString()
            day.value =_dayOfMonth.toString()

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog = DatePickerDialog(v.context)
            datePickerDialog!!.setOnDateSetListener(dateSetListener)
        } else {
            datePickerDialog = DatePickerDialog(
                v.context,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }

        datePickerDialog!!.let {
            //첫 화면은 연도 선택하도록 세팅
            it.datePicker.touchables[0].performClick()
            //선택하게할 연도 제한 걸기
            val c = Calendar.getInstance()
            c.add(Calendar.YEAR, -19)
            it.datePicker.maxDate = c.timeInMillis

            it.show()
        }
    }

}