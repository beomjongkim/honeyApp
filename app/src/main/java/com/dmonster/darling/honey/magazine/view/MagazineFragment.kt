package com.dmonster.darling.honey.magazine.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.databinding.FragmentMagazineBinding
import com.dmonster.darling.honey.magazine.viewmodel.MagazineVM
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility


class MagazineFragment : BaseFragment() {
    lateinit var binding : FragmentMagazineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_magazine,container,false)
        setViewModel()
        return binding.root
    }

    private fun setViewModel(){
        context?.let {
            binding.magazineVM = MagazineVM(CustomAdapter(R.layout.item_magazine,this))
            binding.lifecycleOwner = this
            binding.ctbActMagazine.setTitle("매거진")
        }
    }


}