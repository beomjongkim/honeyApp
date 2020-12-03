package com.dmonster.darling.honey.webview.viewmodel

import android.app.Application
import android.util.Log
import android.webkit.CookieManager
import androidx.lifecycle.*
import com.dmonster.darling.honey.js.JSHandler
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility

class WebViewmodel(application: Application, var url: String, var jsHandler: JSHandler, lifecycle: Lifecycle) :
    AndroidViewModel(application) , LifecycleObserver {
    var context = application.applicationContext
    var cookieManager: CookieManager

    init {
        cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        val url_home = "https://jjagiya.co.kr"
        cookieManager.getCookie(url_home)?.let {

            val cookieArray =    it.split(";")
            for (cookie in cookieArray) {
                if (cookie.contains("cookie_logged_in_id")) {
                    var keyValue = cookie.split("=")
                    Utility.instance.savePref(application, AppKeyValue.instance.savePrefID, keyValue[1])
                    break
                }
            }
        }

        lifecycle.addObserver(this)
        setMainColor(url_home)
        login(url_home)
    }

    fun login(mUrl: String) {
        var id = Utility.instance.getPref(context, AppKeyValue.instance.savePrefID)
        if (id != null) {
            cookieManager.setCookie(mUrl, "cookie_logged_in=true")
            cookieManager.setCookie(mUrl,
                "cookie_logged_in_id=${ Utility.instance.getPref(context, AppKeyValue.instance.savePrefID)}")
            cookieManager.setCookie(mUrl,
                "cookie_logged_in_nick=${ Utility.instance.getPref(context, AppKeyValue.instance.savePrefNick)}")
            cookieManager.setCookie(mUrl,
                "cookie_logged_in_gender=${ Utility.instance.getPref(context, AppKeyValue.instance.savePrefGender)}")
        }
    }

    private fun setMainColor(mUrl: String) {
        cookieManager.setCookie(mUrl, "azures-color-scheme=red2")
    }

}