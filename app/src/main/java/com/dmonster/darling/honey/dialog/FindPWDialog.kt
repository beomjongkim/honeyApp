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
import kotlinx.android.synthetic.main.dialog_find_pw.*
import java.util.concurrent.TimeUnit

class FindPWDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        
        isCancelable = false
        return inflater.inflate(R.layout.dialog_find_pw, container, false)
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
        disposeBag.add(RxView.clicks(tv_dlg_find_pw_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_find_pw_enter)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dialog?.context?.let { it1 ->
                        val phone = et_dlg_find_pw_phone.text.toString()
                        val email = et_dlg_find_pw_email.text.toString()

                        if(TextUtils.isEmpty(phone)) {
                            Utility.instance.showToast(it1, it1.resources.getString(R.string.msg_popup_find_pw_phone))
                        }
                        else if(TextUtils.isEmpty(email)) {
                            Utility.instance.showToast(it1, it1.resources.getString(R.string.msg_popup_find_pw_email))
                        }
                        else {
                            ll_dlg_find_pw_progress.visibility = View.VISIBLE
                            getFindPW(phone, email)
                        }
                    }
                })
    }

    /*    비밀번호 찾기    */
    private fun getFindPW(phone: String?, email: String?) {
        val subscriber = object: DisposableObserver<ResultItem<FindIDPWData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<FindIDPWData>) {
                item.let {
                    ll_dlg_find_pw_progress.visibility = View.GONE
                    tv_dlg_find_pw_content.text = it.message
                }
            }
        }

        RetrofitProtocol().retrofit.requestFindIDPW(ServerApi.instance.findIDPWMethod, "pw", phone, email)
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
