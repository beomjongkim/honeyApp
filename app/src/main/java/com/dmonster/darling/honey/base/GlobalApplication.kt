package com.dmonster.darling.honey.base

import android.app.Application
import com.kakao.auth.KakaoSDK

class GlobalApplication: Application() {

//    @Volatile
//    private var obj: GlobalApplication? = null
//    @Volatile
//    private var currentActivity: Activity? = null

    companion object {
        @Volatile private var instance: GlobalApplication? = null

        val globalApplicationContext: GlobalApplication?
            get() {
                if (instance == null)
                    throw IllegalStateException("this application does not inherit com.kakao.GlobalApplication")
                return instance
            }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSDK.init(KakaoSDKAdapter())
    }

//    fun getGlobalApplicationContext(): GlobalApplication? {
//        return obj
//    }
//
//    fun getCurrentActivity(): Activity? {
//        return currentActivity
//    }
//
//    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
//    fun setCurrentActivity(currentActivity: Activity) {
//        GlobalApplication().currentActivity = currentActivity
//    }
}
