package com.dmonster.darling.honey.banner.viewmodel

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView

class BannerVM(var lifecycle: Lifecycle, var view: BannerAdView)  : ViewModel() , LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    var adListener =  object : AdListener {  // optional :: 광고 수신 리스너 설정

        override fun onAdLoaded() {
            // 배너 광고 노출 완료 시 호출
            Log.d("bannerVM", "adLoaded")
        }

        override fun onAdFailed(errorCode: Int) {
            // 배너 광고 노출 실패 시 호출
            Log.d("bannerVM", "errorCode:$errorCode")
        }

        override fun onAdClicked() {
            // 배너 광고 클릭 시 호출
            Log.d("bannerVM", "adClicked")

        }

    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        view.resume()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        view.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        view.destroy()
    }
}