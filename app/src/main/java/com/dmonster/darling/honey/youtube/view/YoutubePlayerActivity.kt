package com.dmonster.darling.honey.youtube.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.databinding.ActivityYoutubeBinding
import com.dmonster.darling.honey.youtube.viewmodel.YoutubePlayerVM
import com.google.android.youtube.player.YouTubeBaseActivity

class YoutubePlayerActivity : YouTubeBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityYoutubeBinding = DataBindingUtil.setContentView(this,R.layout.activity_youtube)
        binding.playerVM = YoutubePlayerVM(this,findViewById(R.id.tv_act_youtube_second))
    }
}