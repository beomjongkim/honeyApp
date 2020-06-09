package com.dmonster.darling.honey.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_basic.*
import java.util.concurrent.TimeUnit

class DormantDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable

    private var id: String? = null

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
            tv_dlg_basic_title.text = it.getString(R.string.main_more_menu_dormant)
            tv_dlg_basic_content.text = it.resources.getString(R.string.msg_main_dormant)
            id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID)
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
                    setDormant(id, AppKeyValue.instance.keyYes)
                })
    }

    /*    계정 휴면    */
    private fun setDormant(id: String?, type: String?) {
        val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<BaseItem>) {
                item.let {
                    if(it.isSuccess) {
                        dialog?.context?.let { it1 ->
                            Utility.instance.showAlert(it1, it1.getString(R.string.app_name), it1.getString(R.string.msg_main_dormant_complete), DialogInterface.OnClickListener { dialog, which ->
                                /*Utility.instance.savePref(it1, AppKeyValue.instance.savePrefDormant, AppKeyValue.instance.keyYes)*/
                                FirebaseAnalytics.getInstance(it1).logEvent("setDormant",Bundle().also { it2 -> it2.putString("user_id", id)})
                                Utility.instance.setLogout(it1)
                                dismiss()
                            })
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestDormant(ServerApi.instance.dormantMethod, id, type)
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
