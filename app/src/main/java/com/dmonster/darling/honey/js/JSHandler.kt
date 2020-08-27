package com.dmonster.darling.honey.js

import android.content.Context
import android.webkit.JavascriptInterface
import android.widget.Toast
import com.dmonster.darling.honey.intro.data.IntroLoginData
import com.dmonster.darling.honey.intro.model.IntroLoginModel
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class JSHandler(var context: Context) {

    @JavascriptInterface
    fun login(id: String?, password: String? = "", type: String?){
       val instanceId = FirebaseInstanceId.getInstance().token

        val mModel = IntroLoginModel()
        val subscription = CompositeDisposable()

        val subscriber = object: DisposableObserver<ResultItem<IntroLoginData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<IntroLoginData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.item?.let {
                            Utility.instance.UserData().apply {
                                setUserId(it.mbId)
                                setUserMb(it.mbNo)
                                setUserNick(it.mbNick)
                                setUserRecommend(it.mbSn)
                                setUserGender(it.mbSex)
                                setUserDormant(it.mbSleep)
                                setUserProfile(it.mbProfileState)
                            }
                        }
                    }
                }
            }
        }
        mModel.requestIntroLogin(id, password, instanceId, type, subscriber)
        subscription.add(subscriber)
    }

}