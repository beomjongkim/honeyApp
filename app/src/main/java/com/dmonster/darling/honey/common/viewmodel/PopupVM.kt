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
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.notice.view.NoticeActivity
import com.dmonster.darling.honey.util.AppKeyValue


class PopupVM(var title: String, var subTitle: String?, var dialogInterface: CustomDialogInterface? = null, var subTitleTwo: String = "", var link : String = "", val lifecycleOwner: LifecycleOwner? = null) : ViewModel(), LifecycleObserver {
    var positiveText = MutableLiveData<String>().also { it.value = "확인" }
    var negativeText = MutableLiveData<String>().also { it.value = "취소" }

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
    }

    fun onClickLink(v : View, url : String){
        if(url.contains("http")){
            val i = Intent(Intent.ACTION_VIEW,Uri.parse(url))
            v.context.startActivity(i)
        }else if(url.contains("market")){
            val i = Intent(v.context,MainActivity::class.java)
            i.putExtra(AppKeyValue.instance.goToMarket,true)
            v.context.startActivity(i)
        }else if(url.contains("magazine")){
            val i = Intent(v.context,MainActivity::class.java)
            i.putExtra(AppKeyValue.instance.goToMagazine,true)
            v.context.startActivity(i)
        }else if(url.contains("mailBox")){
            val i = Intent(v.context,MainActivity::class.java)
            i.putExtra(AppKeyValue.instance.goToMailBox,true)
            v.context.startActivity(i)
        }else if(url.contains("notice")){
            val i = Intent(v.context,NoticeActivity::class.java)
            v.context.startActivity(i)
        }else{
            dialogInterface?.onConfirm(v)
        }

    }

}