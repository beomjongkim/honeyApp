package com.dmonster.darling.honey.webview.viewmodel

import android.webkit.CookieManager
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.util.Utility

class WebViewmodel(var url : String) : ViewModel() {
    var cookieManager: CookieManager
    init {
        cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        val url_home = "https://jjagiya.co.kr"
        setMainColor(url_home)
        login(url_home)

    }

    private fun login(url: String){
        Utility.instance.UserData().getUserId()?.let {
            if (it.isNotBlank()) {
                cookieManager.setCookie(url, "cookie_logged_in=true")
                cookieManager.setCookie(url, "cookie_logged_in_id=${Utility.instance.UserData().getUserId()}")
                cookieManager.setCookie(url, "cookie_logged_in_nick=${Utility.instance.UserData().getUserNick()}")
                cookieManager.setCookie(url, "cookie_logged_in_gender=${Utility.instance.UserData().getUserGender()}")
               
            }
        }
    }
    private fun setMainColor(url: String){
        cookieManager.setCookie(url, "azures-color-scheme=red2}")
    }
}