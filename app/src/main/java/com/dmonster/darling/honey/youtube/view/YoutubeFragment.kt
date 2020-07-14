package com.dmonster.darling.honey.youtube.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.databinding.ActivityYoutubeBinding
import com.dmonster.darling.honey.databinding.YoutubeFragmentBinding
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.youtube.viewmodel.YoutubePlayerVM
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment

class YoutubeFragment : YouTubePlayerFragment(), YouTubePlayer.OnInitializedListener {

    companion object {
        fun newInstance() = YoutubeFragment()
    }

    lateinit var binding: YoutubeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.activity_youtube, null,false)
        binding.playerVM = YoutubePlayerVM(getString(R.string.google_api_key))
        return inflater.inflate(R.layout.youtube_fragment, container, false)
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
                        binding.playerVM.currentMilliSec.value = it.currentTimeMillis
                    }

                    override fun onBuffering(p0: Boolean) {
                    }

                    override fun onPlaying() {
                        Utility.instance.showToast(context, youtubePlayer.currentTimeMillis.toString())
                    }

                    override fun onStopped() {
                    }

                    override fun onPaused() {
                        binding.playerVM.spentSec.value =
                            binding.playerVM.spentSec.value?.minus((it.currentTimeMillis - binding.playerVM.currentMilliSec.value!!)/1000)
                        binding.playerVM.currentMilliSec.value = it.currentTimeMillis

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