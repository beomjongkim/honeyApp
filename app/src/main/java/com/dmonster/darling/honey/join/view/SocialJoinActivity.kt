package com.dmonster.darling.honey.join.view

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.block_friends.view.BlockFriendsActivity
import com.dmonster.darling.honey.customview.DialogSelect
import com.dmonster.darling.honey.databinding.ActivitySocialJoinBinding
import com.dmonster.darling.honey.dialog.SelectorMemberDialog
import com.dmonster.darling.honey.information.view.PhoneAuthActivity
import com.dmonster.darling.honey.join.data.JoinData
import com.dmonster.darling.honey.join.viewmodel.PhoneCertVM
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.google.firebase.iid.FirebaseInstanceId
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_social_join.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class SocialJoinActivity : BaseActivity() {

    private lateinit var disposeBag: CompositeDisposable

    private lateinit var checkArrayId: Array<Int>
    private lateinit var checkArrayCheck: Array<CheckBox?>
    private lateinit var binding : ActivitySocialJoinBinding

    private var checkArray: ArrayList<CheckBox>? = null
    private var id: String? = null
    private var social: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_social_join)
//        setContentView(R.layout.activity_social_join)
        init()
        setListener()

    }


    private fun init() {
        setSocialLogin()
        bindViewModel()
        disposeBag = CompositeDisposable()

        checkArrayId = arrayOf(R.id.cb_act_social_join_male, R.id.cb_act_social_join_female)

        checkArray = ArrayList()
        checkArray?.add(cb_act_social_join_male)
        checkArray?.add(cb_act_social_join_female)

        if (social == "naver") {
            tv_act_social_join_title.text =
                resources.getString(R.string.social_login_naver_title)


            iv_act_social_join_logo.layoutParams.width =
                (Utility.instance.getWidth(this) * 0.5).roundToInt()
            this.let {
                Glide.with(it).load(R.drawable.naver_logo)
                    .into(iv_act_social_join_logo)
                tv_act_social_join_enter.setBackgroundColor(
                    ContextCompat.getColor(
                        it,
                        R.color.vibrant_green
                    )
                )
            }
        } else if (social == "kakao") {
            tv_act_social_join_title.text =
                resources.getString(R.string.social_login_kakao_title)
            this.let {
                Glide.with(it).load(R.drawable.kakao_logo)
                    .into(iv_act_social_join_logo)

                tv_act_social_join_enter.setBackgroundColor(
                    ContextCompat.getColor(
                        it,
                        R.color.dandelion
                    )
                )
            }

        } else if (social == "facebook") {
            tv_act_social_join_title.text =
                resources.getString(R.string.social_login_facebook_title)
            this.let {
                Glide.with(it).load(R.drawable.ic_facebook)
                    .into(iv_act_social_join_logo)

                tv_act_social_join_enter.setBackgroundColor(
                    ContextCompat.getColor(
                        it,
                        R.color.com_facebook_blue
                    )
                )
            }
        } else if (social == "google") {
            tv_act_social_join_title.text =
                resources.getString(R.string.social_login_google_title)
            this.let {
                Glide.with(it).load(R.drawable.ic_google)
                    .into(iv_act_social_join_logo)

                tv_act_social_join_enter.setBackgroundColor(
                    ContextCompat.getColor(
                        it,
                        R.color.salmon
                    )
                )
            }
        } else {

        }
    }

    private fun bindViewModel(){
        binding.phoneCertVM = object : PhoneCertVM() {
            override fun getActivity(): Activity {
                return this@SocialJoinActivity
            }

            override fun startActivityForResult() {
                val intent = Intent(this@SocialJoinActivity, PhoneAuthActivity::class.java)
                intent.putExtra(AppKeyValue.instance.intentPhone, phoneNumber.value)
                startActivity(intent)
            }

        }
    }

    private fun setListener() {
        checkArrayCheck = arrayOfNulls(2)
        for (i in checkArrayCheck.indices) {
            checkArrayCheck[i] = findViewById<CheckBox>(checkArrayId[i])

            checkArrayCheck[i]?.let { it ->
                RxView.clicks(it)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        setPress(i)
                    }
            }?.let { disposeBag.add(it) }
        }

        /*    대화명 입력    */
        et_act_social_join_talk.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
                val pattern =
                    resources.getString(R.string.utility_korean_number_only_regex).toRegex()
                if (!s.isNullOrBlank()) {
                    if (!pattern.matches(s)) {
                        cb_act_social_join_talk_check.isChecked = false
                        Utility.instance.showToast(
                            this@SocialJoinActivity,
                            resources.getString(R.string.msg_error_join_talk)
                        )
                        et_act_social_join_talk.setText("")

                    } else {
                        if (!TextUtils.isEmpty(s)) {
                            cb_act_social_join_talk_check.isChecked = s.length > 1
                        } else {
                            cb_act_social_join_talk_check.isChecked = false
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /*    태어난 년도    */
        disposeBag.add(RxView.clicks(rl_act_social_join_age)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val ageDialog = this.let { it1 ->
                    var ageArray = Utility.instance.getFormer(it1)
                    ageArray = Utility.instance.reArrangeGrid(ageArray)
                    SelectorMemberDialog(
                        it1,
                        AppKeyValue.instance.memberSelectThree,
                        AppKeyValue.instance.memberTypeNotLimit,
                        it1.resources.getString(R.string.information_member_dlg_age),
                        ageArray,
                        object : DialogSelect {
                            override fun onSelect(position: Int, o: Any) {
                                tv_act_social_join_age.text = ageArray[position]
                                cb_act_social_join_age_check.isChecked = true
                            }
                        })
                }
                ageDialog.setCanceledOnTouchOutside(false)
                ageDialog.show()
            })

        /*    지역    */
        disposeBag.add(RxView.clicks(rl_act_social_join_area)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val area01Array = resources.getStringArray(R.array.member_area_array)
                val area01Dialog = this.let { it1 ->
                    SelectorMemberDialog(
                        it1,
                        AppKeyValue.instance.memberSelectThree,
                        AppKeyValue.instance.memberTypeNotLimit,
                        it1.resources.getString(R.string.information_member_dlg_area),
                        area01Array,
                        object : DialogSelect {
                            override fun onSelect(position: Int, o: Any) {
                                val area01Position = position
                                val area = area01Array[position]
                                var area02Array: Array<String?> = emptyArray()
                                when (area) {
                                    area01Array[0] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_seoul_array)
                                    area01Array[1] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_gyeonggi_array)
                                    area01Array[2] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_guangwon_array)
                                    area01Array[3] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_gyeongnam_array)
                                    area01Array[4] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_gyeongbuk_array)
                                    area01Array[5] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_gwangju_array)
                                    area01Array[6] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_daegu_array)
                                    area01Array[7] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_daejeon_array)
                                    area01Array[8] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_busan_array)
                                    area01Array[9] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_sejong_array)
                                    area01Array[10] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_ulsan_array)
                                    area01Array[11] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_incheon_array)
                                    area01Array[12] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_jeonnam_array)
                                    area01Array[13] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_jeonbuk_array)
                                    area01Array[14] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_jeju_array)
                                    area01Array[15] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_chungnam_array)
                                    area01Array[16] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_chungbuk_array)
                                    area01Array[17] -> area02Array =
                                        it1.resources.getStringArray(R.array.member_area_foreign_array)
                                }

                                val area02Dialog =
                                    SelectorMemberDialog(
                                        it1,
                                        AppKeyValue.instance.memberSelectThree,
                                        AppKeyValue.instance.memberTypeNotLimit,
                                        it1.resources.getString(R.string.information_member_dlg_area),
                                        area02Array,
                                        object : DialogSelect {
                                            override fun onSelect(position: Int, o: Any) {
                                                val area02Position = position
                                                tv_act_social_join_area.text = String.format(
                                                    it1.resources.getString(R.string.arrow),
                                                    area01Array[area01Position],
                                                    area02Array[area02Position]
                                                )
                                                cb_act_social_join_area_check.isChecked = true
                                            }
                                        })
                                area02Dialog.setCanceledOnTouchOutside(false)
                                area02Dialog.show()
                            }
                        })
                }
                area01Dialog.setCanceledOnTouchOutside(false)
                area01Dialog.show()
            })

        /*    취소    */
        disposeBag.add(RxView.clicks(tv_act_social_join_cancel)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                this.let { it1 ->
                    Utility.instance.hideSoftKeyboard(it1)
                    onBackPressed()
                }
            })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_act_social_join_enter)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                this.let { it1 ->
                    val email = if (social == "naver") {
                        "naver.com"
                    } else if (social == "kakao") {
                        "kakao.com"
                    } else if (social == "facebook") {
                        "facebook.com"
                    } else if (social == "google") {
                        "gmail.com"
                    } else {
                        "unknown.com"
                    }
                    val talkId = et_act_social_join_talk.text.toString()
                    val age = tv_act_social_join_age.text.toString()
                        .replace(resources.getString(R.string.utility_date_former), "")
                    val area = tv_act_social_join_area.text.toString()
                    var area01: String? = null
                    var area02: String? = null
                    if (!TextUtils.isEmpty(area)) {
                        val areaArray =
                            area.split(" > ".toRegex()).dropLastWhile { it2 -> it2.isEmpty() }
                                .toTypedArray()
                        area01 = areaArray[0]
                        area02 = areaArray[1]
                    }
                    var gender: String? = "M"
                    when {
                        cb_act_social_join_male.isChecked -> gender = "M"
                        cb_act_social_join_female.isChecked -> gender = "F"
                    }
                    val recommendation = et_act_social_join_recommendation.text.toString()
                    val phone = et_act_social_join_phone_cert.text.toString()
                    val type = "sns"

                    if (TextUtils.isEmpty(talkId) || !cb_act_social_join_talk_check.isChecked) {
                        Utility.instance.showToast(
                            it1,
                            it1.resources.getString(R.string.msg_error_talk)
                        )
                    } else if (TextUtils.isEmpty(area)) {
                        Utility.instance.showToast(
                            it1,
                            resources.getString(R.string.msg_error_area_join)
                        )
                    } else if (TextUtils.isEmpty(age)) {
                        Utility.instance.showToast(
                            it1,
                            resources.getString(R.string.msg_error_age)
                        )
                    }  else if (!(cb_act_social_join_male.isChecked) && !(cb_act_social_join_female.isChecked)) {
                        Utility.instance.showToast(
                            it1,
                            it1.resources.getString(R.string.msg_error_gender)
                        )
                    } else if (!cb_act_social_join_phone_check.isChecked) {
                        Utility.instance.showToast(
                            this,
                            resources.getString(R.string.msg_error_phone_check)
                        )
                    } else {
                        this.let { it2 -> Utility.instance.hideSoftKeyboard(it2) }
                        ll_act_social_join_progress.visibility = View.VISIBLE
                        setSocialJoin(
                            id,
                            email,
                            talkId,
                            age,
                            area01,
                            area02,
                            gender,
                            recommendation,
                            phone,
                            type,
                            social
                        )
                    }
                }
            })
    }

    private fun setPress(position: Int) {
        checkArray?.let { it ->
            it.map {
                it.isChecked = false
            }
            it[position].isChecked = true

            if (position == 1) {
                ll_act_social_join_recommendation.visibility = View.GONE
            } else {
                ll_act_social_join_recommendation.visibility = View.GONE
            }
        }
    }

    /*    소셜 회원가입    */
    private fun setSocialJoin(
        id: String?,
        email: String?,
        talkId: String?,
        age: String?,
        area01: String?,
        area02: String?,
        gender: String?,
        recommendation: String?,
        phone: String?,
        type: String?,
        social: String?
    ) {
        val subscriber = object : DisposableObserver<ResultItem<JoinData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<JoinData>) {
                item.let { it ->
                    this@SocialJoinActivity.let { it1 ->
                        if (it.isSuccess) {
                            it.item?.mbId?.let { it2 ->
                                Utility.instance.savePref(
                                    it1,
                                    AppKeyValue.instance.savePrefID,
                                    it2
                                )
                            }
                            Utility.instance.savePref(
                                it1,
                                AppKeyValue.instance.savePrefPassword,
                                ""
                            )
                            Utility.instance.UserData().setUserMb(it.item?.mbNo)
                            Utility.instance.UserData().setUserGender(it.item?.mbSex)

                            val instanceId = FirebaseInstanceId.getInstance().token
                            setLogin(it.item?.mbId, instanceId)
                        } else {
                            ll_act_social_join_progress.visibility = View.GONE
                            Utility.instance.showToast(it1, it.message)
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestSocialJoin(
            ServerApi.instance.joinMethod,
            id,
            email,
            talkId,
            age,
            area01,
            area02,
            gender,
            recommendation,
            phone,
            type,
            social
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    /*    로그인    */
    private fun setLogin(id: String?, instanceId: String?) {
        val subscriber = object : DisposableObserver<ResultItem<LoginData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<LoginData>) {
                item.let { it ->
                    this@SocialJoinActivity.let { it1 ->
                        if (it.isSuccess) {
                            it.item?.let { it2 ->
                                val sucId = it2.mbId
                                val sucMemberNumber = it2.mbNo
                                val sucGender = it2.mbSex
                                val sucDormantState = it2.mbSleep
                                val sucProfileState = it2.mbProfileState

                                sucId?.let { it3 ->
                                    Utility.instance.savePref(
                                        it1,
                                        AppKeyValue.instance.savePrefID,
                                        it3
                                    )
                                }
                                Utility.instance.savePref(
                                    it1,
                                    AppKeyValue.instance.savePrefPassword,
                                    ""
                                )
                                Utility.instance.savePref(
                                    it1,
                                    AppKeyValue.instance.savePrefType,
                                    AppKeyValue.instance.keyTypeSocial
                                )
                                Utility.instance.UserData().apply {
                                    setUserMb(sucMemberNumber)
                                    setUserGender(sucGender)
                                    setUserDormant(sucDormantState)
                                    setUserProfile(sucProfileState)
                                }

                                ll_act_social_join_progress.visibility = View.GONE
                                Utility.instance.showTwoButtonAlert(
                                    this@SocialJoinActivity,
                                    "지인차단",
                                    "나를 알수도 있는 회원을 차단할 수 있습니다. 차단하시겠습니까?",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        if (which == DialogInterface.BUTTON_POSITIVE) {
                                            startActivity(
                                                Intent(
                                                    this@SocialJoinActivity,
                                                    BlockFriendsActivity::class.java
                                                )
                                            )
                                        } else {
                                            val intent = Intent(it1, MainActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                        }
                                    })
                            }
                        } else {
                            ll_act_social_join_progress.visibility = View.GONE
                            Utility.instance.showToast(it1, it.message)
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestLogin(
            ServerApi.instance.loginMethod,
            id,
            "",
            instanceId,
            AppKeyValue.instance.keyTypeSocial
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    fun setSocialLogin() {

        this.id = intent.getStringExtra("id")
        this.social = intent.getStringExtra("social")
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }

    override fun onStop() {
        super.onStop()
        Utility.instance.savePref(this, AppKeyValue.instance.phoneCert, "")
    }
    override fun onResume() {
        super.onResume()
        if (!Utility.instance.getPref(this, AppKeyValue.instance.phoneCert).isNullOrBlank()) {
            et_act_social_join_phone_cert.setText(Utility.instance.getPref(this, AppKeyValue.instance.phoneCert))
            cb_act_social_join_phone_check.isChecked = true
            et_act_social_join_phone_cert.isEnabled = false
        } else {
            cb_act_social_join_phone_check.isChecked = false
        }

    }
}
