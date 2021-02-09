package com.dmonster.darling.honey.youtube.viewmodel

import android.app.Activity
import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.youtube.data.YoutubeData
import com.dmonster.darling.honey.youtube.model.YoutubeModel
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import io.reactivex.observers.DisposableObserver
import java.text.SimpleDateFormat
import java.util.*

class YoutubePlayerVM(var activity: Activity, var secLeftTextView: TextView) : ViewModel(),
    YouTubePlayer.OnInitializedListener {
    var apiKey: String = activity.getString(R.string.google_api_key)
    var currentMilliSec = 0
    var leftSec = 30
    var timer: CountDownTimer = object : CountDownTimer(30000, 1000) {
        override fun onFinish() {
            TODO("Not yet implemented")
        }

        override fun onTick(millisUntilFinished: Long) {
            TODO("Not yet implemented")
        }

    }
    var model = YoutubeModel()

    fun onClickSkip(v: View) {
        leftSec.let {
            if (it <= 0) {
                ItemModel().also {
                    var id = Utility.instance.getPref(activity, AppKeyValue.instance.savePrefID)
                    Log.e("idCheck","onClickSkip id : "+id)
                    it.addAdsReward(id, object : DisposableObserver<ResultItem<String>>() {
                        override fun onComplete() {
                        }

                        override fun onNext(t: ResultItem<String>) {
                            if (t.isSuccess) {
                                Utility.instance.showToast(activity, t.message)

                                val sdf = SimpleDateFormat("yyyy-MM-dd")
                                val currentDate = sdf.format(Date())
                                Utility.instance.savePref(activity,AppKeyValue.instance.savePrefDayReward,currentDate)

                                val myIntent = Intent("refresh")
                                myIntent.putExtra("action", "aaa")
                                activity.sendBroadcast(myIntent)

                            } else {
                                Utility.instance.showToast(activity,
                                    activity.getString(R.string.app_error))
                            }
                            activity.finish()
                        }

                        override fun onError(e: Throwable) {
                            Utility.instance.showToast(activity,
                                activity.getString(R.string.app_error))
                        }
                    })
                }
            }
        }

    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        youtubePlayer: YouTubePlayer?,
        isReady: Boolean
    ) {
        if (!isReady) {

            model.getYoutubePlayKey(object : DisposableObserver<ResultItem<YoutubeData>>() {
                override fun onComplete() {

                    var id = Utility.instance.getPref(activity, AppKeyValue.instance.savePrefID)
                    Log.e("YutubeCheck","id 2 : "+id)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onNext(t: ResultItem<YoutubeData>) {
                    if (t.isSuccess) {
                        youtubePlayer?.let {
                            it.cueVideo(t.item?.playKey)
                            it.setPlaybackEventListener(object :
                                YouTubePlayer.PlaybackEventListener {
                                override fun onSeekTo(p0: Int) {
                                    currentMilliSec = it.currentTimeMillis
                                }

                                override fun onBuffering(p0: Boolean) {
                                }

                                override fun onPlaying() {
                                    timer =
                                        object : CountDownTimer((leftSec * 1000).toLong(), 1100) {
                                            override fun onTick(millisUntilFinished: Long) {
                                                leftSec = (millisUntilFinished / 1000).toInt()
                                                secLeftTextView.text = leftSec.toString() + "초 남음"
                                            }

                                            override fun onFinish() {
                                                secLeftTextView.text = "이용권 받기"
                                            }
                                        }.also { it.start() }

                                }

                                override fun onStopped() {
                                }

                                override fun onPaused() {
                                    timer.cancel()
                                }

                            })
                        }
                    }

                }
            })

        }

        youtubePlayer?.let {

            it.setPlayerStateChangeListener(object : YouTubePlayer.PlayerStateChangeListener {
                override fun onAdStarted() {}
                override fun onLoading() {}
                override fun onVideoStarted() {}
                override fun onVideoEnded() {}
                override fun onError(p0: YouTubePlayer.ErrorReason) {}
                override fun onLoaded(videoId: String) {
                    it.play()
                }
            })
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
    }

}