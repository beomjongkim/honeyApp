package com.dmonster.darling.honey.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.common.viewmodel.DialogVM
import kotlinx.android.synthetic.main.fragment_inquiry_inquire.*

abstract class CustomArrayDialog(context: Context, title: String, subTitle: String, strArr: Array<String>, viewType : String = "vertical") : Dialog(context) {
    var dialogVM =
            object : DialogVM(
                    title,
                    subTitle,
                    strArr,
                    viewType
            ) {
                override fun onClickBack(v: View) {
                    hide()
                }

                override fun onClickCancel(v: View) {
                    hide()
                }

                override fun onClickConfirm(v: View) {
                    onConfirm()
                    hide()
                }
            }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(context), R.layout.layout_viewmodel_dialog, null, false)
        binding.setVariable(BR.dialogVM, dialogVM)
        binding.lifecycleOwner = context as FragmentActivity
        super.addContentView(
                binding.root,
                ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                )
        )

        val lp = WindowManager.LayoutParams()
        this.window?.let { it1 ->
            lp.copyFrom(it1.attributes)
            lp.width = (context.resources.displayMetrics.widthPixels * 0.94f).toInt()
            lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            it1.attributes = lp
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hide()
    }

    abstract fun onConfirm()
}