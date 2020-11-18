package com.dmonster.darling.honey.option.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.databinding.FragmentOptionMainBinding
import com.dmonster.darling.honey.option.viewmodel.OptionVM
import com.dmonster.darling.honey.util.AppKeyValue
import com.kakao.util.helper.Utility

class OptionFragment : BaseFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentOptionMainBinding>(
            inflater,
            R.layout.fragment_option_main,
            container,
            false
        )
        fragmentManager?.let {
            context?.let { it1 ->
                binding.optionVM = OptionVM(
                    it,
                    lifecycle,
                    com.dmonster.darling.honey.util.Utility.instance.getPref(
                        it1,
                        AppKeyValue.instance.savePrefID
                    )!!
                )
                binding.lifecycleOwner = this
            }

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

}