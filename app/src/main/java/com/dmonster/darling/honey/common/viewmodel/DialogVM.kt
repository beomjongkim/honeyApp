package com.dmonster.darling.honey.common.viewmodel

import android.view.View
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.BR
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.SelectorMultiItemVM

abstract class DialogVM(
    var title: String,
    var subTitle: String?,
    var strArr: Array<String>?,
    var viewType : String?,
    val lifecycleOwner: LifecycleOwner? = null
) : ViewModel(), LifecycleObserver {
    var customAdapter = CustomAdapter(R.layout.layout_item_selector_multi_type_b, lifecycleOwner)
    var selectedIndex: Int = 0
    var itemVMList = ArrayList<SelectorMultiItemVM>()

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
        setItem()
    }

    private fun setItem() {
        strArr?.let {
            for (str in it) {
                var itemVM =  object : SelectorMultiItemVM(str) {
                    override fun onSelected() {
                        selectedIndex = it.indexOf(title)
                        resetCheck(false)
                    }

                    override fun onDismissed() {
                        resetCheck(false)
                    }
                }
                customAdapter.dataList.add(RecyclerItemData(0, itemVM, BR.itemCheckVM))

                itemVMList.add(itemVM)
            }
            customAdapter.notifyDataSetChanged()
        }
    }

    private fun resetCheck(isCheck : Boolean){
        for (itemVM in itemVMList){
            itemVM.isChecked.set(isCheck)
        }
    }

    abstract fun onClickCancel(v: View)

    abstract fun onClickConfirm(v: View)

    abstract fun onClickBack(v: View)
}