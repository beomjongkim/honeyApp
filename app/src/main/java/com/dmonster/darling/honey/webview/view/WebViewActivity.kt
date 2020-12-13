package com.dmonster.darling.honey.webview.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log

import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.ads.viewmodel.RewardVM
import com.dmonster.darling.honey.databinding.ActivityWebViewBinding
import com.dmonster.darling.honey.js.JSHandler
import com.dmonster.darling.honey.login.presenter.LoginContract
import com.dmonster.darling.honey.login.presenter.LoginPresenter
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.webview.viewmodel.WebViewmodel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId

import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WebViewActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: LoginContract.Presenter

    var activityWebViewBinding: ActivityWebViewBinding? = null
    private lateinit var webView: WebView
    var uploadMessage: ValueCallback<Array<Uri?>>? = null

    // 네이버 로그인
    private lateinit var mOAuthLoginModule: OAuthLogin
    private lateinit var OAUTH_CLIENT_ID: String
    private lateinit var OAUTH_CLIENT_SECRET: String
    private lateinit var OAUTH_CLIENT_NAME: String

    // 카카오 로그인
    private lateinit var kakaoCallback: SessionCallback

    //페이스북 로그인
    private lateinit var facebookCallbackManager: CallbackManager
    private lateinit var facebookCallback: FacebookCallback<LoginResult>

    //구글 로그인
    lateinit var auth: FirebaseAuth

    val RC_GOOGLE_LOGIN: Int = 101
    val RC_FACEBOOK_LOGIN: Int = 102
    val RC_KAKAO_LOGIN: Int = 103
    val RC_NAVER_LOGIN: Int = 104
    val RC_FILE_UPLOAD : Int = 105


    private var id: String? = null
    private var social: String? = null

    lateinit var rewardVM : RewardVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_web_view)
        val id = Utility.instance.getPref(this,AppKeyValue.instance.savePrefID)
        activityWebViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view)
        val url_home = "https://jjagiya.co.kr"
        var url = url_home + "/page-login-1.html"
        if (intent.hasExtra("link")) {
            url = url_home + intent.getStringExtra("link")
        }
        activityWebViewBinding?.let {
            it.webViewModel = WebViewmodel(application,
                url,
                JSHandler(this, object : JSHandler.WebViewInterface {
                    override fun initSocialLogin() {
                        initializeSocialLogin()
                    }

                    override fun afterPurchase() {
                        webView.reload()
                    }

                    override fun showVideoAds() {
                        rewardVM.rewardedAd.show(this@WebViewActivity, rewardVM.adCallBackBase)
                    }
                }),
                lifecycle
            )
            it.bannerVM = BannerVM(id, lifecycle, this)
            webView = it.wvWebview
            it.lifecycleOwner = this
        }

        init()

    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mPresenter = LoginPresenter()
        mPresenter.attachView(this)
        rewardVM = RewardVM(this)
        rewardVM.adCallback = object : RewardedAdCallback() {

            override fun onRewardedAdOpened() {
                // Ad opened.
                Log.d("showAds", "Ad opened.")
            }

            override fun onRewardedAdClosed() {
                // Ad closed.
                Log.d("showAds", "Ad closed.")
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {

                // User earned reward.
                val subscriber = object : DisposableObserver<ResultItem<String>>() {
                    override fun onComplete() {
                    }

                    override fun onError(e: Throwable) {
                    }

                    override fun onNext(item: ResultItem<String>) {
                        item.let { it ->
                            if (it.isSuccess) {
                                this@WebViewActivity.let { it1 -> Utility.instance.showToast(it1, "성공적으로 이용권을 구매하였습니다.") }
                            }
                        }
                    }
                }
                val id = Utility.instance.getPref(this@WebViewActivity,AppKeyValue.instance.savePrefID)
                ItemModel().buyItem(id, 1, subscriber)
                Log.d("showAds", "User earned reward.")
            }

            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
                Log.d("showAds", "Ad failed to display.")
            }
        }


        webView.webChromeClient = object : WebChromeClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri?>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                val context = this@WebViewActivity
                //        일단은 image 를 업로드한다는 가정에서 작업 .
                val acceptType =
                    fileChooserParams?.acceptTypes?.get(0).toString() //access 타입을 체크합니다 .
                // filePathCallback : ValueCallback < Array < Uri ? > 이놈의 경우 한번 오픈했으면 반드시 초기화 해줘야합니다.
                // 확인또확인!안 그러면 두번다시 안열리거나 에러를 뱉습니다.
                if (uploadMessage != null) {
                    uploadMessage!!.onReceiveValue(null)
                }

                val permissionCheck1 =
                    ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                val permissionCheck2 =
                    ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCheck3 =
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                uploadMessage = filePathCallback
                if (permissionCheck1 == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED && permissionCheck3 == PackageManager.PERMISSION_GRANTED) { //권한이 있다면
//                    권한이 있다면 예제는 파일chooser를 이용해서 파일처리해보겠습니다 .
                    val contentSelectionIntent = Intent()

                    //Intent . ACTION_GET_CONTENT
                    // if (mode == FILE_MULTIPLE_SELECT) {
//                    멀티플 처리한다면
                    contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//                }
                    contentSelectionIntent.action = Intent.ACTION_GET_CONTENT
                    contentSelectionIntent.addCategory(
                        Intent.CATEGORY_OPENABLE
                    )
                    contentSelectionIntent.type = "image/*"
                    startActivityForResult(contentSelectionIntent, RC_FILE_UPLOAD)
                } else {
                    //… 권한이 없을경우 작업 처리 //… //얘는 무조건 해줘야 합니다!!!
                    if (uploadMessage != null) {
                        uploadMessage!!.onReceiveValue(null)
                    }
                    uploadMessage = null
                }
                return true
            }
        }

    }

    private fun initializeSocialLogin() {
        //구글 로그인
        // Configure Google Sign In
        auth = FirebaseAuth.getInstance()

        //        facebook 로그인
        facebookCallbackManager = CallbackManager.Factory.create()
        facebookCallback = object :
            FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                id = result?.accessToken?.userId
                val loginId = "$id@facebook.com"
                val instanceId = FirebaseInstanceId.getInstance().token
                social = "facebook"
                mPresenter.setSocialLogin(
                    loginId,
                    instanceId,
                    AppKeyValue.instance.keyTypeSocial
                )
            }

            override fun onCancel() {
                Utility.instance.showToast(this@WebViewActivity,
                    getString(R.string.facebook_login_canceled))
            }

            override fun onError(error: FacebookException?) {
                Utility.instance.showToast(this@WebViewActivity,
                    getString(R.string.facebook_login_error_occurred))
                Log.e("facebookLogin", error.toString())
            }

        }

        /*    네이버 로그인    */
        OAUTH_CLIENT_ID = getString(R.string.NAVER_CLIENT_ID)
        OAUTH_CLIENT_SECRET = getString(R.string.NAVER_CLIENT_SECRET)
        OAUTH_CLIENT_NAME = getString(R.string.NAVER_CLIENT_NAME)

        mOAuthLoginModule = OAuthLogin.getInstance()
        mOAuthLoginModule.init(
            this, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME
            //,OAUTH_CALLBACK_INTENT
            // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT 변수를 사용하지 않습니다.
        )

        /*    카카오 로그인    */
        kakaoCallback = SessionCallback()
        Session.getCurrentSession().addCallback(kakaoCallback)
        Session.getCurrentSession().checkAndImplicitOpen()

    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("resultCode:: ", resultCode.toString())
        Log.d("requestCode:: ", requestCode.toString())
        if(requestCode == RC_FILE_UPLOAD){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var results: Array<Uri?>? = null
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (uploadMessage == null) {
                            return
                        }

                        if (data != null) {
                            val dataString = data.dataString
                            val clipData = data.clipData
                            //멀티선택
                            if (clipData != null) {
                                results = arrayOfNulls(clipData.itemCount)
                                for (i in 0 until clipData.itemCount) {
                                    val item = clipData.getItemAt(i)
                                    results[i] = item.uri
                                }
                            }
                            //단일선택
                            if (dataString != null) {
                                results = arrayOf(Uri.parse(dataString))
                            }

                        }
                        uploadMessage!!.onReceiveValue(results)
                        uploadMessage = null
                    } catch (e: Exception) {
                        Log.i("WebViewActivity", e.message)
                        Toast.makeText(this, "업로드실패 ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        if (requestCode == RC_GOOGLE_LOGIN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("googleLogin", "Google sign in failed", e)
                // ...
            }
        }
        if (requestCode == RC_KAKAO_LOGIN) {
            if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                return
            }
        }
        if (requestCode == RC_FACEBOOK_LOGIN)
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("googleLogin", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("googleLogin", "signInWithCredential:success")
                    Log.d("googleLogin", acct.toString())
                    id = acct.id
                    val email = id + "@gmail.com"
                    val instanceId = FirebaseInstanceId.getInstance().token
                    social = "google"
                    mPresenter.setSocialLogin(
                        email,
                        instanceId,
                        AppKeyValue.instance.keyTypeSocial
                    )

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("googleLogin",
                        "signInWithCredential:failure" + task.result.toString(),
                        task.exception)
                }

                // ...
            }
    }

    override fun setLoginComplete(
        loginId: String?,
        mbNo: String?,
        recommend: String?,
        gender: String?,
        mbNick: String?,
        dormantState: String?,
        profileState: String?
    ) {
        loginId?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefID, it) }
        mbNo?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefMbNumber, it) }
        mbNick?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefNick, it) }
        gender?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefGender, it) }
        dormantState?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefDormant, it) }
    }

    override fun setLoginFailed(error: String?) {
    }

    override fun setSocialLoginComplete(
        loginId: String?,
        mbNo: String?,
        recommend: String?,
        gender: String?,
        mbNick: String?,
        dormantState: String?,
        profileState: String?
    ) {
        loginId?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefID, it) }
        mbNo?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefMbNumber, it) }
        mbNick?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefNick, it) }
        gender?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefGender, it) }
        dormantState?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefDormant, it) }
