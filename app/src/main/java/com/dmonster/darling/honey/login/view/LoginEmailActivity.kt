package com.dmonster.darling.honey.login.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.customview.CustomArrayDialog
import com.dmonster.darling.honey.join.view.SocialJoinActivity
import com.dmonster.darling.honey.join.view.JoinActivity
import com.dmonster.darling.honey.login.presenter.LoginContract
import com.dmonster.darling.honey.login.presenter.LoginPresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.webview.view.WebViewActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.jakewharton.rxbinding2.view.RxView
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.OptionalBoolean
import com.kakao.util.exception.KakaoException
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login_email.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit

class LoginEmailActivity : BaseActivity(), LoginContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: LoginContract.Presenter

    // 네이버 로그인
    private lateinit var mOAuthLoginModule: OAuthLogin
    private lateinit var OAUTH_CLIENT_ID: String
    private lateinit var OAUTH_CLIENT_SECRET: String
    private lateinit var OAUTH_CLIENT_NAME: String

    // 카카오 로그인
    private lateinit var kakaoCallback: SessionCallback
    private var isKakaoLoginMode = false

    //페이스북 로그인
    private lateinit var facebookCallbackManager: CallbackManager
    private lateinit var facebookCallback: FacebookCallback<LoginResult>
    private var isFacebookLoginMode = false

    //구글 로그인


    lateinit var auth :FirebaseAuth
    lateinit var authListener : FirebaseAuth.AuthStateListener
    lateinit var googleSigneInClient : GoogleSignInClient
    val RC_SIGN_IN: Int = 100
    private var isGoogleLoginMode = false




    private var id: String? = null
    private var social: String? = null
    private var intentPW: Boolean? = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_email)

        init()
        setListener()

        /*    네이버 로그아웃    */
        mOAuthLoginModule.logout(this)
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mPresenter = LoginPresenter()
        mPresenter.attachView(this)

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
                Utility.instance.showToast(this@LoginEmailActivity, getString(R.string.facebook_login_canceled))
            }

            override fun onError(error: FacebookException?) {
                Utility.instance.showToast(this@LoginEmailActivity, getString(R.string.facebook_login_error_occurred))
                Log.e("facebookLogin",error.toString())
            }

        }


        //구글 로그인
        // Configure Google Sign In
       configureGoogleSignIn()
        auth = FirebaseAuth.getInstance()

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


        //이전에 로그인한 기록이 있다면 아이디 세팅
