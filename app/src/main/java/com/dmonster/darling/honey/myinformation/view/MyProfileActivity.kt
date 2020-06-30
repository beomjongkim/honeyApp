package com.dmonster.darling.honey.myinformation.view

import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.databinding.ActivityNoticeBinding
import com.dmonster.darling.honey.databinding.FragmentMyInfoMainBinding
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_my_info_main.*
import java.util.ArrayList

class MyProfileActivity : BaseActivity() {

    private lateinit var disposeBag: CompositeDisposable

    private val fragMember = 0
    private val fragIdealType = 1
    private var arrMenus: ArrayList<Button>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding : FragmentMyInfoMainBinding = DataBindingUtil.setContentView(this,R.layout.fragment_my_info_main)
        binding.bannerVM =
            BannerVM(Utility.instance.getPref(this, AppKeyValue.instance.savePrefID), lifecycle, this)
        binding.lifecycleOwner = this
        init()

    }


    private fun init() {
//        setContentView(R.layout.fragment_my_info_main)
        disposeBag = CompositeDisposable()
        ctb_frag_my_info_main.setTitle(getString(R.string.my_profile_title))

//        val bundle = arguments?.getString(AppKeyValue.instance.idealTypeBundle)
//        if(bundle == AppKeyValue.instance.idealTypeBundle) {
//            setPress(fragIdealType)
//            fragmentReplace(fragIdealType)
//        }
//        else {
        setPress(fragMember)
        fragmentReplace(fragMember)
//        }
    }

    private fun setPress(position: Int) {
        arrMenus?.let { it ->
            it.map {
                it.isSelected = false
            }
            it[position].isSelected = true
        }
    }

    private fun fragmentReplace(reqNewFragmentIndex: Int) {
        val newFragment: androidx.fragment.app.Fragment? = getFragment(reqNewFragmentIndex)

        val transaction = supportFragmentManager.beginTransaction()
        newFragment?.let { transaction.replace(R.id.fl_frag_my_info_main_content_layout, it) }
        transaction.commit()

        Utility.instance.hideSoftKeyboard(this)
    }

    private fun getFragment(idx: Int): androidx.fragment.app.Fragment? {
        var newFragment: androidx.fragment.app.Fragment? = null

        when (idx) {
            fragMember -> newFragment = MyProfileFragment()
        }

        return newFragment
    }

}
