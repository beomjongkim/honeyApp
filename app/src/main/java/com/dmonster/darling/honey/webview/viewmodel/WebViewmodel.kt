package com.dmonster.darling.honey.webview.viewmodel

import android.webkit.CookieManager
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.js.JSHandler
import com.dmonster.darling.honey.util.Utility

class WebViewmodel(var url : String, var jsHandler: JSHandler) : ViewModel() {
    var cookieManager: CookieManager
    init {
        cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        val url_home = "https://jjagiya.co.kr"
        setMainColor(url_home)
        login(url_home)

    }

    fun login(mUrl: String){
        Utility.instance.UserData().getUserId()?.let {
            if (it.isNotBlank()) {
                cookieManager.setCookie(mUrl, "cookie_logged_in=true")
                cookieManager.setCookie(mUrl, "cookie_logged_in_id=${Utility.instance.UserData().getUserId()}")
                cookieManager.setCookie(mUrl, "cookie_logged_in_nick=${Utility.instance.UserData().getUserNick()}")
                cookieManager.setCookie(mUrl, "cookie_logged_in_gender=${Utility.instance.UserData().getUserGender()}")
            }
        }
    }
    private fun setMainColor(mUrl: String){
        cookieManager.setCookie(mUrl, "azures-color-scheme=red2")
    }
}