//        val prefId = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
//        if(!TextUtils.isEmpty(prefId)) {
//            val prefArray = prefId.split("@")
//            val id = prefArray[0]
//            val email = prefArray[1]
//            et_act_login_id.setText(id)
//            tv_act_login_email.text = email
//        }
    }

    override fun setSocialLoginComplete(
        loginId: String?,
        mbNo: String?,
        recommend: String?,
        gender: String?,
        mbNick : String?,
        dormantState: String?,
        profileState: String?
    ) {


        loginId?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefID, it) }
        Utility.instance.UserData().apply {
            setUserMb(mbNo)
            setUserRecommend(recommend)
            setUserGender(gender)
            setUserNick(mbNick)
            setUserDormant(dormantState)
            setUserProfile(profileState)
        }

        val loginPassword = et_act_login_password.text.toString()
        Utility.instance.savePref(this, AppKeyValue.instance.savePrefPassword, loginPassword)
        Utility.instance.savePref(
            this,
            AppKeyValue.instance.savePrefType,
            AppKeyValue.instance.keyTypeSocial
        )

        ll_act_login_progress.visibility = View.GONE

        val intent = Intent(this, WebViewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    override fun setSocialLoginFailed() {
        val intent = Intent(this, SocialJoinActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("social", social)
        startActivity(intent)
    }

    private fun setListener() {
        /*    이메일 선택    */
        disposeBag.add(RxView.clicks(rl_act_login_email)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val emailArray = resources.getStringArray(R.array.login_email_array)
                val emailDialog = object : CustomArrayDialog(
                    this,
                    resources.getString(R.string.hint_login_email),
                    resources.getString(R.string.dialog_title),
                    emailArray
                ) {
                    override fun onConfirm() {
                        val position = dialogVM.selectedIndex
                        if (position == emailArray.size.minus(1)) {
                            this@LoginEmailActivity.tv_act_login_email.visibility = View.GONE
                            this@LoginEmailActivity.et_act_login_email.visibility = View.VISIBLE
                        } else {
                            this@LoginEmailActivity.tv_act_login_email.visibility = View.VISIBLE
                            this@LoginEmailActivity.et_act_login_email.visibility = View.GONE

                            this@LoginEmailActivity.tv_act_login_email.text = emailArray[position]
                        }
                    }
                }

                emailDialog.setCanceledOnTouchOutside(false)
                emailDialog.show()
            })

        /*    로그인    */
        disposeBag.add(RxView.clicks(tv_act_login_login)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val id = et_act_login_id.text.toString()
                val email = tv_act_login_email.text.toString()
                val emailEdit = et_act_login_email.text.toString()
                val password = et_act_login_password.text.toString()

                val loginId: String = if (et_act_login_email.visibility == View.VISIBLE) {
                    "$id@$emailEdit"
                } else {
                    "$id@$email"
                }
                val isValidEmail = loginId.let { it1 -> Utility.instance.isValidEmail(it1) }

                if (TextUtils.isEmpty(id)) {
                    Utility.instance.showToast(this, resources.getString(R.string.msg_error_id))
                } else if ((tv_act_login_email.visibility == View.VISIBLE && TextUtils.isEmpty(
                        email
                    )) || (et_act_login_email.visibility == View.VISIBLE && TextUtils.isEmpty(
                        emailEdit
                    )) || !isValidEmail
                ) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_email)
                    )
                } else if (TextUtils.isEmpty(password)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_password)
                    )
                } else {
                    ll_act_login_progress.visibility = View.VISIBLE
                    val loginPassword = et_act_login_password.text.toString()
                    val instanceId = FirebaseInstanceId.getInstance().token
                    mPresenter.setLogin(
                        loginId,
                        loginPassword,
                        instanceId,
                        AppKeyValue.instance.keyTypeBasic
                    )
                }
            })

//        /*    고객센터    */
//        disposeBag.add(RxView.clicks(tv_act_login_service_center)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe {
//                    val intent = Intent(this, ServiceCenterActivity::class.java)
//                    startActivity(intent)
//                })

        /*    아이디 & 비밀번호 찾기    */
        disposeBag.add(RxView.clicks(tv_act_login_find_id_password)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, FindIDPWActivity::class.java)
                startActivityForResult(intent, AppKeyValue.instance.requestFindIDPW)
            })

