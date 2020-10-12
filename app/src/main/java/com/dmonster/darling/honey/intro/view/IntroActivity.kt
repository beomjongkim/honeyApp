package com.dmonster.darling.honey.intro.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.widget.ImageView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.intro.presenter.IntroLoginContract
import com.dmonster.darling.honey.intro.presenter.IntroLoginPresenter
import com.dmonster.darling.honey.login.view.LoginEmailActivity
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.VersionChecker
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import android.util.Base64
import com.dmonster.darling.honey.webview.view.WebViewActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class IntroActivity : BaseActivity(), IntroLoginContract.View {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private  var mPresenter: IntroLoginContract.Presenter? = null

    private var handler: Handler? = null
    private val runnable = Runnable {
        /*val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if(telephonyManager.simState == TelephonyManager.SIM_STATE_ABSENT) {
            Utility.instance.showAlert(this, resources.getString(R.string.app_name), resources.getString(R.string.msg_app_opening_phone), CustomDialogInterface.OnClickListener { dialog, which ->
                finish()
            })
        }
        else {*/
        val saveId = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        val savePassword = Utility.instance.getPref(this, AppKeyValue.instance.savePrefPassword)
        val saveType = Utility.instance.getPref(this, AppKeyValue.instance.savePrefType)
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN,Bundle().also {it.putString("user_id",saveId)})

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log
                Log.d(TAG, "FCM_token : " + token)

                if (!TextUtils.isEmpty(saveId) && !TextUtils.isEmpty(savePassword) || saveType == AppKeyValue.instance.keyTypeSocial) {
                    mPresenter?.setLogin(saveId, savePassword, token, saveType)
                } else {
                    val intent = Intent(this, LoginEmailActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            })

        /*}*/
    }
    private val REQUEST_NETWORK_CODE = 1
    private val REQUEST_PERMISSION_CODE = 2
    private val REQUEST_APP_VERSION = 3

    private val TAG = "Intro"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
        setContentView(R.layout.activity_intro)
        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
        init()
        checkNetwork()
    }

    private fun init() {
        handler = Handler()

        mPresenter = IntroLoginPresenter()
        mPresenter?.attachView(this)
        val imageView: ImageView = this.findViewById(R.id.iv_intro)
        imageView.let {

            mPresenter?.setRandomImageView(
                it,
                intArrayOf(
                    R.drawable.loding_page
                    )
            )
        }
    }

    /*    앱버전 확인    */
    fun checkAppInfo() {

        val appVersion = VersionChecker(packageName).execute().get()
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val version = packageInfo?.versionName

        if (version != appVersion) {
            Utility.instance.showTwoButtonAlert(
                this,
                resources.getString(R.string.app_name),
                resources.getString(R.string.msg_app_update),
                DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data =
                            Uri.parse(resources.getString(R.string.app_update) + packageName)
                        startActivityForResult(intent, REQUEST_APP_VERSION)
                    } else {
                        dialog.dismiss()
                        handler?.postDelayed(runnable, 500)
                    }
                },
                DialogInterface.OnKeyListener { dialog, keyCode, event ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        dialog.dismiss()
                        handler?.postDelayed(runnable, 500)
                        true
                    } else {
                        false
                    }
                }
            )
        } else {
            handler?.postDelayed(runnable, 2500)
        }
    }

//    /*    앱버전 호출실패    */
//    override fun setAppInfoFailed() {
//        handler?.postDelayed(runnable, 2500)
//    }

    /*    로그인    */
    override fun setLoginComplete(
        mbId : String?,
        mbNo: String?,
        mbNick : String?,
        recommend: String?,
        gender: String?,
        dormantState: String?,
        profileState: String?
    ) {
        Utility.instance.UserData().apply {
            setUserId(mbId)
            setUserMb(mbNo)
            setUserNick(mbNick)
            setUserRecommend(recommend)
            setUserGender(gender)
            setUserDormant(dormantState)
            setUserProfile(profileState)
        }

        intent = Intent(this, WebViewActivity::class.java)
        startActivity(intent)
        finish()
    }

    /*    로그인 호출실패    */
    override fun setLoginFailed(error: String?) {
        intent = Intent(this, LoginEmailActivity::class.java)
        startActivity(intent)
        finish()
    }

    /*    네트워크 상태확인    */
    private fun checkNetwork() {
        val checkNetwork = Utility.instance.isNetworkConnectedCheck(this)
        if (checkNetwork) {
            checkPermission()
        } else {
            Utility.instance.showAlert(
                this,
                resources.getString(R.string.app_name),
                resources.getString(R.string.msg_app_network_check),
                DialogInterface.OnClickListener { dialog, which ->
                    val intent = Intent(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS)
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    startActivityForResult(intent, REQUEST_NETWORK_CODE)
                })
        }
    }

    /*    권한 확인    */
    private fun checkPermission() {
        val permissionCheck1 =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionCheck2 =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        val permissionCheck3 = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val permissionCheck4 =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)

        val permissionArray = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
        )

        if (permissionCheck1 == PackageManager.PERMISSION_DENIED || permissionCheck2 == PackageManager.PERMISSION_DENIED
            || permissionCheck3 == PackageManager.PERMISSION_DENIED || permissionCheck4 == PackageManager.PERMISSION_DENIED
        ) {
            run {
                ActivityCompat.requestPermissions(
                    this,
                    permissionArray,
                    REQUEST_PERMISSION_CODE
                )
            }
        } else {
            checkAppInfo()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_NETWORK_CODE) {
            checkNetwork()
        } else if (requestCode == REQUEST_APP_VERSION) {
            checkAppInfo()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        Permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED
            ) {
                checkAppInfo()
            } else {
                Utility.instance.showToast(this, getString(R.string.msg_app_permission))
            }
        }
    }

    override fun onPause() {
        super.onPause()
        handler?.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler?.removeCallbacks(runnable)
        mPresenter?.detachView()
    }



}
