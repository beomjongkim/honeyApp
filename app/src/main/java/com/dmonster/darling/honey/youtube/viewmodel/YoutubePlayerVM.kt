package com.dmonster.darling.honey.youtube.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.Utility
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

class YoutubePlayerVM(var context: Context) : ViewModel() , YouTubePlayer.OnInitializedListener{
    var apiKey : String  = context.getString(R.string.google_api_key)
    var currentMilliSec = MutableLiveData<Int>().also {
        it.value = 0
    }
    var spentSec = MutableLiveData<Int>().also {
        it.value = 10
    }


    fun onClickSkip(v: View){
        spentSec.value?.let {
            if(it < 0){
                Utility.instance.showToast(v.context,"포인트 지급")
            }
        }

    }
    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        youtubePlayer: YouTubePlayer?,
        isReady: Boolean
    ) {
        if (!isReady) {
            val playListKey = "PLRfRmtouqieksM6IUQzU2dTImvm7epllZ"
            youtubePlayer?.let {
                it.cuePlaylist(playListKey, 10,0)

                it.setPlaybackEventListener(object :YouTubePlayer.PlaybackEventListener{
                    override fun onSeekTo(p0: Int) {
                        currentMilliSec.value = it.currentTimeMillis
                    }

                    override fun onBuffering(p0: Boolean) {
                    }

                    override fun onPlaying() {
                        Utility.instance.showToast(context, youtubePlayer.currentTimeMillis.toString())
                    }

                    override fun onStopped() {
                    }

                    override fun onPaused() {
                        spentSec.value =
                            spentSec.value?.minus((it.currentTimeMillis -currentMilliSec.value!!)/1000)
                       currentMilliSec.value = it.currentTimeMillis

                    }

                })
            }
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
    }

}