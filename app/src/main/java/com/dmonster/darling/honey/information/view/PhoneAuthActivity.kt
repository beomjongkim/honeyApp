package com.dmonster.darling.honey.information.view

import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.information.presenter.PhoneAuthContract
import com.dmonster.darling.honey.information.presenter.PhoneAuthPresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_phone_auth.*
import java.util.concurrent.TimeUnit
import android.content.Intent
import com.dmonster.darling.honey.join.view.JoinActivity
import com.dmonster.darling.honey.util.common.EventBus

class PhoneAuthActivity : BaseActivity(), PhoneAuthContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: PhoneAuthContract.Presenter

    private var isPhoneAuth = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        init()
        setListener()
    }

    private fun init() {
        ctb_act_phone_auth_toolbar.setTitle(resources.getString(R.string.phone_auth_title))
        disposeBag = CompositeDisposable()

        val phone = intent.getStringExtra(AppKeyValue.instance.intentPhone)
        if (!TextUtils.isEmpty(phone)) {
            et_act_phone_auth_phone.setText(phone)
            isPhoneAuth = true
        }

        mPresenter = PhoneAuthPresenter()
        mPresenter.attachView(this)
    }

    private fun setListener() {
        /*    인증번호 확인    */
        disposeBag.add(RxView.clicks(tv_act_phone_auth_number_check)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val phone = et_act_phone_auth_phone.text.toString()
                val authNo = et_act_phone_auth_number.text.toString()

                if (TextUtils.isEmpty(phone)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_phone_auth_error_phone)
                    )
                } else if (!Utility.instance.isValidCellPhoneNumber(phone)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_phone_auth_error_phone_valid)
                    )
                } else if (TextUtils.isEmpty(authNo)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_phone_auth_error_number)
                    )
                } else {
                    Utility.instance.hideSoftKeyboard(this)
                    ll_act_phone_auth_progress.visibility = View.VISIBLE
                    mPresenter.getPhoneAuthCheck(phone, authNo)
                }
            })

        /*    인증번호 받기    */
        disposeBag.add(RxView.clicks(tv_act_phone_auth_receive)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
                val phone = et_act_phone_auth_phone.text.toString()

                if (TextUtils.isEmpty(phone)) {
                    Utility.instance.showToast(
                        this,
                        resources.getString(R.string.msg_phone_auth_error_phone)
                    )
                } else {
                    Utility.instance.hideSoftKeyboard(this)
                    ll_act_phone_auth_progress.visibility = View.VISIBLE
                    mPresenter.getPhoneAuth(id, phone)
                }
            })
    }

    /*    인증번호 받기    */
    override fun setPhoneAuthComplete(message: String?) {
        ll_act_phone_auth_progress.visibility = View.GONE
        Utility.instance.showToast(this, message)
    }

    /*    인증번호 호출실패    */
    override fun setPhoneAuthFailed(error: String?) {
        ll_act_phone_auth_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    휴대폰 인증    */
    override fun setPhoneAuthCheckComplete() {
        val phone = et_act_phone_auth_phone.text.toString()

        ll_act_phone_auth_progress.visibility = View.GONE
        if (isPhoneAuth) {
            Utility.instance.showAlert(
                this,
                resources.getString(R.string.app_name),
                resources.getString(R.string.msg_phone_auth_complete),
                DialogInterface.OnClickListener { dialog, which ->
                    Utility.instance.savePref(this,AppKeyValue.instance.phoneCert,phone)
                    finish()
                })
        } else {
            Utility.instance.showAlert(
                this,
                resources.getString(R.string.app_name),
                resources.getString(R.string.msg_phone_auth_change_complete),
                DialogInterface.OnClickListener { dialog, which ->

                    intent.putExtra(AppKeyValue.instance.phoneAuthPhone, phone)
                    setResult(RESULT_OK, intent)
                    finish()
                })
        }
    }

    /*    휴대폰 인증 호출실패    */
    override fun setPhoneAuthCheckFailed(error: String?) {
        ll_act_phone_auth_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
        Utility.instance.hideSoftKeyboard(this)
    }

}
