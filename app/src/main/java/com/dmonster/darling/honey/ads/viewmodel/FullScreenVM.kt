package com.dmonster.darling.honey.ads.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.view.FullScreenActivity
import com.dmonster.darling.honey.point.data.CheckFreePassData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.point.model.PointModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import io.reactivex.observers.DisposableObserver

class FullScreenVM(var activity: Activity, var mb_id: String) : ViewModel(), LifecycleObserver {
    var fullScreenAd = InterstitialAd(activity)
    var hasPass = MutableLiveData<Boolean>().also {
        it.value = false
    }

    init {
        (activity as FragmentActivity).lifecycle.addObserver(this)
        fullScreenAd.adUnitId = activity.getString(R.string.interstitialAd_id)
        fullScreenAd.loadAd(AdRequest.Builder().build())
        ItemModel().also {
            it.check_own_freepass(mb_id,object : DisposableObserver<ResultItem<CheckFreePassData>>(){
                override fun onComplete() {

                }

                override fun onError(e: Throwable) {
                    hasPass.value = false
                    val pref = activity.getSharedPreferences("Pref", Context.MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putBoolean(AppKeyValue.instance.hasFreePass, false)
                    editor.apply()
                }

                override fun onNext(item: ResultItem<CheckFreePassData>) {

                        item.let { it ->
                            val pref = activity.getSharedPreferences("Pref", Context.MODE_PRIVATE)
                            val editor = pref.edit()
                            editor.putBoolean(AppKeyValue.instance.hasFreePass, item.isSuccess)
                            editor.apply()
                            if(Integer.parseInt(it.item?.minutes_left!!) >  60)
                                hasPass.value = it.isSuccess
                            else
                                hasPass.value = false
                        }
                    }

            })
        }
    }

    fun showAd() {
        if(!hasPass.value!!){
            if (fullScreenAd.isLoaded) {
                fullScreenAd.show()
            } else {
                activity.startActivity(Intent(activity, FullScreenActivity::class.java))
            }
        }

    }

}