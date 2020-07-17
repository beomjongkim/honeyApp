package com.dmonster.darling.honey.information.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.customview.CustomArrayDialog
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.customview.DialogSelect
import com.dmonster.darling.honey.databinding.ActivityMyInfoBinding
import com.dmonster.darling.honey.dialog.SelectorMemberDialog
import com.dmonster.darling.honey.dialog.WithdrawalReDialog
import com.dmonster.darling.honey.information.data.MyInfoData
import com.dmonster.darling.honey.information.presenter.MyInfoContract
import com.dmonster.darling.honey.information.presenter.MyInfoPresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_my_info.*
import java.util.*
import java.util.concurrent.TimeUnit

class MyInfoActivity : BaseActivity(), MyInfoContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: MyInfoContract.Presenter

    private lateinit var marryArrayId: Array<Int>
    private lateinit var marryArrayCheck: Array<CheckBox?>

    private var marryArray: ArrayList<CheckBox>? = null
    private var id: String? = null
    private var mbNo: String? = null
    private var isModifyMode = false

    private var oriTalkId: String? = null
    private var oriAge: String? = null
    private var oriType: String? = null

    private var customPopup: CustomPopup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_info)
        val binding : ActivityMyInfoBinding = DataBindingUtil.setContentView(this,R.layout.activity_my_info)
        binding.bannerVM =
            BannerVM(Utility.instance.getPref(this, AppKeyValue.instance.savePrefID), lifecycle,this)
        binding.lifecycleOwner = this
        init()
        setListener()
        setModifyMode()
    }

    private fun init() {
        ctb_act_my_info_toolbar.setTitle(resources.getString(R.string.my_info_title))
        disposeBag = CompositeDisposable()

        marryArrayId = arrayOf(R.id.cb_act_my_info_marry, R.id.cb_act_my_info_remarry)

        marryArray = ArrayList()
        marryArray?.add(cb_act_my_info_marry)
        marryArray?.add(cb_act_my_info_remarry)

        mPresenter = MyInfoPresenter()
        mPresenter.attachView(this)

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)

        mbNo = Utility.instance.UserData().getUserMb()

        ll_act_my_info_progress.visibility = View.VISIBLE
        mPresenter.getMyInfo(id, mbNo)
    }

    private fun setListener() {
        /*    회원유형    */
        marryArrayCheck = arrayOfNulls(2)
        for (i in marryArrayCheck.indices) {
            marryArrayCheck[i] = findViewById<CheckBox>(marryArrayId[i])

            marryArrayCheck[i]?.let { it ->
                RxView.clicks(it)
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe {
                        setPress(i)
                    }
            }?.let { disposeBag.add(it) }
        }

        /*    로그아웃    */
        disposeBag.add(RxView.clicks(tv_act_my_info_logout)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                Utility.instance.hideSoftKeyboard(this)
                Utility.instance.showTwoButtonAlert(
                    this,
                    resources.getString(R.string.app_name),
                    resources.getString(R.string.msg_app_logout),
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            Utility.instance.showToast(
                                this,
                                resources.getString(R.string.msg_app_logout_complete)
                            )
                            Utility.instance.setLogout(this)
                        }
                    })
            })

        /*    대화명 입력    */
        et_act_my_info_talk_id.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
                val talkId = et_act_my_info_talk_id.text.toString()
//                if(!TextUtils.isEmpty(talkId)) {
//                    cb_act_my_info_talk_id_check.isChecked = talkId.length > 2
//                }
//                else {
//                    cb_act_my_info_talk_id_check.isChecked = false
//                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /*    기존 비밀번호 입력    */
        et_act_my_info_password.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
                val password = et_act_my_info_password.text.toString()
//                if(!TextUtils.isEmpty(password)) {
//                    cb_act_my_info_password_check.isChecked = password.length > 3
//                }
//                else {
//                    cb_act_my_info_password_check.isChecked = false
//                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /*    새 비밀번호 입력    */
        et_act_my_info_new_password.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
                val newPassword = et_act_my_info_new_password.text.toString()
