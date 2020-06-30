package com.dmonster.darling.honey.inquiry.view

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.databinding.ActivityInquiryMainBinding
import com.dmonster.darling.honey.databinding.FragmentMyInfoMainBinding
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_inquiry_main.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class InquiryMainActivity: BaseActivity() {

    private lateinit var disposeBag: CompositeDisposable

    private val fragInquire = 0
    private val fragBreakdown = 1
    private var arrMenus: ArrayList<Button>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : ActivityInquiryMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_inquiry_main)
        binding.bannerVM =
            BannerVM(Utility.instance.getPref(this, AppKeyValue.instance.savePrefID), lifecycle, this)
        binding.lifecycleOwner = this
        init()
        setListener()
        setEventBusListener()
    }

    private fun init() {
        ctb_act_inquiry_main_toolbar.setTitle(resources.getString(R.string.main_more_menu_inquiry))
        disposeBag = CompositeDisposable()

        arrMenus = ArrayList()
        arrMenus?.add(btn_act_inquiry_main_inquire)
        arrMenus?.add(btn_act_inquiry_main_breakdown)

        btn_act_inquiry_main_inquire.isSelected = true
        fragmentReplace(fragInquire)
    }

    private fun setListener() {
        /*    문의하기    */
        disposeBag.add(RxView.clicks(btn_act_inquiry_main_inquire)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    setPress(fragInquire)
                    fragmentReplace(fragInquire)
                })

        /*    문의내역    */
        disposeBag.add(RxView.clicks(btn_act_inquiry_main_breakdown)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    setPress(fragBreakdown)
                    fragmentReplace(fragBreakdown)
                })
    }

    private fun setEventBusListener() {
        EventBus.subjectInquiry.subscribe {
            if(it == AppKeyValue.instance.eventBusInquiry) {
                setPress(fragBreakdown)
                fragmentReplace(fragBreakdown)
            }
        }.addTo(disposeBag)
    }

    private fun setPress(position: Int) {
        arrMenus?.let { it ->
            it.map {
                it.isSelected = false
            }
            it[position].isSelected = true
        }
    }

    private fun fragmentReplace(reqNewFragmentIndex: Int) {
        val newFragment: androidx.fragment.app.Fragment? = getFragment(reqNewFragmentIndex)

        val transaction = supportFragmentManager.beginTransaction()
        newFragment?.let { transaction.replace(R.id.fl_act_inquiry_main_content_layout, it) }
        transaction.commit()

        Utility.instance.hideSoftKeyboard(this)
    }

    private fun getFragment(idx: Int): androidx.fragment.app.Fragment? {
        var newFragment: androidx.fragment.app.Fragment? = null

        when(idx) {
            fragInquire -> newFragment = InquiryMenu01Fragment()

            fragBreakdown -> newFragment = InquiryMenu02Fragment()
        }

        return newFragment
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }

}
