package com.dmonster.darling.honey.common.viewmodel

import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.*
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.SelectorMultiItemVM

abstract class CustomSpinnerVM(
     var adapter: CustomAdapter,
     var array: Array<String>,
    lifecycle: Lifecycle,
    var recyclerType : String = "vertical",
    var multi: Boolean = true
) :
    ViewModel(),
    LifecycleObserver {
    var isOpened = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var text = MutableLiveData<String>().also { it.value = "" }
    var choosenList = ArrayList<String>()
    var itemVMList = ArrayList<SelectorMultiItemVM>()


    init {
        lifecycle.addObserver(this)
        setItemVMList()
    }

    private fun initItemVMList(){
        var i = 0
        adapter.dataList.clear()

        while(i<itemVMList.size){
            itemVMList[i].isChecked.set(false)
            adapter.dataList.add(RecyclerItemData(0,itemVMList[i],BR.itemCheckVM))
            i++
        }
        adapter.notifyDataSetChanged()
    }

    private fun setItemVMList(){
        var x = 0
        while (x < array.size) {
            val str = array.get(x)

            val itemVM = object : SelectorMultiItemVM(str) {
                override fun onDismissed() {
                    if (multi) {
                        choosenList.remove(str)
                        setText()
                    }else{
                        initItemVMList()
                    }
                }

                override fun onSelected() {
                    if (multi) {
                        choosenList.add(str)
                        setText()
                    }else{
                        text.value = str
                        initItemVMList()
                        isOpened.value =false
                    }
                }
            }
            //어댑터에  적용
            adapter.dataList.add(RecyclerItemData(0, itemVM, BR.itemCheckVM))
            itemVMList.add(itemVM)
            x++
        }
    }


    fun onClick() {
        onOpen()
        isOpened.value = isOpened.value != true
    }

    abstract fun onOpen()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        isOpened.value = false
    }

    fun setText() {
        text.value = ""
        var i = 0
        while (i < choosenList.size) {
            if (i == 0) {
                text.value = choosenList.get(i)
            } else {
                text.value = text.value + " , " + choosenList.get(i)
            }
            i++
        }
    }



}