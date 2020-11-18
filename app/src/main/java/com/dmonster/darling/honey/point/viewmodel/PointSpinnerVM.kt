package com.dmonster.darling.honey.point.viewmodel

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import com.dmonster.darling.honey.common.command.SpinnerInterface
import com.dmonster.darling.honey.common.viewmodel.SpinnerVM

class PointSpinnerVM(arrayAdapter: ArrayAdapter<String>, spinnerInterface :SpinnerInterface) : SpinnerVM(arrayAdapter) {
    var won = MutableLiveData<String>().also {
        it.value = ""
    }
    var price = MutableLiveData<Int>().also {
        it.value = 0
    }
    var position = MutableLiveData<Int>().also {
        it.value = 0
    }


    init {
        itemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                won.value =""
                spinnerInterface.onNothingSelected()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                mPosition: Int,
                id: Long
            ) {
                position.value = mPosition
                price.value = arrayAdapter.getItem(mPosition)!!.replace("포인트","").toInt()
                won.value=(price.value!! *110).toString() + "원"
                spinnerInterface.onItemSelected()
            }

        }
    }

}