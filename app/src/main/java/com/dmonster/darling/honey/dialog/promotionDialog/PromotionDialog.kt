package com.dmonster.darling.honey.dialog.promotionDialog

import android.app.Activity
import android.app.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import android.view.Window
import android.view.WindowManager
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.util.Utility
import kotlinx.android.synthetic.main.promotion_dialog.*
import okhttp3.internal.Util
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class PromotionDialog(
    val activity: androidx.fragment.app.FragmentActivity
) : Dialog(activity), LifecycleObserver, ClickButton {
    override fun onClickSeeWeekLater() {
        val df = SimpleDateFormat("yyMMdd", Locale.getDefault())
        val nowTime = System.currentTimeMillis()
        val today = Date(nowTime)
        Utility.instance.savePref(context,"seeWeekLater",df.format(today))
        hide()
    }

    override fun onClickStart() {
        hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        activity.lifecycle.addObserver(this)
        val binding: com.dmonster.darling.honey.databinding.PromotionDialogBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.promotion_dialog, null, false)
        setContentView(binding.root)

        binding.setVariable(BR.promotionDialogVM, this)
        binding.executePendingBindings()

        setDialogWindowSetting(activity)
//        binding.setVariable(BR.promotionDialaogVM,viewModel)
    }

    //다이얼로그의 크기 등을 설정하는 코드.
    internal fun setDialogWindowSetting(activity: androidx.fragment.app.FragmentActivity) {
        //다이얼로그 주변을 흐릿하게 처리.

        val lpWindow = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.8f//흐릿한 정도

        //다이얼로그 크기 설정
        lpWindow.copyFrom(window!!.attributes)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val display = activity.windowManager.defaultDisplay
        val size = Point()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size)
        } else
            display.getSize(size)

//        val dm = context.resources.displayMetrics
//        val width = dm.widthPixels
//        val height = dm.heightPixels
        val width = size.x
        val height = size.y*0.9
        lpWindow.width = width
        lpWindow.height = height.toInt()
        window!!.attributes = lpWindow


    }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        dismiss()
    }
}
