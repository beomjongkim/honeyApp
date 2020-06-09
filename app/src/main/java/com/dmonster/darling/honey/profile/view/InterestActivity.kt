package com.dmonster.darling.honey.profile.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.profile.presenter.GoodContract
import com.dmonster.darling.honey.profile.presenter.GoodPresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_interest.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class InterestActivity: BaseActivity(), GoodContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: GoodContract.Presenter

    private lateinit var checkArrayId: Array<Int>
    private lateinit var checkArrayCheck: Array<CheckBox?>

    private var checkArray: ArrayList<CheckBox>? = null
    private var imageArray: ArrayList<Int>? = null
    private var cardIndex: String? = null

    private var id: String? = null
    private var otherId: String? = null
    private var otherProfileImage: String? = null
    private var otherTalkId: String? = null
    private var otherType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interest)

        init()
        setListener()
    }

    private fun init() {
        ctb_act_interest_toolbar.setTitle(resources.getString(R.string.interest_title))
        disposeBag = CompositeDisposable()
        mPresenter = GoodPresenter()
        mPresenter.attachView(this)

        checkArrayId = arrayOf(R.id.cb_act_interest_image01, R.id.cb_act_interest_image02, R.id.cb_act_interest_image03, R.id.cb_act_interest_image04)

        checkArray = ArrayList()
        checkArray?.add(cb_act_interest_image01)
        checkArray?.add(cb_act_interest_image02)
        checkArray?.add(cb_act_interest_image03)
        checkArray?.add(cb_act_interest_image04)

        imageArray = ArrayList()
        imageArray?.add(R.drawable.interest_image01_btn)
        imageArray?.add(R.drawable.interest_image02_btn)
        imageArray?.add(R.drawable.interest_image03_btn)
        imageArray?.add(R.drawable.interest_image04_btn)

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        otherId = intent.getStringExtra(AppKeyValue.instance.goodOtherId)
        otherProfileImage = intent.getStringExtra(AppKeyValue.instance.goodOtherProfileImage)
        otherTalkId = intent.getStringExtra(AppKeyValue.instance.goodOtherTalkId)
        otherType = intent.getStringExtra(AppKeyValue.instance.goodOtherType)

        Glide.with(this).load(otherProfileImage).into(civ_act_interest_profile)
        tv_act_interest_talk_id.text = otherTalkId
        if(otherType == resources.getString(R.string.information_member_marry)) {
            tv_act_interest_marry_type.setBackgroundColor(resources.getColor(R.color.color_main_type_marry))
        }
        else {
            tv_act_interest_marry_type.setBackgroundColor(resources.getColor(R.color.color_main_type_remarry))
        }
        tv_act_interest_marry_type.text = otherType
    }

    private fun setListener() {
        checkArrayCheck = arrayOfNulls(4)
        for(i in checkArrayCheck.indices) {
            checkArrayCheck[i] = findViewById<CheckBox>(checkArrayId[i])

            checkArrayCheck[i]?.let { it ->
                RxView.clicks(it)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe {
                            setPress(i)
                        }
            }?.let { disposeBag.add(it) }
        }

        /*    관심표현하기    */
        disposeBag.add(RxView.clicks(btn_act_interest_express)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val content = et_act_interest_content.text.toString()
                    if(!getImageChecked()) {
                        Utility.instance.showToast(this, resources.getString(R.string.msg_interest_image))
                    }
                    else {
                        ll_act_interest_progress.visibility = View.VISIBLE
                        mPresenter.setGood(id, otherId, /*content,*/ cardIndex)
                        Utility.instance.hideSoftKeyboard(this)
                    }
                })
    }

    private fun setPress(position: Int) {
        checkArray?.let { it ->
            it.map {
                it.isChecked = false
            }
            it[position].isChecked = true
        }
        imageArray?.get(position)?.let { iv_act_interest_image_detail.setImageResource(it) }

        when(position) {
            0 -> cardIndex = "6"
            1 -> cardIndex = "7"
            2 -> cardIndex = "8"
            3 -> cardIndex = "9"
        }
    }

    private fun getImageChecked(): Boolean {
        checkArray?.let {
            for(i in 0 until it.size) {
                if(it[i].isChecked) {
                    return true
                }
            }
        }
        return false
    }

    /*    관심있어요    */
    override fun setGoodComplete(result: String?) {
        ll_act_interest_progress.visibility = View.GONE
        Utility.instance.showToast(this, result)
        finish()
    }

    /*    관심있어요 호출실패    */
    override fun setGoodFailed(error: String?) {
        ll_act_interest_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
        Utility.instance.hideSoftKeyboard(this)
    }

}
