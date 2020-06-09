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

class CustomPopup : Dialog {
    constructor(
        context: Context,
        title: String,
        subTitle: String,
        lifecycleOwner: LifecycleOwner? = null
    ) : super(context) {
        mTitle = title
        mSubTitle = subTitle
        mLifecycleOwner = lifecycleOwner
        popupVM = PopupVM(mTitle, mSubTitle)
        init()
    }

    constructor(
        context: Context,
        title: String,
        subTitle: String, imgId: Int,
        customDialogInterface: DialogInterface.OnClickListener,
        lifecycleOwner: LifecycleOwner? = null
    ) : super(context) {
        mTitle = title
        mSubTitle = subTitle
        mImgId = imgId
        mLifecycleOwner = lifecycleOwner

        mCustomDialogInterface = object : CustomDialogInterface {
            override fun onConfirm(v: View) {
                customDialogInterface.onClick(this@CustomPopup, DialogInterface.BUTTON_POSITIVE)
                dismiss()
            }

            override fun onCancel(v: View) {
                customDialogInterface.onClick(this@CustomPopup, DialogInterface.BUTTON_NEGATIVE)
                dismiss()
            }
        }
        popupVM = PopupVM(mTitle, mSubTitle,  mCustomDialogInterface)
        init()
    }

    constructor(
        context: Context,
        title: String,
        subTitle: String, imgId: Int,
        customDialogInterface: CustomDialogInterface,
        lifecycleOwner: LifecycleOwner? = null
    ) : super(context) {
        mTitle = title
        mSubTitle = subTitle
        mImgId = imgId
        mLifecycleOwner = lifecycleOwner
        mCustomDialogInterface = object :CustomDialogInterface{
            override fun onConfirm(v: View) {
                customDialogInterface.onConfirm(v)
                dismiss()
            }

            override fun onCancel(v: View) {
                customDialogInterface.onCancel(v)
                dismiss()
            }

        }

        popupVM = PopupVM(mTitle, mSubTitle,  mCustomDialogInterface)
        init()
    }


    constructor(
        context: Context,
        title: String,
        subTitle: String,
       subTitleTwo :String , link : String , customDialogInterface: CustomDialogInterface ? =null,
        lifecycleOwner: LifecycleOwner? = null
    ) : super(context) {
        mTitle = title
        mSubTitle = subTitle
        mImgId = -1
        mLifecycleOwner = lifecycleOwner

        mCustomDialogInterface = null
        popupVM = PopupVM(mTitle, mSubTitle, mCustomDialogInterface, subTitleTwo, link)
        init()
    }

    var mTitle: String
    var mSubTitle: String
    var mImgId: Int = 0
    var mCustomDialogInterface: CustomDialogInterface? = null
    var mLifecycleOwner: LifecycleOwner? = null
    var popupVM: PopupVM

    fun init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var layoutId=R.layout.layout_viewmodel_popup
        if(mCustomDialogInterface == null){
            layoutId =   R.layout.layout_viewmodel_notice
        }
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            layoutId,
            null,
            false
        )
        binding.setVariable(BR.popupVM, popupVM)

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