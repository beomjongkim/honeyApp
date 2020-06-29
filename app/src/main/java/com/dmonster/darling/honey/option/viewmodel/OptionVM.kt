package com.dmonster.darling.honey.option.viewmodel

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.*
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.agreement.view.UseActivity
import com.dmonster.darling.honey.alarm.view.AlarmActivity
import com.dmonster.darling.honey.block.view.BlockActivity
import com.dmonster.darling.honey.block_friends.view.BlockFriendsActivity
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.dialog.DormantClearDialog
import com.dmonster.darling.honey.information.view.MyInfoActivity
import com.dmonster.darling.honey.inquiry.view.InquiryMainActivity
import com.dmonster.darling.honey.magazine.view.MagazineActivity
import com.dmonster.darling.honey.point.view.ItemUseActivity
import com.dmonster.darling.honey.myinformation.view.MyProfileActivity
import com.dmonster.darling.honey.notice.view.NoticeActivity
import com.dmonster.darling.honey.option.data.SimpleMyInfoData
import com.dmonster.darling.honey.option.model.OptionModel
import com.dmonster.darling.honey.servicecenter.view.ServiceCenterActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class OptionVM(var fragmentManager: FragmentManager, lifecycle: Lifecycle, val id: String) :
    ViewModel(), LifecycleObserver {

    private var dormantState: Boolean = false
    private var profileState: Boolean = true
    private var popup: CustomPopup? = null

    var profileImg = MutableLiveData<String>().also {
        it.value = ""
    }
    var mb_nick = MutableLiveData<String>().also {
        it.value = "-"
    }
    var mb_coin = MutableLiveData<String>().also {
        it.value = "0개"
    }

    init {
        lifecycle.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        getData()
    }

    fun onClickAlarm(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            profileState -> {
                val intent = Intent(view.context, AlarmActivity::class.java)
                startActivity(view.context, intent, null)
            }
            else -> setProfileDialog(view.context)
        }
    }

    fun onClickInvitation(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            profileState -> {
                val gender = Utility.instance.UserData().getUserGender()
                if (gender == "F") {
                    Utility.instance.marketSharedFemale(view.context)
                } else {
                    Utility.instance.marketShared(view.context)
                }
            }
            else -> setProfileDialog(view.context)
        }
    }

    fun onClickBlock(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            profileState -> {
                val intent = Intent(view.context, BlockActivity::class.java)
                startActivity(view.context, intent, null)
            }
            else -> setProfileDialog(view.context)
        }
    }

    fun onClickInquiry(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            else -> {
                val intent = Intent(view.context, InquiryMainActivity::class.java)
                startActivity(view.context, intent, null)
            }
        }
    }

    fun onClickUse(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            else -> {
                val intent = Intent(view.context, UseActivity::class.java)
                startActivity(view.context, intent, null)
            }
        }
    }

    fun onClickServiceCenter(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            else -> {
                val intent = Intent(view.context, ServiceCenterActivity::class.java)
                startActivity(view.context, intent, null)
            }
        }
    }

    fun onClickMyInfo(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            profileState -> {
                val intent = Intent(view.context, MyInfoActivity::class.java)
                startActivity(view.context, intent, null)
            }
            else -> setProfileDialog(view.context)
        }
    }

    fun onClickNotice(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            else -> {
                val intent = Intent(view.context, NoticeActivity::class.java)
                startActivity(view.context, intent, null)
            }
        }
    }

    fun onClickCharge(view: View) {

    }

    fun onClickItemUse(view: View) {
        val intent = Intent(view.context, ItemUseActivity::class.java)
        startActivity(view.context, intent, null)
    }

    fun onClickLogOut(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            else -> {
                if (popup == null) {
                    popup = CustomPopup(
                        view.context,
                        view.context.getString(R.string.main_more_menu_logout),
                        view.context.getString(R.string.popup_logout_notice),
                        R.drawable.ic_logout_vivid,
                        object : CustomDialogInterface {
                            override fun onConfirm(v: View) {
                                onConfirm(v.context)
                            }

                            override fun onCancel(v: View) {
                                onCancel()
                            }

                        })
                }
                popup?.show()
            }
        }
    }

    private fun onConfirm(context: Context) {
        Utility.instance.setLogout(context)
        popup?.hide()
    }

    private fun onCancel() {
        popup?.hide()
    }


    fun onClickMagazine(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            else -> {
                val intent = Intent(view.context, MagazineActivity::class.java)
                startActivity(view.context, intent, null)
            }
        }
    }

    fun onClickBlockFriends(view: View) {
        val intent = Intent(view.context, BlockFriendsActivity::class.java)
        startActivity(view.context, intent, null)
    }

    fun onClickDormant(view: View) {
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
        when {
            dormantState -> setDormantDialog()
            profileState -> {
                view.context.let {context->
                    Utility.instance.showTwoButtonAlert(context, context.getString(R.string.main_more_menu_dormant), context.getString(R.string.msg_main_dormant),object : CustomDialogInterface{
                        override fun onConfirm(v: View) {
                            val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
                                override fun onComplete() {

                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }

                                override fun onNext(item: ResultItem<BaseItem>) {
                                    item.let {
                                        if(it.isSuccess) {
                                            context?.let { it1 ->
                                                Utility.instance.showAlert(it1, it1.getString(R.string.app_name), it1.getString(R.string.msg_main_dormant_complete), DialogInterface.OnClickListener { dialog, which ->
                                                    /*Utility.instance.savePref(it1, AppKeyValue.instance.savePrefDormant, AppKeyValue.instance.keyYes)*/
                                                    FirebaseAnalytics.getInstance(it1).logEvent("setDormant",
                                                        Bundle().also { it2 -> it2.putString("user_id", id)})
                                                    Utility.instance.setLogout(it1)
                                                })
                                            }
                                        }
                                    }
                                }
                            }

                            RetrofitProtocol().retrofit.requestDormant(ServerApi.instance.dormantMethod, id, AppKeyValue.instance.keyYes)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .unsubscribeOn(Schedulers.io())
                                .subscribe(subscriber)
                        }

                        override fun onCancel(v: View) {
                        }

                    })
                }

            }
            else -> setProfileDialog(view.context)
        }
    }

    fun onClickMyProfile(view: View) {
        val intent = Intent(view.context, MyProfileActivity::class.java)
        startActivity(view.context, intent, null)
    }

    private fun getData() {
        val mModel = OptionModel()
        val subscription = CompositeDisposable()

        val subscriber = object : DisposableObserver<ResultItem<SimpleMyInfoData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
//                mView.setIdealTypeFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<SimpleMyInfoData>) {
                item.let { it ->
                    if (it.isSuccess) {
                        it.item?.let { it ->
                            mb_coin.value = it.coin + "개"
                            mb_nick.value = it.mbNick
                            it.mbImg?.let {
                                if (it.mbImg?.isNotEmpty()!!)
                                    profileImg.value = it.mbImg?.get(0)
                            }

//                            mView.setIdealTypeComplete(sucAge, sucArea, sucHeight, sucWeight, sucStyle, sucIncome, sucEducation, sucReligion, sucBlood)
                        }
                    } else {
//                        mView.setIdealTypeFailed(it.message)
                    }
                }
            }
        }
        mModel.requestSimpleMyInfo(id, Utility.instance.UserData().getUserMb(), subscriber)
        subscription.add(subscriber)
    }

    private fun setDormantDialog() {
        val dormantClearDialog = DormantClearDialog()
        dormantClearDialog.show(fragmentManager, AppKeyValue.instance.tagDormantClearDlg)
    }

    private fun setProfileDialog(context: Context) {
        Utility.instance.showAlert(
            context,
            context.resources.getString(R.string.app_name),
            context.resources.getString(R.string.msg_error_profile),
            DialogInterface.OnClickListener { dialog, which ->
                EventBus.sendEventProfile(AppKeyValue.instance.eventBusProfile)
            })
    }


}