//        /*    비밀번호찾기    */
//        disposeBag.add(RxView.clicks(tv_act_login_find_password)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe {
//                    /*val findPWDialog = FindPWDialog()
//                    findPWDialog.show(supportFragmentManager, AppKeyValue.instance.tagFindPWDlg)*/
//                    val intent = Intent(this, FindIDPWActivity::class.java)
//                    startActivityForResult(intent, AppKeyValue.instance.requestFindIDPW)
//                })
        /*    카카오 로그인    */
        disposeBag.add(RxView.clicks(ll_act_login_kakao)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                isKakaoLoginMode = true
                Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this)
            })

        /*    네이버 로그인    */
        disposeBag.add(RxView.clicks(ll_act_login_naver)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler)
            })

        /*    페이스북 로그인    */
        disposeBag.add(RxView.clicks(ll_act_login_facebook)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                isFacebookLoginMode = true
                val loginManager = LoginManager.getInstance()
                loginManager.logInWithReadPermissions(this, listOf("public_profile", "email","user_friends"))
                loginManager.registerCallback(facebookCallbackManager, facebookCallback)

            })
        /*    구글 로그인    */
        disposeBag.add(RxView.clicks(ll_act_login_google)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                isGoogleLoginMode = true
                val signInIntent =googleSigneInClient.signInIntent
                startActivityForResult(signInIntent,RC_SIGN_IN)

            })
        /*    회원가입    */
        disposeBag.add(RxView.clicks(ll_act_login_join)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, JoinActivity::class.java)
                startActivity(intent)
            })
    }

    /*    로그인    */
    override fun setLoginComplete(
        loginId: String?,
        mbNo: String?,
        recommend: String?,
        gender: String?,
        nick : String?,
        dormantState: String?,
        profileState: String?
    ) {
        loginId?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefID, it) }
        Utility.instance.UserData().apply {
            setUserId(loginId)
            setUserMb(mbNo)
            setUserRecommend(recommend)
            setUserGender(gender)
            setUserNick(nick)
            setUserDormant(dormantState)
            setUserProfile(profileState)
        }

        val loginPassword = et_act_login_password.text.toString()
        Utility.instance.savePref(this, AppKeyValue.instance.savePrefPassword, loginPassword)
        Utility.instance.savePref(
            this,
            AppKeyValue.instance.savePrefType,
            AppKeyValue.instance.keyTypeBasic
        )

        ll_act_login_progress.visibility = View.GONE

        val intent = Intent(this, WebViewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra(AppKeyValue.instance.findPW, intentPW)
        startActivity(intent)
        finish()
    }

    /*    로그인 호출실패    */
    override fun setLoginFailed(error: String?) {
        ll_act_login_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (isKakaoLoginMode) {
            if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
                return
            }
        }

        if (isFacebookLoginMode)
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data)


        if (resultCode == RESULT_OK) {
            if (requestCode == AppKeyValue.instance.requestFindIDPW) {
                val intentID = data?.getStringExtra(AppKeyValue.instance.findID)
                val intentIDArray = intentID?.split("@")
                when (intentIDArray?.size) {
                    2 -> {
                        val id = intentIDArray[0]
                        val email = intentIDArray[1]

                        et_act_login_id.setText(id)
                        tv_act_login_email.text = email
                    }
                }
                intentPW = data?.getBooleanExtra(AppKeyValue.instance.findPW, false)
            }
        }

        if (requestCode == RC_SIGN_IN) {
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
        super.onActivityResult(requestCode, resultCode, data)
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
            val context = this@LoginEmailActivity
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
                    "LoginEmailActivity", "errorCode:" + errorCode
                            + ", errorDesc:" + errorDesc
                )
                Utility.instance.showToast(
                    context,
                    "네이버 로그인에 실패했습니다. 다른 방식을 시도하거나, 잠시 후 다시 로그인해주세요."
                )
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("googleLogin", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("googleLogin", "signInWithCredential:success")
                    Log.d("googleLogin",acct.toString())
                    id = acct.id
                    val email = id+"@gmail.com"
                    val instanceId = FirebaseInstanceId.getInstance().token
                    social = "google"
                    mPresenter.setSocialLogin(
                        email,
                        instanceId,
                        AppKeyValue.instance.keyTypeSocial
                    )

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("googleLogin", "signInWithCredential:failure"+task.result.toString(), task.exception)
                }

                // ...
            }
    }

    /*    카카오 로그인    */
    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            requestMeKakao()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Log.e("kakaoLogin",exception.localizedMessage)
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
                    Log.e("kakaoLogin",errorResult.errorMessage)
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
    private fun configureGoogleSignIn() {
        // Configure Google Sign In
        //GoogleSignInOptions 옵션을 관리해주는 클래스로 API 키값과 요청할 값이 저장되어 있다.
        val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();

        googleSigneInClient=GoogleSignIn.getClient(this,gso)
    }

}
