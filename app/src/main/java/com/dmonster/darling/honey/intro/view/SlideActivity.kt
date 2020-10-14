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
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.intro.data.SlideData
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.webview.view.WebViewActivity

class SlideActivity : AppCompatActivity() {

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
        val banner : ViewPager  = findViewById(R.id.vp_slide)
        var slideDataList = ArrayList<SlideData>()
        slideDataList.add(SlideData("자기야","커플 매칭의 모든 것 결혼어플 자기야! 자기야의 다양한 서비스를 통해 마음에 드는 이성을 찾고, 나의 매력을 어필하여 인연을 맺어보세요.", R.drawable.intro_img_1))
        slideDataList.add(SlideData("언제 어디서나","결혼어플 자기야는 IOS, 안드로이드, 웹 통합 기능으로 언제 어디서나 어떤 매체로든 편안하게 이용할 수 있는 환경을 제공합니다.", R.drawable.intro_img_2))
        slideDataList.add(SlideData("다크모드 지원","결혼어플 자기야를 통해 마음에 드는 이성과 장시간 소통할 수 있도록, 시간에 구애받지 않고 소통할 수 있도록 눈이 편안한 다크모드를 지원합니다.", R.drawable.intro_img_3))
        slideDataList.add(SlideData("선물&쇼핑하기","이제 결혼어플 자기야에서 선물&쇼핑도 가능합니다! 직접 쇼핑도 즐기고, 마음에 드는 이성에게 선물하기 기능을 통해 마음을 표현해보세요!", R.drawable.intro_img_4))
        slideDataList.add(SlideData("그 외 다양한 기능","결혼어플 자기야는 프로필 점프 ,채팅, 화상채팅 등의 기능들을  제공하고 있으며 횡원님들이 더 편리하게 스마트하게 성혼하실 수 있또록 다양한 기능을 지속적으로 추가하고 있습니다.", R.drawable.intro_img_5))

        banner.setAdapter(object :SlideAdapter(this, slideDataList){
            override fun onTouchLast() {
                onClickSkip()
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return (view == `object` as View)
            }

        })


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

}
