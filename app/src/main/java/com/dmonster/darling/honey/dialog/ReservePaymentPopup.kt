package com.dmonster.darling.honey.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.common.command.TwoBtnSwitch
import com.dmonster.darling.honey.point.viewmodel.ReservePaymentPopupVM

class ReservePaymentPopup(context: Context, var twoBtnSwitch: TwoBtnSwitch,var lifecycleOwner: LifecycleOwner) : Dialog(context), LifecycleObserver{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }
    var reservePaymentPopupVM = ReservePaymentPopupVM(twoBtnSwitch)
    fun init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            R.layout.layout_popup_register_payment,
            null,
            false
        )
        binding.setVariable(BR.registerPaymentVM, reservePaymentPopupVM)
        binding.lifecycleOwner = lifecycleOwner

        super.addContentView(
            binding.root,
            ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
            )
        )

        val lp = WindowManager.LayoutParams()
        this.window.let { it1 ->
            lp.copyFrom(it1.attributes)
            lp.width = (context.resources.displayMetrics.widthPixels * 0.78f).toInt()
            lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            it1.attributes = lp
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        hide()
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        dismiss()
    }
}