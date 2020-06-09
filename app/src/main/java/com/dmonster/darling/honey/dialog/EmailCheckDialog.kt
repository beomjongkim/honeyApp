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
import com.dmonster.darling.honey.util.AppKeyValue
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_basic.*
import java.util.concurrent.TimeUnit

class EmailCheckDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable
    private var email: String? = null

    var enterClick: View.OnClickListener? = null

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

        tv_dlg_basic_content_title.visibility = View.VISIBLE
        tv_dlg_basic_content_title.text = email

        dialog?.context?.let {
            tv_dlg_basic_title.text = it.getString(R.string.popup_email_check_title)
            tv_dlg_basic_content.text = it.resources.getString(R.string.popup_email_check_content)
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
        tv_dlg_basic_enter.setOnClickListener(enterClick)
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
    }

}
