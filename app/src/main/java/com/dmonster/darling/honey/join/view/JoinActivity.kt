package com.dmonster.darling.honey.join.view

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.text.*
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.agreement.view.AgreementActivity
import com.dmonster.darling.honey.block_friends.view.BlockFriendsActivity
import com.dmonster.darling.honey.common.viewmodel.DatePickerVM
import com.dmonster.darling.honey.common.viewmodel.SpinnerVM
import com.dmonster.darling.honey.customview.CustomDialog
import com.dmonster.darling.honey.customview.DialogSelect
import com.dmonster.darling.honey.databinding.ActivityJoinBinding
import com.dmonster.darling.honey.dialog.EmailCheckDialog
import com.dmonster.darling.honey.information.view.PhoneAuthActivity
import com.dmonster.darling.honey.join.presenter.JoinContract
import com.dmonster.darling.honey.join.presenter.JoinPresenter
import com.dmonster.darling.honey.join.viewmodel.BirthDayVM
import com.dmonster.darling.honey.join.viewmodel.BirthMonthVM
import com.dmonster.darling.honey.join.viewmodel.BirthYearVM
import com.dmonster.darling.honey.join.viewmodel.PhoneCertVM
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.webview.view.WebViewActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_join.*
import java.util.*
import java.util.concurrent.TimeUnit


class JoinActivity : BaseActivity(), JoinContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: JoinContract.Presenter
    private lateinit var emailCheckDialog: EmailCheckDialog

    private lateinit var checkArrayId: Array<Int>
    private lateinit var checkArrayCheck: Array<CheckBox?>

    private var checkArray: ArrayList<CheckBox>? = null
    private var id: String? = null
    private var email: String? = null
    private var emailEdit: String? = null
    private var password: String? = null
    private var passwordCheck: String? = null
    private var talkId: String? = null
    private var age: String? = null
    private var area01: String? = null
    private var area02: String? = null
    private var gender: String? = null
    private var recommendation: String? = null
    private var phone: String? = null
    private var binding: ActivityJoinBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)
