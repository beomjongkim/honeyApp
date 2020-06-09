package com.dmonster.darling.honey.newMember.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel

import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.common.viewmodel.CustomSpinnerVM
import com.dmonster.darling.honey.common.viewmodel.SpinnerRangeVM

import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.databinding.FragmentIdealBinding
import com.dmonster.darling.honey.newMember.viewmodel.IdealVM
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility

class NewMemberFragment : BaseFragment() {
    lateinit var binding: FragmentIdealBinding
    var spinnerVMList = ArrayList<ViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentIdealBinding>(
            inflater,
            R.layout.fragment_ideal,
            container,
            false
        )

        val spinnerAgeVM = object : CustomSpinnerVM(
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            resources.getStringArray(R.array.ideal_type_age_array),
            lifecycle,
            "grid"
        ) {
            override fun onOpen() {
                setOpen(0)
            }
        }.also {
            spinnerVMList.add(it)
        }
        val spinnerAreaVM = object : CustomSpinnerVM(
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            resources.getStringArray(R.array.ideal_type_area_array),
            lifecycle,
            "grid"
        ) {
            override fun onOpen() {
                setOpen(1)
            }
        }.also {
            spinnerVMList.add(it)
        }
        val spinnerIncomeVM = object : CustomSpinnerVM(
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            resources.getStringArray(R.array.ideal_type_income_array),
            lifecycle,
            "grid"
        ) {
            override fun onOpen() {
                setOpen(2)
            }
        }.also {
            spinnerVMList.add(it)
        }
        val spinnerEduVM = object : CustomSpinnerVM(
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            resources.getStringArray(R.array.member_education_array),
            lifecycle,
            "grid"
        ) {
            override fun onOpen() {
                setOpen(3)
            }
        }.also {
            spinnerVMList.add(it)
        }
        val spinnerHeightVM = object : SpinnerRangeVM(
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            resources.getStringArray(R.array.ideal_type_height_array),
            lifecycle, "vertical"
        ) {
            override fun onOpen() {
                setOpen(4)
            }
        }.also {
            spinnerVMList.add(it)
        }
        val styleArr: Array<String>
        if (Utility.instance.UserData().getUserGender() == "F")
            styleArr = resources.getStringArray(R.array.member_mystyle_male_array)
        else
            styleArr = resources.getStringArray(R.array.member_mystyle_female_array)
        val spinnerStyleVM = object : CustomSpinnerVM(
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            styleArr,
            lifecycle,
            "grid"
        ) {
            override fun onOpen() {
                setOpen(5)
            }
        }.also {
            spinnerVMList.add(it)
        }
        val spinnerReligionVM = object : CustomSpinnerVM(
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            resources.getStringArray(R.array.member_religion_array),
            lifecycle
        ) {
            override fun onOpen() {
                setOpen(6)
            }
        }.also {
            spinnerVMList.add(it)
        }
        val spinnerBloodVM = object : CustomSpinnerVM(
            CustomAdapter(R.layout.layout_item_selector_multi_type_b, this),
            resources.getStringArray(R.array.member_blood_array),
            lifecycle
        ) {
            override fun onOpen() {
                setOpen(7)
            }
        }.also {
            spinnerVMList.add(it)
        }
        context?.let {
            val id: String? = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID)
            binding.idealVM = fragmentManager?.let { it1 ->
                IdealVM(
                    it1,
                    id,
                    spinnerAgeVM,
                    spinnerAreaVM,
                    spinnerIncomeVM,
                    spinnerEduVM,
                    spinnerHeightVM,
                    spinnerStyleVM,
                    spinnerReligionVM,
                    spinnerBloodVM
                )
            }
        }
        binding.lifecycleOwner = this
        return binding.root
    }

    fun setOpen(index: Int) {
        binding.idealVM?.let {
            if (index != 0)
                if (it.ageVM.isOpened.value!!) {
                    it.ageVM.isOpened.value = false
                }
            if (index != 1)
                if (it.areaVM.isOpened.value!!) {
                    it.areaVM.isOpened.value = false
                }
            if (index != 2)
                if (it.incomeVM.isOpened.value!!) {
                    it.incomeVM.isOpened.value = false
                }
            if (index != 3)
                if (it.eduVM.isOpened.value!!) {
                    it.eduVM.isOpened.value = false
                }
            if (index != 4) {
                if (it.heigthVM.isOpenedMax.value!!) {
                    it.heigthVM.isOpenedMax.value = false
                }
                if (it.heigthVM.isOpenedMin.value!!) {
                    it.heigthVM.isOpenedMin.value = false
                }
            }
            if (index != 5)
                if (it.styleVM.isOpened.value!!) {
                    it.styleVM.isOpened.value = false
                }
            if (index != 6)
                if (it.religionVM.isOpened.value!!) {
                    it.religionVM.isOpened.value = false
                }
            if (index != 7)
                if (it.bloodVM.isOpened.value!!) {
                    it.bloodVM.isOpened.value = false
                }


        }
    }

}
