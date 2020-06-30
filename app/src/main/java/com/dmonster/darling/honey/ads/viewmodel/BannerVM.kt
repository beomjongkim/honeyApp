package com.dmonster.darling.honey.ads.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.dmonster.darling.honey.point.data.CheckFreePassData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import io.reactivex.observers.DisposableObserver

class BannerVM(var mb_id: String, var lifecycle: Lifecycle, var context: Context) : ViewModel(), LifecycleObserver {
    var adRequest: AdRequest = AdRequest.Builder().build()
    var adListener = object : AdListener() {
        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            // Code to be executed when an ad request fails.
        }

        override fun onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
        }

        override fun onAdClicked() {
            // Code to be executed when the user clicks on an ad.
            Log.d("bannerVM", "adClicked")
        }

        override fun onAdLeftApplication() {
            // Code to be executed when the user has left the app.
        }

        override fun onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
        }
    }
    var hasPass = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var itemModel = ItemModel()

    init {
        lifecycle.addObserver(this)

    }

    private fun checkOwnFreepass() {
        val subscriber = object : DisposableObserver<ResultItem<CheckFreePassData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                hasPass.value = false
                val pref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
                val editor = pref.edit()
                editor.putBoolean(AppKeyValue.instance.hasFreePass, false)
                editor.apply()
            }

            override fun onNext(item: ResultItem<CheckFreePassData>) {
                item.let { it ->
                    val pref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
                    val editor = pref.edit()
                    editor.putBoolean(AppKeyValue.instance.hasFreePass, item.isSuccess)
                    editor.apply()
                    hasPass.value = it.isSuccess

                }
            }
        }

        itemModel.check_own_freepass(mb_id, subscriber)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        checkOwnFreepass()
    }


}