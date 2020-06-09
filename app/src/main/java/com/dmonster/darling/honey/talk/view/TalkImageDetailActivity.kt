package com.dmonster.darling.honey.talk.view

import android.os.Bundle
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.util.AppKeyValue
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_image_detail.*
import kotlinx.android.synthetic.main.activity_talk_image_detail.*
import uk.co.senab.photoview.PhotoViewAttacher

class TalkImageDetailActivity : BaseActivity() {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var attacher: PhotoViewAttacher

    private var talkImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_image_detail)

        init()
    }

    private fun init() {
        talkImage = intent.getStringExtra(AppKeyValue.instance.talkImage)

        disposeBag = CompositeDisposable()
        attacher = PhotoViewAttacher(iv_act_talk_image_detail_picture)
        attacher.update()

        Glide.with(this).load(talkImage).into(iv_act_talk_image_detail_picture)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }

}