//        Utility.instance.UserData().apply {
//            setUserId(loginId)
//            setUserMb(mbNo)
//            setUserRecommend(recommend)
//            setUserGender(gender)
//            setUserNick(mbNick)
//            setUserDormant(dormantState)
//            setUserProfile(profileState)
//        }


//        Utility.instance.savePref(this, AppKeyValue.instance.savePrefPassword, loginPassword)
//        Utility.instance.savePref(
//            this,
//            AppKeyValue.instance.savePrefType,
//            AppKeyValue.instance.keyTypeBasic
//        )
        activityWebViewBinding?.webViewModel?.login("https://jjagiya.co.kr")
        webView.loadUrl("https://jjagiya.co.kr/home.html")

    }

    override fun setSocialLoginFailed() {
    }

    override fun onStop() {
        super.onStop()

        /*    카카오 로그아웃    */
        kakaoSetLogout(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
        Session.getCurrentSession().removeCallback(kakaoCallback)
        Utility.instance.hideSoftKeyboard(this)
    }

    /*    네이버 로그인    */
    private val mOAuthLoginHandler = @SuppressLint("HandlerLeak")
    object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            val context = this@WebViewActivity
            if (success) {
                Thread(Runnable {
                    val token =
                        mOAuthLoginModule.getAccessToken(context)    //네이버 로그인 접근 토큰
                    val header = "Bearer $token"    //Bearer 다음에 공백 추가

                    try {
                        val apiURL = "https://openapi.naver.com/v1/nid/me"
                        val url = URL(apiURL)
                        val con = url.openConnection() as HttpURLConnection
                        con.requestMethod = "GET"
                        con.setRequestProperty("Authorization", header)
                        val responseCode = con.responseCode
                        val br: BufferedReader

                        br = if (responseCode == 200) {    //정상 호출
                            BufferedReader(InputStreamReader(con.inputStream))
                        } else {    //에러 발생
                            BufferedReader(InputStreamReader(con.errorStream))
                        }

                        var inputLine: String
                        val response = StringBuffer()

                        while (true) {
                            inputLine = br.readLine() ?: break
                            response.append(inputLine)
                        }

                        br.close()

                        val jo = JSONObject(response.toString())
                        val joResponse = jo.getJSONObject("response")

                        id = joResponse.getString("id")
                        social = "naver"

                        val loginId = "$id@naver.com"
                        val instanceId = FirebaseInstanceId.getInstance().token
                        mPresenter.setSocialLogin(
                            loginId,
                            instanceId,
                            AppKeyValue.instance.keyTypeSocial
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }).start()
            } else {

                val errorCode = mOAuthLoginModule.getLastErrorCode(context).code
                val errorDesc = mOAuthLoginModule.getLastErrorDesc(context)
                Log.e(
                    "WebViewActivity", "errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc
                )
                Utility.instance.showToast(
                    context,
                    "네이버 로그인에 실패했습니다. 다른 방식을 시도하거나, 잠시 후 다시 로그인해주세요."
                )
            }
        }
    }

    /*    카카오 로그인    */
    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            requestMeKakao()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Log.e("kakaoLogin", exception.localizedMessage)
            }
        }
    }

    /*    카카오 사용자정보 받아오기    */
    private fun requestMeKakao() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                result?.let {
                    id = it.id.toString()

                    val email = if (it.kakaoAccount.email != null) {
                        it.kakaoAccount.email
                    } else {
                        // 이메일 획득 불가
                        id + "@kakao.com"
                    }

                    social = "kakao"
                    val instanceId = FirebaseInstanceId.getInstance().token
                    mPresenter.setSocialLogin(email, instanceId, AppKeyValue.instance.keyTypeSocial)
                }

            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                if (errorResult != null) {
                    Log.e("kakaoLogin", errorResult.errorMessage)
                }
            }

        })
    }

    /*    카카오 로그아웃    */
    private fun kakaoSetLogout(action: Runnable?) {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                action?.run()
            }

            override fun onSuccess(userId: Long) {

            }
        })
    }

    override fun onResume() {
        super.onResume()
//        webView.reload()

    }

}
