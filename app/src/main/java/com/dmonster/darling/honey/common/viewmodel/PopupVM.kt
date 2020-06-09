package com.dmonster.darling.honey.common.viewmodel

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.customview.CustomDialogInterface


class PopupVM(var title: String, var subTitle: String?, var dialogInterface: CustomDialogInterface? = null, var subTitleTwo: String = "", var link : String = "", val lifecycleOwner: LifecycleOwner? = null) : ViewModel(), LifecycleObserver {
    var positiveText = MutableLiveData<String>().also { it.value = "확인" }
    var negativeText = MutableLiveData<String>().also { it.value = "취소" }

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    fun onClickLink(v : View, url : String){
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        v.context.startActivity(i)
    }



}