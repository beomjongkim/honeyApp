package com.dmonster.darling.honey.ads.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.databinding.ActivityFullScreenBinding
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.youtube.data.YoutubeData
import com.dmonster.darling.honey.youtube.model.YoutubeModel
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityFullScreenBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_full_screen)
        binding.ctActFullScreen.setTitle("전면광고")
        Glide.with(binding.ivActFullScreen.context).asGif().load(R.raw.ad_fullscreen).apply(
            RequestOptions().centerCrop().placeholder(R.color.color_black)
        ).into(binding.ivActFullScreen)

        val model = YoutubeModel()
        model.getYoutubePlayKey(object: DisposableObserver<ResultItem<YoutubeData>>(){
            override fun onComplete() {
            }

            override fun onNext(t: ResultItem<YoutubeData>) {
                if(t.isSuccess){
                    binding.ivActFullScreen.setOnClickListener {
                        val i = Intent(Intent.ACTION_VIEW, Uri.parse(t.item?.link))
                        startActivity(i)
                        finish()
                    }
                }
            }

            override fun onError(e: Throwable) {
            }
        })
    }
}