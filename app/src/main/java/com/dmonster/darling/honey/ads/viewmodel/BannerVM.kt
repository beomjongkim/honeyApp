package com.dmonster.darling.honey.ads.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.dmonster.darling.honey.ads.view.FullScreenActivity
import com.dmonster.darling.honey.point.data.CheckFreePassData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.youtube.data.YoutubeData
import com.dmonster.darling.honey.youtube.model.YoutubeModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.base.R
import io.reactivex.observers.DisposableObserver
import java.util.*

class BannerVM(var mb_id: String, var lifecycle: Lifecycle, var context: Context) : ViewModel(), LifecycleObserver {
    var adRequest: AdRequest = AdRequest.Builder().build()
    var selfBannerId = -1
    var adListener = object : AdListener() {
        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            // Code to be executed when an ad request fails.
            hasPass.value = true
            isSelfBannerShown.value = true
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
    var isSelfBannerShown  = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var link : String? = ""

    var itemModel = ItemModel()

    init {
        lifecycle.addObserver(this)
        YoutubeModel().also {
            it.getYoutubePlayKey(object : DisposableObserver<ResultItem<YoutubeData>>(){
                override fun onComplete() {
                }

                override fun onNext(t: ResultItem<YoutubeData>) {
                    if(t.isSuccess){
                        link = t.item?.link
                    }
                }

                override fun onError(e: Throwable) {
                }

            })
        }

        val selfBannerList = arrayOf(com.dmonster.darling.honey.R.raw.ad_banner1,com.dmonster.darling.honey.R.raw.ad_banner2)
        selfBannerId = selfBannerList[Random().nextInt(2)]
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
                    if(Integer.parseInt(item.item?.minutes_left) >  60)
                        hasPass.value = it.isSuccess
                    else
                        hasPass.value = false
                }
            }
        }

        itemModel.check_own_freepass(mb_id, subscriber)
    }

    fun onClickSelfBanner(v : View){
        link?.let{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            v.context.startActivity(i)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        isSelfBannerShown.value = false
        checkOwnFreepass()
    }


}