//        setContentView(R.layout.activity_join)

        init()
        setListener()
    }

    private fun init() {
        bindViewModel()
        ctb_act_join_toolbar.setTitle(resources.getString(R.string.join_title))
        disposeBag = CompositeDisposable()
        mPresenter = JoinPresenter()
        mPresenter.attachView(this)

        checkArrayId = arrayOf(R.id.cb_act_join_male, R.id.cb_act_join_female)

        checkArray = ArrayList()
        checkArray?.add(cb_act_join_male)
        checkArray?.add(cb_act_join_female)

        emailCheckDialog = EmailCheckDialog()
    }

    private fun bindViewModel() {
        binding?.let {
            it.lifecycleOwner = this
            it.birthDayVM = BirthDayVM()
            it.birthMonthVM = BirthMonthVM()
            it.birthYearVM = BirthYearVM()
            it.calendarVM = DatePickerVM()

            val arrayAdapterUpper = ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                resources.getStringArray(R.array.main_area_array)
            )
            val arrayAdapterLower = ArrayAdapter<String>(
                this,
                R.layout.support_simple_spinner_dropdown_item
            )
            it.upperSpinnerVM =
                SpinnerVM(arrayAdapterUpper)
            it.lowerSpinnerVM =
                SpinnerVM(arrayAdapterLower)
            it.phoneCertVM = object : PhoneCertVM() {
                override fun startActivityForResult() {
                    val intent = Intent(this@JoinActivity, PhoneAuthActivity::class.java)
                    intent.putExtra(AppKeyValue.instance.intentPhone, phoneNumber.value)
                    startActivity(intent)
                }

                override fun getActivity(): Activity {
                    return this@JoinActivity
                }
            }
        }
    }

    private fun setListener() {

        /*    성별 선택    */
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

        /*    아이디 입력    */
        et_act_join_id.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때

            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
                if (!TextUtils.isEmpty(et_act_join_id.text.toString())) {
                    val joinId: String = if (et_act_join_email.visibility == View.VISIBLE) {
                        et_act_join_id.text.toString() + "@" + et_act_join_email.text.toString()
                    } else {
                        et_act_join_id.text.toString() + "@" + tv_act_join_email.text.toString()
                    }
                    if (Utility.instance.isValidEmail(joinId)) {
                        mPresenter.getJoinId(joinId)
                    } else {
                        cb_act_join_id_check.isChecked = false
                        ll_act_join_progress.visibility = View.GONE
                    }
                } else {
                    ll_act_join_progress.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
                ll_act_join_progress.visibility = View.VISIBLE
            }
        })

        /*    이메일 입력    */
        et_act_join_email.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
                s?.let {
                    val joinId =
                        et_act_join_id.text.toString() + "@" + et_act_join_email.text.toString()
                    if ((s.toString() != "") && (Utility.instance.isValidEmail(joinId))) {
                        mPresenter.getJoinId(joinId)
                    } else {
                        ll_act_join_progress.visibility = View.GONE
                        tv_act_join_use_id.text = getString(R.string.check_email_fomat)
                        cb_act_join_id_check.isChecked = false
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
                ll_act_join_progress.visibility = View.VISIBLE
            }
        })

        /*    이메일 선택    */
        disposeBag.add(RxView.clicks(rl_act_join_email)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val emailArray = resources.getStringArray(R.array.login_email_array)
                val emailDialog = CustomDialog(
                    this,
                    resources.getString(R.string.dialog_title),
                    emailArray,
                    object : DialogSelect {
                        override fun onSelect(position: Int, o: Any) {
                            if (position == emailArray.size.minus(1)) {
                                tv_act_join_email.visibility = View.GONE
                                tv_act_join_email.text = null
                                et_act_join_email.visibility = View.VISIBLE
                                et_act_join_email.requestFocus()
                                cb_act_join_id_check.isChecked = false
                            } else {
                                tv_act_join_email.visibility = View.VISIBLE
                                et_act_join_email.visibility = View.GONE
                                et_act_join_email.text = null

                                tv_act_join_email.text = emailArray[position]
                                val joinId =
                                    et_act_join_id.text.toString() + "@" + tv_act_join_email.text.toString()
                                mPresenter.getJoinId(joinId)
                            }
                        }
                    })
                emailDialog.setCanceledOnTouchOutside(false)
                emailDialog.show()
            })

        /*    비밀번호 입력    */
        et_act_join_password.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
                val password = et_act_join_password.text.toString()
                if (!TextUtils.isEmpty(password)) {
                    cb_act_join_password_check.isChecked = password.length > 3
                } else {
                    cb_act_join_password_check.isChecked = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /*    비밀번호 확인 입력    */
        et_act_join_password_check.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
                val passwordCheck = et_act_join_password_check.text.toString()
                if (!TextUtils.isEmpty(passwordCheck) && TextUtils.equals(
                        et_act_join_password.text,
                        et_act_join_password_check.text
                    )
                ) {
                    cb_act_join_password_check_check.isChecked = passwordCheck.length > 3
                } else {
                    cb_act_join_password_check_check.isChecked = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /*    대화명 입력    */

        et_act_join_talk.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때

            }

            override fun afterTextChanged(s: Editable?) {
                // 입력이 끝났을 때
                val pattern =
                    resources.getString(R.string.utility_korean_number_only_regex).toRegex()
                if (!s.isNullOrBlank()) {
                    if (!pattern.matches(s)) {
                        cb_act_join_talk_check.isChecked = false
                        Utility.instance.showToast(
                            this@JoinActivity,
                            resources.getString(R.string.msg_error_join_talk)
                        )
                        et_act_join_talk.setText("")
                    } else {
                        if (!TextUtils.isEmpty(s)) {
                            cb_act_join_talk_check.isChecked = s.length > 1
                        } else {
                            cb_act_join_talk_check.isChecked = false
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })
//        /*   핸드폰    */
        et_phone_cert.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 입력되는 텍스트에 변화가 있을 때
                cb_act_join_phone_check.isChecked = false
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 입력하기 전에
            }
        })

        /*    이용약관 보기    */
        disposeBag.add(RxView.clicks(tv_act_join_agreement_detail)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, AgreementActivity::class.java)
                intent.putExtra(
                    AppKeyValue.instance.intentAgreement,
                    AppKeyValue.instance.typesAgreement
                )
                startActivity(intent)
            })

        /*    개인정보처리방침 보기    */
        disposeBag.add(RxView.clicks(tv_act_join_personal_detail)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, AgreementActivity::class.java)
                intent.putExtra(
                    AppKeyValue.instance.intentAgreement,
                    AppKeyValue.instance.typesPersonal
                )
                startActivity(intent)
            })

        /*    취소    */
        disposeBag.add(RxView.clicks(btn_act_join_cancel)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                finish()
            })

        /*    가입하기    */
        disposeBag.add(RxView.clicks(btn_act_join_enter)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                id = et_act_join_id.text.toString()
                email = tv_act_join_email.text.toString()
                emailEdit = et_act_join_email.text.toString()

                val joinId: String = if (et_act_join_email.visibility == View.VISIBLE) {
                    "$id@$emailEdit"
                } else {
                    "$id@$email"
                }
                val isValidEmail = joinId.let { it1 -> Utility.instance.isValidEmail(it1) }

                password = et_act_join_password.text.toString()
                passwordCheck = et_act_join_password_check.text.toString()
                talkId = et_act_join_talk.text.toString()
                age = et_act_join_age_year.text.toString()
                    .replace(resources.getString(R.string.utility_date_former), "")

                area01 = binding?.upperSpinnerVM?.text?.value
                area02 = binding?.lowerSpinnerVM?.text?.value

                val area = tv_act_join_area.text.toString()
//                if (!TextUtils.isEmpty(area)) {
//                    val areaArray = area.split(" > ".toRegex()).dropLastWhile { it2 -> it2.isEmpty() }.toTypedArray()
//                    area01 = areaArray[0]
//                    area02 = areaArray[1]
//                }
                gender = "M"
                when {
                    cb_act_join_male.isChecked -> gender = "M"
                    cb_act_join_female.isChecked -> gender = "F"
                }
                recommendation = et_act_join_recommendation.text.toString()
                phone = et_phone_cert.text.toString()
                val useId = tv_act_join_use_id.text.toString()

                if (TextUtils.isEmpty(id)) {
                    Utility.instance.showToast(this, resources.getString(R.string.msg_error_id))
                } else if ((tv_act_join_email.visibility == View.VISIBLE && TextUtils.isEmpty(
                        email
                    )) || (et_act_join_email.visibility == View.VISIBLE && TextUtils.isEmpty(
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
                } else if (TextUtils.isEmpty(passwordCheck)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_password_check)
                    )
                } else if (TextUtils.isEmpty(talkId) || !cb_act_join_talk_check.isChecked) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_talk)
                    )
                } else if (TextUtils.isEmpty(age)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_age)
                    )
                } else if (TextUtils.isEmpty(area)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_area_join)
                    )
                } else if (!(cb_act_join_male.isChecked) && !(cb_act_join_female.isChecked)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_gender)
                    )
                } else if (!cb_act_join_phone_check.isChecked) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_phone_check)
                    )
                } else if (!cb_act_join_agreement.isChecked) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_agreement)
                    )
                } else if (!cb_act_join_personal.isChecked) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_personal)
                    )
                } else if (password != passwordCheck) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_error_password_discord)
                    )
                } else if (useId == resources.getString(R.string.join_use_id)) {
                    Utility.instance.showToast(this, resources.getString(R.string.join_use_id))
                } else {
                    Utility.instance.hideSoftKeyboard(this)
                    emailCheckDialog.setEmail(joinId)
                    emailCheckDialog.show(
                        supportFragmentManager,
                        AppKeyValue.instance.tagEmailCheckDlg
                    )
                }
            })

        emailCheckDialog.enterClick = emailCheckEnterClick()
    }

    private fun setPress(position: Int) {
        checkArray?.let { it ->
            it.map {
                it.isChecked = false
            }
            it[position].isChecked = true

            if (position == 1) {
                ll_act_join_recommendation.visibility = View.GONE
            } else {
                ll_act_join_recommendation.visibility = View.GONE
            }
        }
    }

    private fun emailCheckEnterClick() = View.OnClickListener {
        ll_act_join_progress.visibility = View.VISIBLE

        if (et_act_join_email.visibility == View.VISIBLE) {
            email = emailEdit
        }
        mPresenter.getJoin(
            id,
            email,
            password,
            passwordCheck,
            talkId,
            age,
            area01,
            area02,
            gender,
            recommendation,
            phone
        )
        emailCheckDialog.dismiss()
    }

    /*    회원가입 아이디 사용여부    */
    override fun setJoinIdIsValid(result: String?) {
        ll_act_join_progress.visibility = View.GONE
        if ((!TextUtils.isEmpty(tv_act_join_email.text) || (!TextUtils.isEmpty(et_act_join_email.text))) && !TextUtils.isEmpty(
                et_act_join_id.text
            )
        ) {
            when (result) {
                "N" -> {
                    tv_act_join_use_id.text = resources.getString(R.string.join_use_not_id)
                    cb_act_join_id_check.isChecked = true
                }

                "Y" -> {
                    tv_act_join_use_id.text = resources.getString(R.string.join_use_id)
                    cb_act_join_id_check.isChecked = false
                }
            }
        } else if (TextUtils.isEmpty(tv_act_join_email.text) && TextUtils.isEmpty(et_act_join_email.text) && TextUtils.isEmpty(
                et_act_join_id.text
            )
        ) {
            tv_act_join_use_id.text = resources.getString(R.string.join_id_guide)
            cb_act_join_id_check.isChecked = false
        } else {
            tv_act_join_use_id.text = resources.getString(R.string.join_use_id_initial)
            cb_act_join_id_check.isChecked = false
        }
    }

    /*    회원가입    */
    override fun setJoinComplete(
        joinId: String?,
        mbNo: String?,
        recommend: String?,
        gender: String?
    ) {
        joinId?.let { Utility.instance.savePref(this, AppKeyValue.instance.savePrefID, it) }
        Utility.instance.UserData().apply {
            setUserMb(mbNo)
            setUserRecommend(recommend)
            setUserGender(gender)
        }

        val joinPassword = et_act_join_password.text.toString()
        Utility.instance.savePref(this, AppKeyValue.instance.savePrefPassword, joinPassword)
        Utility.instance.savePref(
            this,
            AppKeyValue.instance.savePrefType,
            AppKeyValue.instance.keyTypeBasic
        )

        val instanceId = FirebaseInstanceId.getInstance().token
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "newJoin")
        FirebaseAnalytics.getInstance(applicationContext)
            .logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle)
        mPresenter.setLogin(joinId, joinPassword, instanceId, AppKeyValue.instance.keyTypeBasic)

    }

    /*    회원가입 호출실패    */
    override fun setJoinFailed(error: String?) {
        ll_act_join_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    로그인    */
    override fun setLoginComplete() {
        ll_act_join_progress.visibility = View.GONE
        Utility.instance.showTwoButtonAlert(
            this,
            "지인차단",
            "나를 알수도 있는 회원을 차단할 수 있습니다. 차단하시겠습니까?",
            DialogInterface.OnClickListener { dialog, which ->
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    startActivity(
                        Intent(this@JoinActivity, BlockFriendsActivity::class.java).also {
                            it.putExtra("fromJoinActivity",true)
                        }
                    )
                } else {
                    val intent = Intent(this, WebViewActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                dialog.dismiss()
            })

    }

    /*    로그인 호출실패    */
    override fun setLoginFailed() {
        ll_act_join_progress.visibility = View.GONE
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
        Utility.instance.hideSoftKeyboard(this)
    }

    override fun onStop() {
        super.onStop()
        Utility.instance.savePref(this, AppKeyValue.instance.phoneCert, "")
    }

    override fun onResume() {
        super.onResume()
        if (!Utility.instance.getPref(this, AppKeyValue.instance.phoneCert).isNullOrBlank()) {
            et_phone_cert.setText(Utility.instance.getPref(this, AppKeyValue.instance.phoneCert))
            cb_act_join_phone_check.isChecked = true
            et_phone_cert.isEnabled = false
        } else {
            cb_act_join_phone_check.isChecked = false
        }

    }
}
