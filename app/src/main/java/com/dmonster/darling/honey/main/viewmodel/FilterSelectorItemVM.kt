package com.dmonster.darling.honey.main.viewmodel

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.SelectorMultiItemVM
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.RecyclerVM
import com.dmonster.darling.honey.customview.ViewModelDialog
import java.util.*

class FilterSelectorItemVM(
    var title: String,
    var entries: Array<String>,
    var customAdapter: CustomAdapter
) :
    RecyclerVM(customAdapter) {
    private val subList = ArrayList<SelectorMultiItemVM>()
    var mDialog: Dialog? = null
    var choosenItem = MutableLiveData<String>().also { it.value = "" }

    init {
        customAdapter.dataList.addAll(fitArrToDataList(entries))
    }

    fun onClick(view: View) {
        view.context.let {
            if (mDialog == null) {
                val dialog = ViewModelDialog(
                    view.context,
                    this,
                    BR.selectorItemVM,
                    R.layout.layout_selector_popup_multi
                )
                dialog.setCanceledOnTouchOutside(false)
                setDialogView(dialog, it)
                mDialog = dialog
            }
            mDialog?.show()
        }
    }

    fun onClickCancel() {
//        initData()
        mDialog?.dismiss()
    }

    fun onClickConfirm() {
        saveChoosenItem()
//        initData()
        mDialog?.dismiss()
    }

    fun saveChoosenItem() {
        var res = ""
        for (item in subList) {
            if (item.isChecked.get() == true) {
                if (res == "")
                    res += item.title
                else
                    res += "," + item.title
            }
        }
        choosenItem.value = res
    }

    fun initData() {
        for (str in subList) {
            str.isChecked.set(false)
        }
        choosenItem.value =""
    }


    private fun fitArrToDataList(arr: Array<String>): ArrayList<RecyclerItemData> {
        val res = ArrayList<RecyclerItemData>()
        for (str in arr) {
            val itemVM = object : SelectorMultiItemVM(str) {
                override fun onDismissed() {
                }

                override fun onSelected() {
                }

            }
            res.add(RecyclerItemData(0, itemVM, BR.itemCheckVM))
            subList.add(itemVM)
        }
        return res
    }


    private fun setDialogView(dialog: Dialog, context: Context, wrap_height: Boolean = false) {
        dialog.show()
        val lp = WindowManager.LayoutParams()
        dialog.window?.let {
            lp.copyFrom(it.attributes)
            lp.width = (context.resources.displayMetrics.widthPixels * 0.94f).toInt()
            if (!wrap_height)
                lp.height = (context.resources.displayMetrics.heightPixels * 0.7f).toInt()
            else
                lp.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            it.attributes = lp
        }
    }
//    private fun setItemRecyclerView(){
//        customAdapter.dataList.clear()
//        for(itemVM in subList){
//            customAdapter.dataList.add(RecyclerItemData(0, itemVM, BR.itemCheckVM))
//        }
//    }

}