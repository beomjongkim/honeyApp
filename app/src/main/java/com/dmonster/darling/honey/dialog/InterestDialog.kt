package com.dmonster.darling.honey.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.profile.view.GoodActivity
import com.dmonster.darling.honey.profile.view.InterestActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_good.*
import kotlinx.android.synthetic.main.dialog_interest.*
import java.util.concurrent.TimeUnit

class InterestDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable

    private var otherId: String? = null
    private var otherProfileImage: String? = null
    private var otherTalkId: String? = null
    private var otherType: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        isCancelable = false
        return inflater.inflate(R.layout.dialog_interest, container, false)
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
        disposeBag.add(RxView.clicks(tv_dlg_interest_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_interest_enter)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val intent = Intent(context, InterestActivity::class.java)
                    intent.putExtra(AppKeyValue.instance.goodOtherId, otherId)
                    intent.putExtra(AppKeyValue.instance.goodOtherProfileImage, otherProfileImage)
                    intent.putExtra(AppKeyValue.instance.goodOtherTalkId, otherTalkId)
                    intent.putExtra(AppKeyValue.instance.goodOtherType, otherType)
                    startActivity(intent)
                    dismiss()
                })
    }

    fun setOtherInfo(otherId: String?, otherProfileImage: String?, otherTalkId: String?, otherType: String?) {
        this.otherId = otherId
        this.otherProfileImage = otherProfileImage
        this.otherTalkId = otherTalkId
        this.otherType = otherType
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
    }

}
