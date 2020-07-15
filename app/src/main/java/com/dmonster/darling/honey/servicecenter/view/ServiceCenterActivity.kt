package com.dmonster.darling.honey.servicecenter.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.agreement.view.AgreementActivity
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.databinding.ActivityMyInfoBinding
import com.dmonster.darling.honey.databinding.ActivityServiceCenterBinding
import com.dmonster.darling.honey.inquiry.view.InquiryMainActivity
import com.dmonster.darling.honey.notice.view.NoticeActivity
import com.dmonster.darling.honey.servicecenter.presenter.ServiceCenterContract
import com.dmonster.darling.honey.servicecenter.presenter.ServiceCenterPresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_service_center.*
import java.util.concurrent.TimeUnit

class ServiceCenterActivity : BaseActivity(), ServiceCenterContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: ServiceCenterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_service_center)
        val binding: ActivityServiceCenterBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_service_center)
        binding.bannerVM =
            BannerVM(
                Utility.instance.getPref(this, AppKeyValue.instance.savePrefID),
                lifecycle,
                this
            )
        binding.lifecycleOwner = this
        init()
        setListener()
    }

    private fun init() {
        ctb_act_service_center_toolbar.setTitle(resources.getString(R.string.main_more_menu_service_center))
        disposeBag = CompositeDisposable()
        mPresenter = ServiceCenterPresenter()
        mPresenter.attachView(this)
        mPresenter.getAppInfo()

        ll_act_service_center_progress.visibility = View.VISIBLE
    }

    private fun setListener() {
        /*    이용약관    */
        disposeBag.add(RxView.clicks(tv_act_service_center_agreement)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, AgreementActivity::class.java)
                intent.putExtra(
                    AppKeyValue.instance.intentAgreement,
                    AppKeyValue.instance.typesAgreement
                )
                startActivity(intent)
            })

        /*    개인정보처리방침    */
        disposeBag.add(RxView.clicks(tv_act_service_center_personal)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, AgreementActivity::class.java)
                intent.putExtra(
                    AppKeyValue.instance.intentAgreement,
                    AppKeyValue.instance.typesPersonal
                )
                startActivity(intent)
            })

        /*    공지사항    */
        disposeBag.add(RxView.clicks(tv_act_service_center_inquiry)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, InquiryMainActivity::class.java)
                startActivity(intent)
            })

        /*    고객센터 전화걸기    */
        disposeBag.add(RxView.clicks(btn_act_service_center_call)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                Utility.instance.showTwoButtonAlert(
                    this,
                    resources.getString(R.string.app_name),
                    resources.getString(R.string.msg_app_call),
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.CALL_PHONE
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                return@OnClickListener
                            }
                            val callNumber = String.format(
                                resources.getString(R.string.service_center_call_tel),
                                tv_act_service_center_phone.text
                            )
                            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(callNumber)))
                        }
                    })
            })
    }

    /*    앱정보    */
    override fun setAppInfo(
        email: String?,
        phone: String?,
        communication: String?,
        business: String?
    ) {
        tv_act_service_center_email.text = email
        tv_act_service_center_phone.text = phone
        tv_act_service_center_communication.text = communication
        tv_act_service_center_business.text = business
        tv_act_service_center_version.text =
            packageManager.getPackageInfo(packageName, 0).versionName
        ll_act_service_center_progress.visibility = View.GONE
    }

    /*    앱정보 호출실패    */
    override fun setAppInfoFailed(error: String?) {
        Utility.instance.showToast(this, error)
        ll_act_service_center_progress.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
