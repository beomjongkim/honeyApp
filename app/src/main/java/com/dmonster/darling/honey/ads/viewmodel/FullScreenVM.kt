package com.dmonster.darling.honey.ads.viewmodel

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.point.data.CheckFreePassData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import io.reactivex.observers.DisposableObserver

class FullScreenVM(var activity : Activity,var mb_id: String): ViewModel(),LifecycleObserver{
   var  fullScreenAd = InterstitialAd(activity)
    var hasPass = MutableLiveData<Boolean>().also {
        it.value = false
    }
    init {
        (activity as FragmentActivity).lifecycle.addObserver(this)
        fullScreenAd.adUnitId = activity.getString(R.string.interstitialAd_id)
        fullScreenAd.loadAd(AdRequest.Builder().build())
    }

    fun showAd(){
      fullScreenAd.show()
    }

}