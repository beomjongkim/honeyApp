package com.dmonster.darling.honey.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_withdrawal_select.*
import java.util.*
import java.util.concurrent.TimeUnit

class WithdrawalSelectDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable

    private lateinit var checkArrayId: Array<Int>
    private lateinit var checkArrayCheck: Array<CheckBox?>

    private var checkArray: ArrayList<CheckBox>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        isCancelable = false
        return inflater.inflate(R.layout.dialog_withdrawal_select, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        setListener()
    }

    private fun init() {
        tv_dlg_withdrawal_select_title.text = dialog?.context?.getString(R.string.main_more_menu_withdrawal)
        disposeBag = CompositeDisposable()

        checkArrayId = arrayOf(R.id.cb_dlg_withdrawal_select01, R.id.cb_dlg_withdrawal_select02, R.id.cb_dlg_withdrawal_select03, R.id.cb_dlg_withdrawal_select04)

        checkArray = ArrayList()
        checkArray?.add(cb_dlg_withdrawal_select01)
        checkArray?.add(cb_dlg_withdrawal_select02)
        checkArray?.add(cb_dlg_withdrawal_select03)
        checkArray?.add(cb_dlg_withdrawal_select04)
    }

    private fun setListener() {
        checkArrayCheck = arrayOfNulls(4)
        for(i in checkArrayCheck.indices) {
            checkArrayCheck[i] = dialog?.findViewById<CheckBox>(checkArrayId[i])

            checkArrayCheck[i]?.let { it ->
                RxView.clicks(it)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe {
                            setPress(i)
                        }
            }?.let { disposeBag.add(it) }
        }

        /*    취소    */
        disposeBag.add(RxView.clicks(tv_dlg_withdrawal_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_withdrawal_enter)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dialog?.context?.let { it1 ->
                        val id = Utility.instance.getPref(it1, AppKeyValue.instance.savePrefID)
                        val password = Utility.instance.getPref(it1, AppKeyValue.instance.savePrefPassword)
                        var reason: String? = null
                        when {
                            cb_dlg_withdrawal_select01.isChecked -> reason = cb_dlg_withdrawal_select01.text.toString()
                            cb_dlg_withdrawal_select02.isChecked -> reason = cb_dlg_withdrawal_select02.text.toString()
                            cb_dlg_withdrawal_select03.isChecked -> reason = cb_dlg_withdrawal_select03.text.toString()
                            cb_dlg_withdrawal_select04.isChecked -> reason = cb_dlg_withdrawal_select04.text.toString()
                        }
                        setWithdrawal(id, password, reason)
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
    }

    /*    회원탈퇴    */
    private fun setWithdrawal(id: String?, password: String?, reason: String?) {
        val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<BaseItem>) {
                item.let { it ->
                    if(it.isSuccess) {
                        dialog?.context?.let { it1 ->
                            Utility.instance.showAlert(it1, it1.getString(R.string.app_name), it1.getString(R.string.msg_app_withdrawal_complete), DialogInterface.OnClickListener { dialog, which ->
                                Utility.instance.setLogout(it1)
                                dismiss()
                            })
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestWithdrawal(ServerApi.instance.withdrawalMethod, id, password, reason)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
    }

}
