package com.dmonster.darling.honey.banner.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest

class BannerVM(var lifecycle: Lifecycle )  : ViewModel() , LifecycleObserver {
    var adRequest : AdRequest = AdRequest.Builder().build()
    var adListener =  object: AdListener() {
        override fun onAdLoaded() {
            // Code to be executed when an ad finishes loading.
        }

        override fun onAdFailedToLoad(errorCode : Int) {
            // Code to be executed when an ad request fails.
        }

        override fun onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
        }

        override fun onAdClicked() {
            // Code to be executed when the user clicks on an ad.
            Log.d("bannerVM","adClicked")
        }

        override fun onAdLeftApplication() {
            // Code to be executed when the user has left the app.
        }

        override fun onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
        }
    }
    init {
        lifecycle.addObserver(this)
    }

}