package com.dmonster.darling.honey.intro.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.Utility

class SlideActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slide)
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
        banner.setAdapter(object :SlideAdapter(this, arrayListOf(R.drawable.intro_page_slide01,R.drawable.intro_page_slide02,R.drawable.intro_page_slide03,R.drawable.intro_page_slide04)){
            override fun onTouchLast() {
                onClickSkip()
            }

            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return (view == `object` as View)
            }

        })


    }
    private fun onClickSkip(){
        Utility.instance.savePref(this@SlideActivity,"firstIntroduce", "true")
        startActivity(Intent(this@SlideActivity,IntroActivity::class.java))
        finish()
    }

}
