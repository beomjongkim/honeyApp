package com.dmonster.darling.honey.common.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

abstract class BaseEditRegexVM : ViewModel() {

    private lateinit var textWatcher: TextWatcher

    fun setTextWatcher(editText: EditText) {

        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onTextChange(editText, s, 0, 0, 0)

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        }
        editText.addTextChangedListener(textWatcher)
    }

    abstract fun onTextChange(editText: EditText, s: CharSequence?, start: Int, before: Int, count: Int)
}