package com.dmonster.darling.honey.webview.view

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity
import java.net.URISyntaxException

class CustomWebviewClient : WebViewClient() {
    // 코틀린
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url != null && url.startsWith("intent://")) {
            try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                val existPackage = view.context.packageManager.getLaunchIntentForPackage(intent.getPackage()!!)
                if (existPackage != null) {
                    view.context.startActivity(intent)
                } else {
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data =
                        Uri.parse("market://details?id=" + intent.getPackage()!!)
                    view.context.startActivity(marketIntent)
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (url != null && url.startsWith("market://")) {
            try {
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                if (intent != null) {
                    view.context.startActivity(intent)
                }
                return true
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }

        }
        view.loadUrl(url)
        return false
    }
}