package com.dmonster.darling.honey.common.viewmodel

import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.*
import com.dmonster.darling.honey.custom_recyclerview.model.RecyclerItemData
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.custom_recyclerview.viewmodel.SelectorMultiItemVM

abstract class SpinnerRangeVM(
    var minAdapter: CustomAdapter,
    var maxAdapter: CustomAdapter,
    var array: Array<String>,
    lifecycle: Lifecycle,
    var recyclerType: String = "vertical"
) :
    ViewModel(),
    LifecycleObserver {
    var isOpenedMax = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var isOpenedMin = MutableLiveData<Boolean>().also {
        it.value = false
    }

    var textMax = MutableLiveData<String>().also { it.value = "" }
    var textMin = MutableLiveData<String>().also { it.value = "" }

    var minItemVMList = ArrayList<SelectorMultiItemVM>()
    var maxItemVMList = ArrayList<SelectorMultiItemVM>()
    var minIndex: Int = 0


    init {
        lifecycle.addObserver(this)
//        setItemVMList(false, textMax, isOpenedMax, maxAdapter, itemVMList)
        //미니멈 스피너 세팅
        setItemVMList(true)
        setItemVMList(false)
    }

    private fun initItemVMList(isMin: Boolean) {
        if (isMin)
            minAdapter.dataList.clear()
        else
            maxAdapter.dataList.clear()
        if (isMin) {
            for (itemVM in minItemVMList) {
                itemVM.isChecked.set(false)
                val i = minItemVMList.indexOf(itemVM)
                minAdapter.dataList.add(RecyclerItemData(0, minItemVMList[i], BR.itemCheckVM))

            }
            minAdapter.notifyDataSetChanged()
        } else {
            for (itemVM in maxItemVMList) {
                itemVM.isChecked.set(false)
                val i = maxItemVMList.indexOf(itemVM)
                if (i >= minIndex)
                    maxAdapter.dataList.add(RecyclerItemData(0, maxItemVMList[i], BR.itemCheckVM))

            }
            maxAdapter.notifyDataSetChanged()
        }
    }

    private fun setMinIndex(str: String) {
        textMin.value?.let {
            minIndex = array.indexOf(str)
        }
    }

    private fun setItemVMList(
        isMin: Boolean
    ) {
        var x = 0
        while (x < array.size) {
            val str = array.get(x)
            val itemVM = object : SelectorMultiItemVM(str) {
                override fun onDismissed() {
//                    initItemVMList(isMin, minAdapter)
                    if (isMin) {
                        textMin.value = ""
                        textMax.value = ""
                        initItemVMList(true)
                    } else {
                        textMax.value = ""
                        initItemVMList(false)
                    }
                }

                override fun onSelected() {
                    if (isMin) {
                        //미니멈 스피너에서 선택한 글자 저장.
                        textMin.value = str
                        textMax.value = ""
                        //미니멈 스피너에서 선택한 글자의 인덱스 받아오기
                        setMinIndex(str)
                        //미니멈 스피너를 선택했을 때 맥시멈 스피너 세팅
                        //중복선택 안되도록 클릭 시 모든 아이템 리셋
                        initItemVMList(true)
                        resetMaxAdapter()

                    } else {
                        //맥시멈 스피너에서 선택한 글자 저장
                        textMax.value = str
                        initItemVMList(false)
                    }
                }
            }
            //어댑터에  적용
            if (isMin) {
                minAdapter.dataList.add(RecyclerItemData(0, itemVM, BR.itemCheckVM))
                // 상태를 저장할 VM을 저장.
                minItemVMList.add(itemVM)
            } else {
                maxAdapter.dataList.add(RecyclerItemData(0, itemVM, BR.itemCheckVM))
                maxItemVMList.add(itemVM)
            }
            x++
        }
        if (isMin)
            minAdapter.notifyDataSetChanged()
        else {
            maxAdapter.notifyDataSetChanged()
        }
    }

    private fun resetMaxAdapter() {
        maxAdapter.dataList.clear()
        for (itemVM in maxItemVMList) {
            val i = maxItemVMList.indexOf(itemVM)
            if (minIndex <= i)
                maxAdapter.dataList.add(RecyclerItemData(0, itemVM, BR.itemCheckVM))
        }
        maxAdapter.notifyDataSetChanged()
    }

    fun onClickMaxSpinner() {
        onOpen()
        isOpenedMax.value = isOpenedMax.value != true
    }

    fun onClickMinSpinner() {
        onOpen()
        isOpenedMin.value = isOpenedMin.value != true
    }


    abstract fun onOpen()

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        isOpenedMax.value = false
        isOpenedMin.value = false
    }


}