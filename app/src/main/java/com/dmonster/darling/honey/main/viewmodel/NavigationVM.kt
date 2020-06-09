package com.dmonster.darling.honey.main.viewmodel

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.dialog.DormantClearDialog
import com.dmonster.darling.honey.item.view.ItemFragment
import com.dmonster.darling.honey.newMember.view.NewMemberFragment
import com.dmonster.darling.honey.item.view.ItemMainFragment
import com.dmonster.darling.honey.main.data.NaviData
import com.dmonster.darling.honey.main.model.MainModel
import com.dmonster.darling.honey.main.view.MainFragment
import com.dmonster.darling.honey.myactivity.view.MyActMainFragment
import com.dmonster.darling.honey.option.view.OptionFragment
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.retrofit.ResultItem
import io.reactivex.observers.DisposableObserver

class NavigationVM(var fragmentManager: FragmentManager, var mb_id: String, lifecycle: Lifecycle) : ViewModel(), LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }

    var countTotal = MutableLiveData<Int>().also {
        it.value = 0
    }

    var fragmentNumber = MutableLiveData<Int>().also {
        it.value = fragMain
    }

    private var newFragment: androidx.fragment.app.Fragment? = null

    private var dormantState: Boolean = false
    private var profileState: Boolean = true

    var isGray0 = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var isGray1 = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var isGray2 = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var isGray3 = MutableLiveData<Boolean>().also {
        it.value = false
    }
    var isGray4 = MutableLiveData<Boolean>().also {
        it.value = false
    }

    private val fragMain = 0
    private val fragMarket = 1
    private val fragIdeal = 2
    private val fragMe = 3
    private val fragOption = 4

    fun getCountData() {
        val subscriber = object : DisposableObserver<ResultItem<NaviData>>() {
            override fun onNext(t: ResultItem<NaviData>) {
                t.item?.let {
                    countTotal.value = it.cnt_mst_not_read?.toInt()
                }
            }

            override fun onComplete() {
            }

            override fun onError(e: Throwable) {
            }

        }
        MainModel().getNaviCount(mb_id, subscriber)
    }

    fun onClickMain(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        if (dormantState)
            setDormantDialog()
        else {
            fragmentReplace(fragMain)
            fragmentNumber.value = fragMain
        }
    }

    fun onClickMarket(view: View) {
//        val subscriber = object : DisposableObserver<ResultItem<NoticePopupData>>() {
//            override fun onNext(t: ResultItem<NoticePopupData>) {
//                t.item?.let {
//
//                    Utility.instance.showNotice(view.context,it.nt_title!!,it.nt_subtitle!!,it.nt_subtitle2!!,it.nt_link!!)
//                }
//            }
//
//            override fun onComplete() {
//            }
//
//            override fun onError(e: Throwable) {
//            }
//
//        }
//        MainModel().getNotice(subscriber)
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            profileState -> {
                fragmentReplace(fragMarket)
                fragmentNumber.value = fragMarket
            }
            else -> setProfileDialog(view.context)
        }

    }

    fun onClickNewMember(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            newFragment !is NewMemberFragment -> {
                fragmentReplace(fragIdeal)
                fragmentNumber.value = fragIdeal

            }
        }
    }

    fun onClickMyPopularity(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            profileState -> {
                if (newFragment !is ItemMainFragment) {
                    fragmentReplace(fragMe)
                    fragmentNumber.value = fragMe

                }
            }
            else -> setProfileDialog(view.context)
        }
    }

    fun onClickOption(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            newFragment !is OptionFragment -> {
                fragmentReplace(fragOption)
                fragmentNumber.value = fragOption
            }
        }
    }


    fun fragmentReplace(reqNewFragmentIndex: Int) {
        getCountData()
        newFragment = getFragment(reqNewFragmentIndex)

        val transaction = fragmentManager.beginTransaction()
        newFragment?.let { transaction.replace(R.id.fl_act_main_content_layout, it) }
        transaction.commit()
//        Utility.instance.hideSoftKeyboard(this)
    }

    private fun getFragment(idx: Int): androidx.fragment.app.Fragment? {
        var newFragment: androidx.fragment.app.Fragment? = null

        when (idx) {
            fragMain -> newFragment = MainFragment()

            fragMarket -> newFragment = ItemFragment()

            fragIdeal -> newFragment = NewMemberFragment()
//            fragIdeal -> newFragment = MyProfileActivity()

            fragMe -> newFragment = MyActMainFragment()

            fragOption -> newFragment = OptionFragment()
        }
        selectView(idx)

        return newFragment
    }


    private fun setDormantDialog() {
        val dormantClearDialog = DormantClearDialog()
        dormantClearDialog.show(fragmentManager, AppKeyValue.instance.tagDormantClearDlg)
    }


    private fun setProfileDialog(context: Context) {
        var userNick = "회원"

        Utility.instance.UserData().getUserNick()?.let {
            userNick =  it
        }
        Utility.instance.showAlert(
                context,
                context.resources.getString(R.string.app_name),
               userNick+ context.resources.getString(R.string.msg_error_profile),
                DialogInterface.OnClickListener { dialog, which ->
                    EventBus.sendEventProfile(AppKeyValue.instance.eventBusProfile)
                })
    }

    fun selectView(id: Int) {
        isGray0.value = false
        isGray1.value = false
        isGray2.value = false
        isGray3.value = false
        isGray4.value = false

        when (id) {
            fragMain -> isGray0.value = true

            fragMarket -> isGray1.value = true

            fragIdeal -> isGray2.value = true

            fragMe -> isGray3.value = true

            fragOption -> isGray4.value = true

            else -> isGray0.value = true
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        getCountData()
    }

}