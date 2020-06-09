package com.dmonster.darling.honey.myinformation.view

import android.os.Bundle
import android.widget.Button
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
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
        init()
    }


    private fun init() {
        setContentView(R.layout.fragment_my_info_main)
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
