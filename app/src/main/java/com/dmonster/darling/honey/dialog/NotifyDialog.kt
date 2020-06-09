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
import kotlinx.android.synthetic.main.dialog_notify.*
import java.util.*
import java.util.concurrent.TimeUnit

class NotifyDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable

    private lateinit var checkArrayId: Array<Int>
    private lateinit var checkArrayCheck: Array<CheckBox?>

    private var checkArray: ArrayList<CheckBox>? = null
    private var id: String? = null
    private var mbNo: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        isCancelable = false
        return inflater.inflate(R.layout.dialog_notify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()

        checkArrayId = arrayOf(R.id.cb_dlg_notify_select01, R.id.cb_dlg_notify_select02, R.id.cb_dlg_notify_select03, R.id.cb_dlg_notify_select04)

        checkArray = ArrayList()
        checkArray?.add(cb_dlg_notify_select01)
        checkArray?.add(cb_dlg_notify_select02)
        checkArray?.add(cb_dlg_notify_select03)
        checkArray?.add(cb_dlg_notify_select04)

        id = context?.let { Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
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
        disposeBag.add(RxView.clicks(tv_dlg_notify_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_notify_enter)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    var type: String? = null
                    when {
                        cb_dlg_notify_select01.isChecked -> type = AppKeyValue.instance.notifyType01
                        cb_dlg_notify_select02.isChecked -> type = AppKeyValue.instance.notifyType02
                        cb_dlg_notify_select03.isChecked -> type = AppKeyValue.instance.notifyType03
                        cb_dlg_notify_select04.isChecked -> type = AppKeyValue.instance.notifyType04
                    }
                    setNotify(id, mbNo, type)
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

    /*    신고하기    */
    private fun setNotify(id: String?, mbNo: String?, type: String?) {
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
                            Utility.instance.showAlert(it1, it1.getString(R.string.app_name), it1.getString(R.string.msg_popup_notify_complete), DialogInterface.OnClickListener { dialog, which ->
                                dismiss()
                            })
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestNotify(ServerApi.instance.notifyMethod, id, mbNo, type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .subscribe(subscriber)
    }

    fun setMbNumber(mbNo: String?) {
        this.mbNo = mbNo
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
    }

}
