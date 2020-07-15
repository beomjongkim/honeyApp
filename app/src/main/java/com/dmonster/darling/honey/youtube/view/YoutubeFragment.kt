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

class YoutubeFragment : YouTubePlayerFragment() {

    companion object {
        fun newInstance() = YoutubeFragment()
    }

    lateinit var binding: YoutubeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.activity_youtube, null,false)
        binding.playerVM = YoutubePlayerVM(activity)
        return binding.root
    }


}