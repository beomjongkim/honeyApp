package com.dmonster.darling.honey.js

import android.app.Activity
import android.content.Context
import android.webkit.JavascriptInterface
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingResult
import com.dmonster.darling.honey.intro.data.IntroLoginData
import com.dmonster.darling.honey.intro.model.IntroLoginModel
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.webview.model.InappPurchaseModel
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class JSHandler(var activity: AppCompatActivity) {
       lateinit var inappPurchaseModel : InappPurchaseModel

    @JavascriptInterface
    fun login(id: String?, password: String? = "", type: String?) {
        val instanceId = FirebaseInstanceId.getInstance().token

        val mModel = IntroLoginModel()
        val subscription = CompositeDisposable()

        val subscriber = object : DisposableObserver<ResultItem<IntroLoginData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {

                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<IntroLoginData>) {
                item.let { it ->
                    if (it.isSuccess) {
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

    @JavascriptInterface
    fun logout() {
        Utility.instance.setLogout(activity)
    }

    @JavascriptInterface
    fun initInappPurchase(id: String?){
        inappPurchaseModel = InappPurchaseModel(id, activity)
    }
    @JavascriptInterface
    fun inAppPurchase(pointUnit: String?) {
        //point Unit 예
        //point50,point100,point150
        inappPurchaseModel.let {
            for (skuDetail in inappPurchaseModel.skuDetailList) {
                if (skuDetail.sku == pointUnit) {
                    inappPurchaseModel.doBillingFlow(skuDetail)
                    break;
                }
            }
        }
    }
    @JavascriptInterface
    fun inAppPurchase(){

    }



}