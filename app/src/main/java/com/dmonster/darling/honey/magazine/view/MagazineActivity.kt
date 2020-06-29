package com.dmonster.darling.honey.magazine.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.databinding.ActivityMagazineBinding
import com.dmonster.darling.honey.magazine.viewmodel.MagazineVM
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility


class MagazineActivity : BaseActivity() {
    lateinit var binding : ActivityMagazineBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_magazine)
        setViewModel()
    }

    private fun setViewModel(){
        binding.bannerVM = BannerVM(Utility.instance.getPref(this, AppKeyValue.instance.savePrefID),lifecycle)
        binding.magazineVM = MagazineVM(CustomAdapter(R.layout.item_magazine,this))
        binding.lifecycleOwner = this
    }
}