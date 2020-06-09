package com.dmonster.darling.honey.main.viewmodel

import android.content.res.Resources
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter

abstract class FilterEtcVM(mResources: Resources) : ViewModel() {
    var religionVM = FilterSelectorItemVM(
        "종교",
        mResources.getStringArray(R.array.member_religion_array),
        CustomAdapter(R.layout.layout_item_selector_multi_type_a)
    )
    var incomeVM = FilterSelectorItemVM(
        "연봉",
        mResources.getStringArray(R.array.member_income_array),
        CustomAdapter(R.layout.layout_item_selector_multi_type_a)
    )
    var fortuneVM = FilterSelectorItemVM(
        "재산",
        mResources.getStringArray(R.array.member_fortune_array),
        CustomAdapter(R.layout.layout_item_selector_multi_type_a)
    )
    var blTypeVM = FilterSelectorItemVM(
        "혈액형",
        mResources.getStringArray(R.array.member_blood_array),
        CustomAdapter(R.layout.layout_item_selector_multi_type_a)
    )
    var educationVM = FilterSelectorItemVM(
        "학력",
        mResources.getStringArray(R.array.member_education_array),
        CustomAdapter(R.layout.layout_item_selector_multi_type_a)
    )
    var drinkVM = FilterSelectorItemVM(
        "음주",
        mResources.getStringArray(R.array.member_drink),
        CustomAdapter(R.layout.layout_item_selector_multi_type_a)
    )

    var babyVM = FilterSelectorItemVM(
        "자녀",
        arrayOf("자녀 있음", "자녀 없음"),
        CustomAdapter(R.layout.layout_item_selector_multi_type_a)
    )


//    var baby = MutableLiveData<String>().also { it.value = "" }
    var marry_pic = MutableLiveData<Boolean>().also { it.value = false }
    var profile_pic = MutableLiveData<Boolean>().also { it.value = false }


    var itemVMs: Array<FilterSelectorItemVM> = arrayOf(religionVM, incomeVM, fortuneVM, blTypeVM, educationVM, drinkVM, babyVM)

//    var onCheckedChangeListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
//        if (group != null) {
//            val indexOfChecked = group.indexOfChild(group.findViewById(checkedId))
//            when(indexOfChecked){
//                0-> baby.value = ""
//                1-> baby.value = "1"
//                2-> baby.value = "0"
//            }
//        }
//    }

    fun onClickInit(view: View) {
        religionVM.initData()
        incomeVM.initData()
        fortuneVM.initData()
        blTypeVM.initData()
        educationVM.initData()
        drinkVM.initData()
        babyVM.initData()
//        baby.value = ""
        marry_pic.value = false
        profile_pic.value = false
        afterClickInit()
    }

    abstract fun onClickBack()
    abstract fun onClickSearch()
    abstract fun afterClickInit()
}