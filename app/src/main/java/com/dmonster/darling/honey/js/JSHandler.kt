package com.dmonster.darling.honey.js

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import com.android.billingclient.api.BillingResult
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.block_friends.view.BlockFriendsActivity
import com.dmonster.darling.honey.intro.data.IntroLoginData
import com.dmonster.darling.honey.intro.model.IntroLoginModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.webview.model.InappPurchaseModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

 class JSHandler(var activity: AppCompatActivity, var webViewInterface: JSHandler.WebViewInterface? = null) {
    lateinit var inappPurchaseModel : InappPurchaseModel
    val RC_GOOGLE_LOGIN: Int = 101
    val RC_FACEBOOK_LOGIN: Int = 102
    val RC_KAKAO_LOGIN: Int = 103
    val RC_NAVER_LOGIN: Int = 104

    lateinit var googleSigneInClient : GoogleSignInClient

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

                                it.mbId?.let { it1 ->
                                    Utility.instance.savePref(activity, AppKeyValue.instance.savePrefID, it1)
                                }
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
    fun goToBlockFriend(){
        activity.startActivity(Intent(activity,BlockFriendsActivity::class.java))
    }

    @JavascriptInterface
    fun deleteCache(){
        activity.cacheDir.deleteRecursively()
    }


    @JavascriptInterface
    fun facebookLogin(){

    }


    @JavascriptInterface
    fun setGoogleLogin(){
        configureGoogleSignIn()
    }
    @JavascriptInterface
    fun googleLogin(){
        val signInIntent =googleSigneInClient.signInIntent
        startActivityForResult( activity,signInIntent,RC_GOOGLE_LOGIN, null)
    }

    @JavascriptInterface
     fun initSocialLogin(){
        webViewInterface?.initSocialLogin()
    }

    private fun configureGoogleSignIn() {
        // Configure Google Sign In
        //GoogleSignInOptions 옵션을 관리해주는 클래스로 API 키값과 요청할 값이 저장되어 있다.
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        googleSigneInClient= GoogleSignIn.getClient(activity,gso)
    }

     interface WebViewInterface{
         fun initSocialLogin()
     }
}