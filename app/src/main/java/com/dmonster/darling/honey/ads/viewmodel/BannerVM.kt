package com.dmonster.darling.honey.ads.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.youtube.data.YoutubeData
import com.dmonster.darling.honey.youtube.model.YoutubeModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import io.reactivex.observers.DisposableObserver
import java.util.*

class BannerVM(var mb_id: String?, var lifecycle: Lifecycle, var context: Context) : ViewModel(), LifecycleObserver {
    var adRequest: AdRequest = AdRequest.Builder().build()
    var selfBannerId = -1
    var adAvailable = false
    var adListener = object : AdListener() {
        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
            val pref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
            if(pref.getBoolean(AppKeyValue.instance.hasFreePass,true)){
                isBannerShown.value = false
                isSelfBannerShown.value = true
            }else{
                isBannerShown.value = true
                isSelfBannerShown.value = false
            }

        }

        override fun onAdFailedToLoad(errorCode: Int) {
            // Code to be executed when an ad request fails.
//            adAvailable = false
        }

        override fun onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
//            adAvailable = true
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
    var isBannerShown = MutableLiveData<Boolean>().also {
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

//    private fun checkOwnFreepass() {
//        val pref = context.getSharedPreferences("Pref", Context.MODE_PRIVATE)
//        Log.d("BannerVM", pref.getBoolean(AppKeyValue.instance.hasFreePass,false).toString())
//        if(adAvailable){//광고를 불러올 수 있는 상황이라면,
//            isBannerShown.value =  !pref.getBoolean(AppKeyValue.instance.hasFreePass,false)//서버에서 이용권있는지 체크하고 이용권이 있다면 배너를 없앤다.
//            isSelfBannerShown.value =  false
//        }else{//
//            isBannerShown.value = false
//            isSelfBannerShown.value = !pref.getBoolean(AppKeyValue.instance.hasFreePass,false)
//        }
//    }
//
    fun onClickSelfBanner(v : View){
        link?.let{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            v.context.startActivity(i)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
    }


}