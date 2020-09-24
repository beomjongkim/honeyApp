package com.dmonster.darling.honey.webview.view

import android.content.Intent
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.databinding.ActivityWebViewBinding
import com.dmonster.darling.honey.login.view.LoginEmailActivity
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.webview.viewmodel.WebViewmodel

class WebViewActivity : AppCompatActivity() {
    lateinit var activityWebViewBinding : ActivityWebViewBinding
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_web_view)
        var id = Utility.instance.UserData().getUserId()
        activityWebViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        activityWebViewBinding.webViewModel = WebViewmodel("https://jjagiya.co.kr/home.html")
        activityWebViewBinding.bannerVM = BannerVM(id, lifecycle, this)
        webView = activityWebViewBinding.wvWebview
        activityWebViewBinding.lifecycleOwner = this

        if(id.isNullOrEmpty()){
            val intent = Intent(this, LoginEmailActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }


}