//                if(!TextUtils.isEmpty(newPassword)) {
//                    cb_act_my_info_new_password_check.isChecked = newPassword.length > 3
//                }
//                else {
//                    cb_act_my_info_new_password_check.isChecked = false
//                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /*    새 비밀번호 확인 입력    */
        et_act_my_info_new_password_check.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
                val newPasswordCheck = et_act_my_info_new_password_check.text.toString()
//                if(!TextUtils.isEmpty(newPasswordCheck)) {
//                    cb_act_my_info_new_password_check_check.isChecked = newPasswordCheck.length > 3
//                }
//                else {
//                    cb_act_my_info_new_password_check_check.isChecked = false
//                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /*    나이    */
        disposeBag.add(RxView.clicks(tv_act_my_info_age)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (isModifyMode) {
                    val ageArray = Utility.instance.getFormer(this)
                    val ageDialog = SelectorMemberDialog(
                        this,
                        AppKeyValue.instance.memberSelectThree,
                        AppKeyValue.instance.memberTypeNotLimit,
                        resources.getString(R.string.information_member_dlg_age),
                        ageArray,
                        object : DialogSelect {
                            override fun onSelect(position: Int, o: Any) {
                                tv_act_my_info_age.text = ageArray[position]
                            }
                        })
                    ageDialog.setCanceledOnTouchOutside(false)
                    ageDialog.show()
                }
            })

        /*    지역01    */
        disposeBag.add(RxView.clicks(rl_act_my_info_area01)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (isModifyMode) {
                    val area01Array = resources.getStringArray(R.array.member_area_array)


                    val area01Dialog = object : CustomArrayDialog(
                        this,
                        resources.getString(R.string.information_member_dlg_area),
                        "",
                        area01Array, "grid"
                    ) {
                        override fun onConfirm() {
                            setTextArea01(area01Array[dialogVM.selectedIndex])
                        }

                    }
                    area01Dialog.setCanceledOnTouchOutside(false)
                    area01Dialog.show()
                }
            })

        /*    지역02    */
        disposeBag.add(RxView.clicks(rl_act_my_info_area02)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (isModifyMode) {
                    val area = tv_act_my_info_area01.text
                    if (!TextUtils.isEmpty(area)) {
                        val area01Array = resources.getStringArray(R.array.member_area_array)
                        var area02Array: Array<String> = emptyArray()
                        when (area) {
                            area01Array[0] -> area02Array =
                                resources.getStringArray(R.array.member_area_seoul_array)
                            area01Array[1] -> area02Array =
                                resources.getStringArray(R.array.member_area_gyeonggi_array)
                            area01Array[2] -> area02Array =
                                resources.getStringArray(R.array.member_area_guangwon_array)
                            area01Array[3] -> area02Array =
                                resources.getStringArray(R.array.member_area_gyeongnam_array)
                            area01Array[4] -> area02Array =
                                resources.getStringArray(R.array.member_area_gyeongbuk_array)
                            area01Array[5] -> area02Array =
                                resources.getStringArray(R.array.member_area_gwangju_array)
                            area01Array[6] -> area02Array =
                                resources.getStringArray(R.array.member_area_daegu_array)
                            area01Array[7] -> area02Array =
                                resources.getStringArray(R.array.member_area_daejeon_array)
                            area01Array[8] -> area02Array =
                                resources.getStringArray(R.array.member_area_busan_array)
                            area01Array[9] -> area02Array =
                                resources.getStringArray(R.array.member_area_sejong_array)
                            area01Array[10] -> area02Array =
                                resources.getStringArray(R.array.member_area_ulsan_array)
                            area01Array[11] -> area02Array =
                                resources.getStringArray(R.array.member_area_incheon_array)
                            area01Array[12] -> area02Array =
                                resources.getStringArray(R.array.member_area_jeonnam_array)
                            area01Array[13] -> area02Array =
                                resources.getStringArray(R.array.member_area_jeonbuk_array)
                            area01Array[14] -> area02Array =
                                resources.getStringArray(R.array.member_area_jeju_array)
                            area01Array[15] -> area02Array =
                                resources.getStringArray(R.array.member_area_chungnam_array)
                            area01Array[16] -> area02Array =
                                resources.getStringArray(R.array.member_area_chungbuk_array)
                            area01Array[17] -> area02Array =
                                resources.getStringArray(R.array.member_area_foreign_array)
                        }

                        val area02Dialog = object : CustomArrayDialog(
                            this,
                            resources.getString(R.string.information_member_dlg_area),
                            "",
                            area02Array,
                            "grid"
                        ) {
                            override fun onConfirm() {

                                setTextArea02(area02Array[dialogVM.selectedIndex])
                            }

                        }
                        area02Dialog.setCanceledOnTouchOutside(false)
                        area02Dialog.show()
                    }
                }
            })

        /*    핸드폰번호 변경    */
        disposeBag.add(RxView.clicks(tv_act_my_info_phone_edit)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, PhoneAuthActivity::class.java)
                startActivityForResult(intent, AppKeyValue.instance.requestPhoneAuth)
            })

        /*    휴면설정    */
        disposeBag.add(RxView.clicks(ll_act_my_info_dormant)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                customPopup = CustomPopup(
                    this,
                    getString(R.string.main_more_menu_dormant),
                    getString(R.string.msg_main_dormant),
                    R.drawable.ic_dormant,
                    object : CustomDialogInterface {
                        override fun onConfirm(v: View) {
                            val subscriber = object : DisposableObserver<ResultItem<BaseItem>>() {
                                override fun onComplete() {

                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }

                                override fun onNext(item: ResultItem<BaseItem>) {
                                    item.let {
                                        if (it.isSuccess) {
                                            this@MyInfoActivity.let { it1 ->
                                                Utility.instance.showAlert(
                                                    it1,
                                                    it1.getString(R.string.app_name),
                                                    it1.getString(R.string.msg_main_dormant_complete),
                                                    DialogInterface.OnClickListener { dialog, which ->
                                                        /*Utility.instance.savePref(it1, AppKeyValue.instance.savePrefDormant, AppKeyValue.instance.keyYes)*/
                                                        Utility.instance.setLogout(it1)
                                                        customPoupHide()
                                                    })
                                            }
                                        }
                                    }
                                }
                            }

                            RetrofitProtocol().retrofit.requestDormant(
                                ServerApi.instance.dormantMethod,
                                id,
                                AppKeyValue.instance.keyYes
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(subscriber)
                        }

                        override fun onCancel(v: View) {
                            customPoupHide()
                        }
                    })
                customPopup?.show()
            })

        /*    회원탈퇴    */
        disposeBag.add(RxView.clicks(ll_act_my_info_withdrawal)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                customPopup = CustomPopup(
                    this,
                    getString(R.string.main_more_menu_withdrawal),
                    getString(R.string.msg_main_withdrawal),
                    R.drawable.ic_withdrawal,
                    object : CustomDialogInterface {
                        override fun onCancel(v: View) {
                            customPoupHide()
                        }

                        override fun onConfirm(v: View) {
                            val withdrawalReDialog = WithdrawalReDialog()
                            withdrawalReDialog.show(
                                supportFragmentManager,
                                AppKeyValue.instance.tagWithdrawalReDlg
                            )
                            customPoupHide()
                        }
                    })
                customPopup?.show()
            })


        /*    수정완료    */
        disposeBag.add(RxView.clicks(btn_act_my_info_complete)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (isModifyMode) {
                    var talkId = et_act_my_info_talk_id.text.toString()
                    var age = tv_act_my_info_age.text.toString()
                        .replace(resources.getString(R.string.utility_date_former), "")
                    var type: String? = if (cb_act_my_info_marry.isChecked) {
                        resources.getString(R.string.information_member_marry)
                    } else {
                        resources.getString(R.string.information_member_remarry)
                    }
                    val area01 = tv_act_my_info_area01.text.toString()
                    val area02 = tv_act_my_info_area02.text.toString()
                    val password = et_act_my_info_password.text.toString()
                    val newPassword = et_act_my_info_new_password.text.toString()
                    val newPasswordCheck = et_act_my_info_new_password_check.text.toString()
                    val phone = tv_act_my_info_phone.text.toString().replace("-", "")

                    if (TextUtils.isEmpty(talkId)) {
                        Utility.instance.showToast(
                            this,
                            resources.getString(R.string.msg_my_info_error_talk_id)
                        )
                    } else if (TextUtils.isEmpty(age)) {
                        Utility.instance.showToast(
                            this,
                            resources.getString(R.string.msg_my_info_error_age)
                        )
                    } else if (!cb_act_my_info_marry.isChecked && !cb_act_my_info_remarry.isChecked) {
                        Utility.instance.showToast(
                            this,
                            resources.getString(R.string.msg_my_info_error_type)
                        )
                    } else if (TextUtils.isEmpty(area01) || TextUtils.isEmpty(area02)) {
                        Utility.instance.showToast(
                            this,
                            resources.getString(R.string.msg_my_info_error_area)
                        )
                    } else {
                        if (oriTalkId == talkId) {
                            talkId = ""
                        }

                        if (oriAge == age) {
                            age = ""
                        }

                        if (oriType == type) {
                            type = ""
                        }

                        if (!TextUtils.isEmpty(password)) {
                            when {
                                TextUtils.isEmpty(newPassword) -> Utility.instance.showToast(
                                    this,
                                    resources.getString(R.string.msg_my_info_error_password_new)
                                )
                                TextUtils.isEmpty(newPasswordCheck) -> Utility.instance.showToast(
                                    this,
                                    resources.getString(R.string.msg_my_info_error_password_new_check)
                                )
                                newPassword != newPasswordCheck -> Utility.instance.showToast(
                                    this,
                                    resources.getString(R.string.msg_my_info_error_password_discord)
                                )
                                else -> {
                                    Utility.instance.hideSoftKeyboard(this)
                                    ll_act_my_info_progress.visibility = View.VISIBLE
                                    mPresenter.setMyInfoEdit(
                                        id,
                                        talkId,
                                        age,
                                        type,
                                        area01,
                                        area02,
                                        password,
                                        newPassword,
                                        newPasswordCheck,
                                        phone
                                    )
                                }
                            }
                        } else if (!TextUtils.isEmpty(newPassword) || !TextUtils.isEmpty(
                                newPasswordCheck
                            )
                        ) {
                            Utility.instance.showToast(
                                this,
                                resources.getString(R.string.msg_my_info_error_password)
                            )
                        } else {
                            Utility.instance.hideSoftKeyboard(this)
                            ll_act_my_info_progress.visibility = View.VISIBLE
                            mPresenter.setMyInfoEdit(
                                id,
                                talkId,
                                age,
                                type,
                                area01,
                                area02,
                                password,
                                newPassword,
                                newPasswordCheck,
                                phone
                            )
                        }
                    }
                } else {
                    btn_act_my_info_complete?.apply {
                        setBackgroundResource(R.drawable.bg_color_white)
                        setTextColor(resources.getColor(R.color.color_main))
                        text = resources.getString(R.string.information_member_modify_complete)
                    }
                    isModifyMode = true
                    setModifyMode()
                }
            })
    }

    private fun setPress(position: Int) {
        marryArray?.let { it ->
            it.map {
                it.isChecked = false
            }
            it[position].isChecked = true
        }
    }

    private fun setModifyMode() {
        if (isModifyMode) {
            et_act_my_info_password.isEnabled = true
            et_act_my_info_new_password.isEnabled = true
            et_act_my_info_new_password_check.isEnabled = true
            et_act_my_info_talk_id.isEnabled = true

            cb_act_my_info_marry.isClickable = true
            cb_act_my_info_remarry.isClickable = true
            tv_act_my_info_phone_edit.isClickable = true
        } else {
            et_act_my_info_password.isEnabled = false
            et_act_my_info_new_password.isEnabled = false
            et_act_my_info_new_password_check.isEnabled = false
            et_act_my_info_talk_id.isEnabled = false

            cb_act_my_info_marry.isClickable = false
            cb_act_my_info_remarry.isClickable = false
            tv_act_my_info_phone_edit.isClickable = false
        }
    }

    /*    기본정보 불러오기    */
    override fun setMyInfoComplete(
        email: String?,
        talkId: String?,
        age: String?,
        type: String?,
        area01: String?,
        area02: String?,
        phone: String?,
        talkIdState: String?,
        ageState: String?,
        typeState: String?
    ) {
        oriTalkId = talkId
        oriAge = age
        oriType = type

        email?.let {
            if(it.contains("kakao.com")){
                tv_act_my_info_email.text = "카카오로 가입한 회원입니다."
            }else if(it.contains("naver.com")){
                tv_act_my_info_email.text = "네이버로 가입한 회원입니다."
            }else if(it.contains("facebook.com")){
                tv_act_my_info_email.text = "페이스북으로 가입한 회원입니다."
            }else{
                tv_act_my_info_email.text = email
            }
        }
        et_act_my_info_talk_id.setText(talkId)
        tv_act_my_info_age.text = String.format(resources.getString(R.string.birth), age)

        if (type == resources.getString(R.string.information_member_marry)) {
            setPress(0)
        } else {
            setPress(1)
        }

        tv_act_my_info_area01.text = area01
        tv_act_my_info_area02.text = area02
        tv_act_my_info_phone.text = phone

        talkIdState?.toInt()?.let {
            if (it > 0) {
                et_act_my_info_talk_id.isEnabled = false
            }
        }

        ageState?.toInt()?.let {
            if (it > 0) {
                tv_act_my_info_age.isClickable = false
            }
        }

        typeState?.toInt()?.let {
            if (it > 0) {
                cb_act_my_info_marry.isClickable = false
                cb_act_my_info_remarry.isClickable = false
            }
        }

        ll_act_my_info_progress.visibility = View.GONE
    }

    /*    기본정보 호출실패    */
    override fun setMyInfoFailed(error: String?) {
        ll_act_my_info_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
        finish()
    }

    /*    기본정보 수정    */
    override fun setMyInfoEditComplete(
        talkId: String?,
        age: String?,
        birth: String?,
        type: String?,
        area01: String?,
        area02: String?,
        phone: String?
    ) {
        ll_act_my_info_progress.visibility = View.GONE
        btn_act_my_info_complete?.apply {
            setBackgroundResource(R.drawable.bg_color_main)
            setTextColor(resources.getColor(R.color.color_white))
            text = resources.getString(R.string.information_member_modify)
        }
        isModifyMode = false
        setModifyMode()

        Utility.instance.showAlert(
            this,
            resources.getString(R.string.app_name),
            resources.getString(R.string.msg_my_info_edit_complete),
            DialogInterface.OnClickListener { dialog, which ->
                val newPassword = et_act_my_info_new_password.text.toString()
                if (!TextUtils.isEmpty(newPassword)) {
                    Utility.instance.savePref(
                        this,
                        AppKeyValue.instance.savePrefPassword,
                        newPassword
                    )
                }

                val myInfoData = MyInfoData()
                myInfoData.mbNick = talkId
                myInfoData.mbAge = age
                myInfoData.mbBirth = birth
                myInfoData.mbChar = type
                myInfoData.mbAddr1 = area01
                myInfoData.mbAddr2 = area02
                myInfoData.mbHp = phone
                EventBus.sendEventMyInfo(myInfoData)
                finish()
            })
    }

    /*    기본정보 수정 호출실패    */
    override fun setMyInfoEditFailed(error: String?) {
        ll_act_my_info_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == AppKeyValue.instance.requestPhoneAuth) {
                val phone = data?.getStringExtra(AppKeyValue.instance.phoneAuthPhone)
                val phone01 = phone?.substring(0, 3)
                val phone02 = phone?.substring(3, 7)
                val phone03 = phone?.substring(7)
                tv_act_my_info_phone.text =
                    String.format(resources.getString(R.string.line_str), phone01, phone02, phone03)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
        Utility.instance.hideSoftKeyboard(this)
    }

    private fun setTextArea01(str: String) {
        tv_act_my_info_area01.text = str
        tv_act_my_info_area02.text = null
    }

    private fun setTextArea02(str: String) {
        tv_act_my_info_area02.text = str
    }

    private fun customPoupHide() {
        customPopup?.hide()
    }


}
