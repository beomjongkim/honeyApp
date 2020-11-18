package com.dmonster.darling.honey.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel

class ViewModelDialog(context: Context, viewModel: ViewModel, vmID : Int, layoutID : Int) : Dialog(context) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val binding = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(context),layoutID,null,false)
        binding.setVariable(vmID, viewModel)
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
}