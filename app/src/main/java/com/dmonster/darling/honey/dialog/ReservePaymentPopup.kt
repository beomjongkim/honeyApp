package com.dmonster.darling.honey.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.dialog.viewmodel.ReservePaymentSpinnerVM
import com.dmonster.darling.honey.point.viewmodel.ReservePaymentPopupVM
import com.dmonster.darling.honey.util.Utility

class ReservePaymentPopup(context: Context, var lifecycleOwner: LifecycleOwner) : Dialog(context),
    LifecycleObserver {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    var reservePaymentPopupVM = ReservePaymentPopupVM()
    fun init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            R.layout.layout_popup_register_payment,
            null,
            false
        )

        val arrayAdapter = ArrayAdapter<String>(
            context,
            R.layout.custom_dropdown
        )
        binding.setVariable(BR.registerPaymentVM, reservePaymentPopupVM.also {
            it.radioGroupListener =
                RadioGroup.OnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.rb_reserve_popup2 -> {
                            it.needReceipt.value = "private"
                            arrayAdapter.clear()
                            arrayAdapter.addAll("휴대폰번호", "주민등록번호")
                            reservePaymentPopupVM.receiptType.value = "휴대폰번호"
                        }
                        R.id.rb_reserve_popup3 -> {
                            it.needReceipt.value = "business"
                            arrayAdapter.clear()
                            arrayAdapter.addAll("사업자등록번호")
                            reservePaymentPopupVM.receiptType.value = "사업자등록번호"
                        }
                        else -> {
                            it.needReceipt.value = ""
                        }
                    }
                }
        })

        binding.setVariable(BR.spinnerVM, ReservePaymentSpinnerVM(arrayAdapter).also {
            it.itemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    reservePaymentPopupVM.receiptType.value = arrayAdapter.getItem(position)
                }
            }
        })

        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
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
            lp.width = (context.resources.displayMetrics.widthPixels * 0.95f).toInt()
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