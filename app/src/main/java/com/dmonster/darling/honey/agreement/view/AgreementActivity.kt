package com.dmonster.darling.honey.agreement.view

import android.os.Bundle
import android.view.View
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.agreement.presenter.AgreementContract
import com.dmonster.darling.honey.agreement.presenter.AgreementPresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import kotlinx.android.synthetic.main.activity_agreement.*

class AgreementActivity: BaseActivity(), AgreementContract.View {

    private lateinit var mPresenter: AgreementContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        init()
    }

    private fun init() {
        mPresenter = AgreementPresenter()
        mPresenter.attachView(this)

        ll_act_agreement_progress.visibility = View.VISIBLE

        val getTypes = intent.getStringExtra(AppKeyValue.instance.intentAgreement)
        when(getTypes) {
            AppKeyValue.instance.typesAgreement -> {
                ctb_act_agreement_toolbar.setTitle(resources.getString(R.string.agreement_title))
            }

            AppKeyValue.instance.typesPersonal -> {
                ctb_act_agreement_toolbar.setTitle(resources.getString(R.string.personal_title))
            }

            AppKeyValue.instance.typesUse -> {
                ctb_act_agreement_toolbar.setTitle(resources.getString(R.string.main_more_menu_use))
            }
        }
        mPresenter.getAgreement(getTypes)
    }

    /*    이용약관, 개인정보처리방침    */
    override fun agreementComplete(content: String?) {
        tv_act_agreement_content.text = content
        ll_act_agreement_progress.visibility = View.GONE
    }

    /*    이용약관, 개인정보처리방침 호출실패    */
    override fun agreementFailed(error: String?) {
        Utility.instance.showToast(this, error)
        ll_act_agreement_progress.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}
