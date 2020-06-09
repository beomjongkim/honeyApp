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

class WithdrawalReDialog: androidx.fragment.app.DialogFragment() {

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
            tv_dlg_basic_content.text = it.resources.getString(R.string.msg_main_withdrawal_dormant)
            tv_dlg_basic_cancel.text = it.resources.getString(R.string.main_more_menu_dormant)
            tv_dlg_basic_enter.text = it.resources.getString(R.string.main_more_menu_withdrawal_do)
        }
    }

    private fun setListener() {
        /*    취소    */
        disposeBag.add(RxView.clicks(tv_dlg_basic_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val dormantDialog = DormantDialog()
                    activity?.supportFragmentManager?.let { it1 -> dormantDialog.show(it1, AppKeyValue.instance.tagDormantDlg) }
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_basic_enter)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val withdrawalSelectDialog = WithdrawalSelectDialog()
                    activity?.supportFragmentManager?.let { it1 -> withdrawalSelectDialog.show(it1, AppKeyValue.instance.tagWithdrawalSelectDlg) }
                    dismiss()
                })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
    }

}
