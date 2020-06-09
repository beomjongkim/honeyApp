package com.dmonster.darling.honey.custom_recyclerview.viewmodel

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.dmonster.darling.honey.R

abstract class SelectorMultiItemVM(override val title : String?) : RecyclerViewItemVM(title) {
    var isChecked =ObservableField<Boolean>().also {
        it.set(false)
    }

    fun onClick(view : View){
        if(isChecked.get() == false){
            onSelected()
        }else{
            onDismissed()
        }
        isChecked.set(isChecked.get()==false)
    }

    abstract fun onSelected()
    abstract fun onDismissed()
}