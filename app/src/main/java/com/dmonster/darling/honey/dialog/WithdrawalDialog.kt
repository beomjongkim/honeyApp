package com.dmonster.darling.honey.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.item.data.CoinData
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_basic.*
import kotlinx.android.synthetic.main.dialog_item_talk.*
import java.util.concurrent.TimeUnit

class WithdrawalDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        isCancelable = false
        return inflater.inflate(R.layout.dialog_basic, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        dialog?.context?.let {
            tv_dlg_basic_title.text = it.getString(R.string.main_more_menu_withdrawal)
            tv_dlg_basic_content.text = it.resources.getString(R.string.msg_main_withdrawal)

            val id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID)
            setNowCoin(id)
        }
    }

    private fun setListener() {
        /*    취소    */
        disposeBag.add(RxView.clicks(tv_dlg_basic_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_basic_enter)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val withdrawalReDialog = WithdrawalReDialog()
                    activity?.supportFragmentManager?.let { it1 -> withdrawalReDialog.show(it1, AppKeyValue.instance.tagWithdrawalReDlg) }
                    dismiss()
                })
    }

    /*    현재 보유코인    */
    private fun setNowCoin(id: String?) {
        val subscriber = object: DisposableObserver<ResultItem<CoinData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                dialog?.context?.let { Utility.instance.showToast(it, e.message) }
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<CoinData>) {
                item.let { it ->
                    dialog?.context?.let { it1 ->
                        if(it.isSuccess) {
                            val nowCoin = String.format(it1.resources.getString(R.string.popup_item_sum_coin), it.item?.cntCoin)
                            tv_dlg_basic_coin.visibility = View.VISIBLE
                            tv_dlg_basic_coin.text = String.format(it1.resources.getString(R.string.msg_main_withdrawal_coin), nowCoin)
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestPossessionCoin(ServerApi.instance.possessionCoinMethod, id)
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
