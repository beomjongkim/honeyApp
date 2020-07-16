package com.dmonster.darling.honey.ads.viewmodel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.view.FullScreenActivity
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.point.viewmodel.PointViewModel
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.youtube.view.YoutubePlayerActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_my_act_new_member.*

class RewardVM(var activity: Activity) : ViewModel(), LifecycleObserver {
    var rewardedAd = RewardedAd(activity, activity.getString(R.string.rewardAd_id))
    var model: ItemModel
    lateinit var adCallback: RewardedAdCallback

    init {
        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                // Ad failed to load.
            }
        }
        rewardedAd.loadAd(AdRequest.Builder().build(), adLoadCallback)
        model = ItemModel()

    }

    var adCallBackBase = object : RewardedAdCallback() {
        override fun onUserEarnedReward(p0: RewardItem) {
            adCallback.onUserEarnedReward(p0)
        }

        override fun onRewardedAdOpened() {
            adCallback.onRewardedAdOpened()
        }

        override fun onRewardedAdClosed() {
            adCallback.onRewardedAdClosed()

        }

        override fun onRewardedAdFailedToShow(errorCode: Int) {
            adCallback.onRewardedAdFailedToShow(errorCode)
            activity.startActivity(Intent(activity, YoutubePlayerActivity::class.java))
        }
    }


}