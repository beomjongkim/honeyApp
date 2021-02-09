package com.dmonster.darling.honey.js

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.RewardVM
import com.dmonster.darling.honey.block_friends.view.BlockFriendsActivity
import com.dmonster.darling.honey.intro.data.IntroLoginData
import com.dmonster.darling.honey.intro.model.IntroLoginModel
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.webview.model.InappPurchaseModel
import com.dmonster.darling.honey.youtube.view.YoutubePlayerActivity
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_my_act_talk.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class JSHandler(
     var activity: AppCompatActivity,
     var webViewInterface: JSHandler.WebViewInterface? = null
 ) {
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
//                            Utility.instance.UserData().apply {
//                                setUserId(it.mbId)
//                                setUserMb(it.mbNo)
//                                setUserNick(it.mbNick)
//                                setUserRecommend(it.mbSn)
//                                setUserGender(it.mbSex)
//                                setUserDormant(it.mbSleep)
//                                setUserProfile(it.mbProfileState)
//                            }

                            it.mbId?.let { it1 ->

                                Log.e("idCheck","JSHandler set id : "+"")
                                Utility.instance.savePref(activity,
                                    AppKeyValue.instance.savePrefID,
                                    it1)
                            }
                            it.mbSex?.let { it1 ->
                                Utility.instance.savePref(activity,
                                    AppKeyValue.instance.savePrefGender,
                                    it1)
                            }
                            it.mbSleep?.let { it1 ->
                                Utility.instance.savePref(activity,
                                    AppKeyValue.instance.savePrefDormant,
                                    it1)
                            }
                            it.mbNick?.let { it1 ->
                                Utility.instance.savePref(activity,
                                    AppKeyValue.instance.savePrefNick,
                                    it1)
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
        Log.e("logoutCheck", "4")
        Utility.instance.setLogout(activity)
    }

    @JavascriptInterface
    fun logoutBridge() {
        Log.e("logoutCheck", "4")
        Utility.instance.setBridgeLogout(activity)
    }

    @JavascriptInterface
    fun initInappPurchase(id: String?){
        inappPurchaseModel =   InappPurchaseModel(id, activity, webViewInterface)
    }
    @JavascriptInterface
    fun inAppPurchase(pointUnit: String?) {
        //point Unit 예
        //point50,point100,point150
        inappPurchaseModel.let {
            Log.d("inAppPurchase", "unit : "+pointUnit)
            for (skuDetail in inappPurchaseModel.skuDetailList) {
                Log.d("inAppPurchase", "sku : "+skuDetail.sku)
                if (skuDetail.sku == pointUnit) {
                    inappPurchaseModel.doBillingFlow(skuDetail)
                    break;
                }
            }
        }
    }
    @JavascriptInterface
    fun goToBlockFriend(){
        activity.startActivity(Intent(activity, BlockFriendsActivity::class.java))
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
        Log.e("JSCheck","googleLogin")
        val signInIntent =googleSigneInClient.signInIntent
        startActivityForResult(activity, signInIntent, RC_GOOGLE_LOGIN, null)
    }

    @JavascriptInterface
     fun initSocialLogin(){
        Log.e("JSCheck","initSocialLogin")
        webViewInterface?.initSocialLogin()
    }
    @JavascriptInterface
    fun viewRefresh(){
        Log.e("JSCheck","viewRefresh")
    }
    @JavascriptInterface
    fun checkOwnFreepass(hasFreepass : Boolean) {
        Log.e("JSCheck","checkOwnFreepass")
        val pref = activity.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(AppKeyValue.instance.hasFreePass, hasFreepass)
        editor.apply()
        Log.d("checkOwnFreepass",hasFreepass.toString())
    }
     @JavascriptInterface
     fun showYoutube(){
//         webViewInterface?.showVideoAds()

         val sdf = SimpleDateFormat("yyyy-MM-dd")
         val currentDate = sdf.format(Date())
         var prefDate = Utility.instance.getPref(activity,AppKeyValue.instance.savePrefDayReward)

//         activity.startActivity(Intent(activity, YoutubePlayerActivity::class.java))
         if(currentDate != prefDate){
             activity.startActivity(Intent(activity, YoutubePlayerActivity::class.java))
         }else{
             Utility.instance.showToast(activity,
                 "무료이용권이 이미 지급완료 되었습니다.")
         }
     }
     @JavascriptInterface
     fun showOtherWebsite(uri : String){
         val browserIntent =
             Intent(Intent.ACTION_VIEW, Uri.parse(uri))
         activity.startActivity(browserIntent)
     }

    private fun configureGoogleSignIn() {
        // Configure Google Sign In
        //GoogleSignInOptions 옵션을 관리해주는 클래스로 API 키값과 요청할 값이 저장되어 있다.
        val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        googleSigneInClient= GoogleSignIn.getClient(activity, gso)
    }

    interface WebViewInterface{
         fun initSocialLogin()
         fun afterPurchase()
         fun showVideoAds()
         fun viewRefresh()
     }
}