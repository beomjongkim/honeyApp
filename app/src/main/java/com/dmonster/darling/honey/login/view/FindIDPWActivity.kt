package com.dmonster.darling.honey.login.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.login.presenter.FindIDPWContract
import com.dmonster.darling.honey.login.presenter.FindIDPWPresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_find_id_pw.*
import java.util.concurrent.TimeUnit

class FindIDPWActivity : BaseActivity(), FindIDPWContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: FindIDPWContract.Presenter

    private var findIDSuccess: Boolean = false
    private var findPWSuccess: Boolean = false
    private var passwordFind: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_id_pw)

        init()
        setListener()
    }

    private fun init() {
        ctb_act_find_toolbar.setTitle(resources.getString(R.string.find_id_pw_title))
        disposeBag = CompositeDisposable()
        mPresenter = FindIDPWPresenter()
        mPresenter.attachView(this)
    }

    private fun setListener() {
        /*    뒤로가기    */
        disposeBag.add(RxView.clicks(ctb_act_find_toolbar.getLeftBtn())
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent()
                intent.putExtra(AppKeyValue.instance.findPW, passwordFind)
                setResult(Activity.RESULT_OK, intent)
                finish()
            })

        /*    아이디 찾기    */
        disposeBag.add(RxView.clicks(tv_act_find_id_enter)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (findIDSuccess) {
                    Utility.instance.hideSoftKeyboard(this)

                    val id = tv_act_find_id.text.toString()
                    val intent = Intent()
                    intent.putExtra(AppKeyValue.instance.findID, id)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val phone = et_act_find_id_phone.text.toString()
                    if (TextUtils.isEmpty(phone)) {
                        Utility.instance.showToast(
                            this,
                            resources.getString(R.string.msg_find_phone)
                        )
                    } else {
                        ll_act_find_progress.visibility = View.VISIBLE
                        mPresenter.setFindID("id", phone, "")
                    }
                }
            })

        /*    비밀번호 찾기    */
        disposeBag.add(RxView.clicks(tv_act_find_pw_enter)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (findPWSuccess) {
                    Utility.instance.hideSoftKeyboard(this)

                    val id = et_act_find_pw_email.text.toString()
                    val intent = Intent()
                    intent.putExtra(AppKeyValue.instance.findID, id)
                    intent.putExtra(AppKeyValue.instance.findPW, passwordFind)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val phone = et_act_find_pw_phone.text.toString()
                    val id = et_act_find_pw_email.text.toString()

                    when {
                        TextUtils.isEmpty(phone) -> Utility.instance.showToast(
                            this,
                            resources.getString(R.string.msg_find_phone)
                        )
                        TextUtils.isEmpty(id) -> Utility.instance.showToast(
                            this,
                            resources.getString(R.string.msg_find_email)
                        )
                        else -> {
                            ll_act_find_progress.visibility = View.VISIBLE
                            mPresenter.setFindPW("pw", phone, id)
                        }
                    }
                }
            })
    }

    /*    아이디 찾기    */
    override fun setFindIDComplete(id: String?) {
        ll_act_find_progress.visibility = View.GONE
        tv_act_find_id.visibility = View.VISIBLE
        var id_text = ""
        var id_go = ""
        //아이디에 naver혹은 kakao가 포함되어 있다면?
        id?.let {
            if (it.contains("kakao")) {
                id_text = "카카오로 간편 로그인하셨습니다.\n해당 계정으로 진행해주세요."
                id_go = "로그인화면으로"
            } else if (it.contains("naver")) {
                id_text = "네이버로 간편 로그인하셨습니다.\n해당 계정으로 진행해주세요."

                id_go = "로그인화면으로"
            } else {
                id_text = id
                id_go = resources.getString(R.string.find_id_enter_use)
            }
        }
        tv_act_find_id.text = id_text

        findIDSuccess = true
        tv_act_find_id_enter.text = id_go
        Utility.instance.hideSoftKeyboard(this)
    }

    /*    아이디 찾기 호출실패    */
    override fun setFindIDFailed(error: String?) {
        ll_act_find_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    비밀번호 찾기    */
    override fun setFindPWComplete(message: String?) {
        ll_act_find_progress.visibility = View.GONE
        Utility.instance.showToast(this, message)

        passwordFind = true
        findPWSuccess = true
        tv_act_find_pw_enter.text = resources.getString(R.string.find_id_enter_login)
        Utility.instance.hideSoftKeyboard(this)
    }

    /*    비밀번호 찾기 호출실패    */
    override fun setFindPWFailed(error: String?) {
        ll_act_find_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(AppKeyValue.instance.findPW, passwordFind)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
        Utility.instance.hideSoftKeyboard(this)
    }

}
