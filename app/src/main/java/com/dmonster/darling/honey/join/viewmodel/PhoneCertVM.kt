package com.dmonster.darling.honey.join.viewmodel

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.information.view.PhoneAuthActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import kotlinx.android.synthetic.main.fragment_my_info_member.*

abstract class PhoneCertVM : ViewModel() {
    var phoneNumber=MutableLiveData<String>().also {
        it.value = ""
    }

    fun onClickCert(view : View){
        val activity = getActivity()
        activity?.let { it1 ->
            if (phoneNumber.value.isNullOrEmpty()) {
                Utility.instance.showToast(
                    it1,
                    it1.resources.getString(R.string.msg_error_information_member_phone)
                )
            } else {
                startActivityForResult()
            }
        }
    }
    abstract fun getActivity() : Activity
    abstract fun startActivityForResult()
}