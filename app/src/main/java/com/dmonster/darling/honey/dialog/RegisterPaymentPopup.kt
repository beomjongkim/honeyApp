package com.dmonster.darling.honey.customview

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.common.viewmodel.PopupVM

class RegisterPaymentPopup : Dialog {
    constructor(
        context: Context, price: Int, lifecycleOwner: LifecycleOwner? = null
    ) : super(context) {
        mLifecycleOwner = lifecycleOwner
        popupVM = RegisterPaymentPopup(context,price)
        init()
    }
    var mLifecycleOwner: LifecycleOwner? = null
    var popupVM: RegisterPaymentPopup

    fun init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var layoutId = R.layout.layout_popup_register_payment

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            layoutId,
            null,
            false
        )
        binding.setVariable(BR.registerPaymentVM, popupVM)

        if (mLifecycleOwner != null) {
            binding.lifecycleOwner = mLifecycleOwner
        }

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

}