package com.dmonster.darling.honey.intro.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.intro.data.SlideData
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.webview.view.WebViewActivity
import kotlinx.android.synthetic.main.activity_slide.*
import kotlinx.android.synthetic.main.item_slide.view.*

class SlideActivity : AppCompatActivity() {
    private var itemList =  ArrayList<SlideData>()
    private lateinit var slide : ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)
        initFCM()
        init()
    }
    fun init(){
        if(Utility.instance.getPref(this,"firstIntroduce")=="true"){
//            startActivity(Intent(this@SlideActivity,IntroActivity::class.java))
//            finish()
        }
        val skip :TextView= findViewById(R.id.tv_slide_skip)
        skip.setOnClickListener {
            onClickSkip()
        }
         slide = findViewById(R.id.vp_slide)
        var imgList = ArrayList<Int>()
        imgList.add(R.drawable.intro_img_1)
        imgList.add(R.drawable.intro_img_2)
        imgList.add(R.drawable.intro_img_3)
        imgList.add(R.drawable.intro_img_4)
        imgList.add(R.drawable.intro_img_5)

        itemList.add(SlideData("자기야","커플 매칭의 모든 것 결혼어플 자기야! 자기야의 다양한 서비스를 통해 마음에 드는 이성을 찾고, 나의 매력을 어필하여 인연을 맺어보세요.", R.drawable.scroll1))
        itemList.add(SlideData("언제 어디서나","결혼어플 자기야는 IOS, 안드로이드, 웹 통합 기능으로 언제 어디서나 어떤 매체로든 편안하게 이용할 수 있는 환경을 제공합니다.", R.drawable.scroll2))
        itemList.add(SlideData("다크모드 지원","결혼어플 자기야를 통해 마음에 드는 이성과 장시간 소통할 수 있도록, 시간에 구애받지 않고 소통할 수 있도록 눈이 편안한 다크모드를 지원합니다.", R.drawable.scroll3))
        itemList.add(SlideData("선물&쇼핑하기","이제 결혼어플 자기야에서 선물&쇼핑도 가능합니다! 직접 쇼핑도 즐기고, 마음에 드는 이성에게 선물하기 기능을 통해 마음을 표현해보세요!", R.drawable.scroll4))
        itemList.add(SlideData("그 외 다양한 기능","결혼어플 자기야는 더욱더 편리하고 스마트하게 성혼하실 수 있도록 다양한 기능들을 지속적으로 추가하고 있습니다.", R.drawable.scroll5))

        slide.adapter = object :SlideAdapter(this, imgList){
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return (view == `object` as View)
            }

        }
        val viewPager = findViewById<ViewPager>(R.id.vp_slide)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
               if(position == itemList.lastIndex){
                    btn_intro_slide_next.visibility = View.VISIBLE
                    btn_intro_slide_next.text = "시작하기"
                    btn_intro_slide_next.setOnClickListener { v: View? ->
                        onClickSkip()
                    }
                   btn_intro_slide_before.visibility = View.GONE
                } else if(position==0){
                   tv_intro_slide_subtitle1.visibility = View.VISIBLE
                   tv_intro_slide_subtitle2.visibility = View.VISIBLE
                   tv_intro_slide_subtitle3.visibility = View.VISIBLE
                   btn_intro_slide_before.visibility = View.GONE
               }else{
                    tv_intro_slide_subtitle1.visibility = View.GONE
                    tv_intro_slide_subtitle2.visibility = View.GONE
                    tv_intro_slide_subtitle3.visibility = View.GONE
                    btn_intro_slide_before.visibility = View.VISIBLE
                    btn_intro_slide_next.visibility = View.VISIBLE
                    btn_intro_slide_next.text = "다음"
                   btn_intro_slide_before.setOnClickListener { view: View? ->
                       onClickBefore()
                   }
                   btn_intro_slide_next.setOnClickListener { view: View? ->
                       onClickNext()
                   }
                }
                Glide.with(this@SlideActivity).load(itemList[position].img)
                    .into(iv_slide_scroll)
                tv_item_slide_title.text = itemList[position].title
                tv_item_slide_description.text = itemList[position].description
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
        btn_intro_slide_before.setOnClickListener { view: View? ->
            onClickBefore()
        }
        btn_intro_slide_next.setOnClickListener { view: View? ->
            onClickNext()
        }
    }

    private fun initFCM() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    AppKeyValue.instance.notificationChannelId,
                    AppKeyValue.instance.notificationChannelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!!.get(key)
                Log.d("Fcm", "Key: $key Value: $value")
                if(key == "link" ){
                    val mIntent = Intent(this@SlideActivity,WebViewActivity::class.java)
                    mIntent.putExtra("link", value.toString())
                    startActivity(mIntent)
                }
            }
        }
    }
    private fun onClickSkip(){
        Utility.instance.savePref(this@SlideActivity,"firstIntroduce", "true")
        startActivity(Intent(this@SlideActivity,IntroActivity::class.java))
        finish()
    }
    private fun onClickBefore(){
        slide.let {
            it.setCurrentItem(it.currentItem-1)
        }
    }
    private fun onClickNext(){
        slide.let {
            it.setCurrentItem(it.currentItem+1)
        }
    }

}
