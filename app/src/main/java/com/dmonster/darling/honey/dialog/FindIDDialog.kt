package com.dmonster.darling.honey.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.text.TextUtils
import android.view.*
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.login.data.FindIDPWData
import com.dmonster.darling.honey.login.data.LoginData
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_find_id.*
import java.util.concurrent.TimeUnit

class FindIDDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable
    private var emailSuccess: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        
        isCancelable = false
        return inflater.inflate(R.layout.dialog_find_id, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
    }

    private fun setListener() {
        /*    취소    */
        disposeBag.add(RxView.clicks(tv_dlg_find_id_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_find_id_enter)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dialog?.context?.let { it1 ->
                        if(emailSuccess) {
                            dismiss()
                        }
                        else {
                            val phone = et_dlg_find_id_phone.text.toString()

                            if(TextUtils.isEmpty(phone)) {
                                Utility.instance.showToast(it1, it1.resources.getString(R.string.popup_find_id_content))
                            }
                            else {
                                ll_dlg_find_id_progress.visibility = View.VISIBLE
                                getFindID(phone)
                            }
                        }
                    }
                })
    }

    /*    아이디 찾기    */
    private fun getFindID(phone: String?) {
        val subscriber = object: DisposableObserver<ResultItem<FindIDPWData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<FindIDPWData>) {
                item.let {
                    if(it.isSuccess) {
                        ll_dlg_find_id_progress.visibility = View.GONE
                        tv_dlg_find_id_content.text = it.item?.mbId
                    }
                    else {
                        ll_dlg_find_id_progress.visibility = View.GONE
                        tv_dlg_find_id_content.text = it.message
                    }
                    emailSuccess = true
                }
            }
        }

        RetrofitProtocol().retrofit.requestFindIDPW(ServerApi.instance.findIDPWMethod, "id", phone, "")
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
