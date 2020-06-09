package com.dmonster.darling.honey.myactivity.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.util.AppKeyValue
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_my_act_main.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class MyActMainFragment: BaseFragment() {

    private lateinit var disposeBag: CompositeDisposable

    private val fragTalk = 0
    private val fragReading = 1
    private val fragProfile = 2
    private var arrMenus: ArrayList<Button>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_act_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()

        arrMenus = ArrayList()
        arrMenus?.add(btn_frag_my_act_main_talk)
        arrMenus?.add(btn_frag_my_act_main_reading)
        arrMenus?.add(btn_frag_my_act_main_profile)

        val bundle = arguments?.getString(AppKeyValue.instance.pushNewMember)
        if(bundle != AppKeyValue.instance.pushNewMember) {
            btn_frag_my_act_main_talk.isSelected = true
            fragmentReplace(fragTalk)
        }
    }

    private fun setListener() {
        /*    나의톡    */
        disposeBag.add(RxView.clicks(btn_frag_my_act_main_talk)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    setPress(fragTalk)
                    fragmentReplace(fragTalk)
                })

//        /*    신규회원    */
//        disposeBag.add(RxView.clicks(btn_frag_my_act_main_new_member)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe {
//                    setPress(fragNewMember)
//                    fragmentReplace(fragNewMember)
//                })

        /*    내 프로필 열람    */
        disposeBag.add(RxView.clicks(btn_frag_my_act_main_reading)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    setPress(fragReading)
                    fragmentReplace(fragReading)
                })

        /*    내가 본 프로필    */
        disposeBag.add(RxView.clicks(btn_frag_my_act_main_profile)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    setPress(fragProfile)
                    fragmentReplace(fragProfile)
                })
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

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        newFragment?.let { transaction?.replace(R.id.fl_frag_my_act_main_content_layout, it) }
        transaction?.commit()
    }

    private fun getFragment(idx: Int): androidx.fragment.app.Fragment? {
        var newFragment: androidx.fragment.app.Fragment? = null

        when(idx) {
            fragTalk -> newFragment = MyActMenu01Fragment()

//            fragNewMember -> newFragment = NewMemeberSearchFragment()

            fragReading -> newFragment = MyActMenu03Fragment()

            fragProfile -> newFragment = MyActMenu04Fragment()
        }

        return newFragment
    }

}
