package com.dmonster.darling.honey.youtube.viewmodel

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.Utility
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

class YoutubePlayerVM(var apiKey : String) : ViewModel() {

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

}