package com.dmonster.darling.honey.item.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.item.presenter.ItemMainContract
import com.dmonster.darling.honey.item.presenter.ItemMainPresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_item_main.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class ItemMainFragment: BaseFragment(), ItemMainContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var itemPresenter: ItemMainContract.Presenter

    private var newFragment: androidx.fragment.app.Fragment? = null

    private val fragAdmin = 0
    private val fragCharge = 1
    private var arrMenus: ArrayList<Button>? = null
    private var id: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
        setEventBusListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()

        arrMenus = ArrayList()
        arrMenus?.add(btn_frag_item_main_admin)
        arrMenus?.add(btn_frag_item_main_charge)

        val bundle = arguments?.getString(AppKeyValue.instance.itemBundleCoin)
        if(bundle == AppKeyValue.instance.itemBundleCoin) {
            setPress(fragCharge)
            fragmentReplace(fragCharge)
        }
        else {
            setPress(fragAdmin)
            fragmentReplace(fragAdmin)
        }

        itemPresenter = ItemMainPresenter()
        itemPresenter.attachView(this)

        id = context?.let { Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
        itemPresenter.getCoin(id)
    }

    private fun setListener() {
        /*    마켓으로 이동    */
        disposeBag.add(RxView.clicks(iv_frag_item_main_market)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    //임시로 마켓 못가도록 막는 코드.
                    context?.let { it1 ->
                        Utility.instance.showAlert(
                            it1,
                            R.string.app_name,
                            R.string.msg_popup_notWorkingYet,
                            DialogInterface.OnClickListener { dialog, which ->

                            })
                    }
//                    val intent = Intent(context, MarketActivity::class.java)
//                    startActivity(intent)
                })

        /*    아이템 관리    */
        disposeBag.add(RxView.clicks(btn_frag_item_main_admin)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(newFragment !is ItemMenu01Fragment) {
                        setPress(fragAdmin)
                        fragmentReplace(fragAdmin)
                    }
                })

        /*    충전하기    */
        disposeBag.add(RxView.clicks(btn_frag_item_main_charge)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(newFragment !is ItemMenu02Fragment) {
                        setPress(fragCharge)
                        fragmentReplace(fragCharge)
                    }
                })
    }

    private fun setEventBusListener() {
        EventBus.subjectCoin.subscribe {
            if(it == AppKeyValue.instance.eventBusCoin) {
                itemPresenter.getCoin(id)
            }
        }.addTo(disposeBag)

        EventBus.subjectCoinCharge.subscribe {
            if(it == AppKeyValue.instance.eventBusCoinCharge) {
                setPress(fragCharge)
                fragmentReplace(fragCharge)
            }
        }.addTo(disposeBag)
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
        newFragment = getFragment(reqNewFragmentIndex)

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        newFragment?.let { transaction?.replace(R.id.fl_frag_item_main_content_layout, it) }
        transaction?.commit()
    }

    private fun getFragment(idx: Int): androidx.fragment.app.Fragment? {
        var newFragment: androidx.fragment.app.Fragment? = null

        when(idx) {
            fragAdmin -> newFragment = ItemMenu01Fragment()

            fragCharge -> newFragment = ItemMenu02Fragment()
        }

        return newFragment
    }

    /*    현재 보유코인    */
    override fun setCoinComplete(nowCoin: String?, saveCoin: String?) {
        context?.let {
            val gender = Utility.instance.UserData().getUserGender()
            if(gender == "F") {
                ll_frag_item_main_save_coin.visibility = View.VISIBLE
            }
            tv_frag_item_main_now_coin.text = String.format(it.resources.getString(R.string.item_now_coin), nowCoin)
            tv_frag_item_main_save_coin.text = String.format(it.resources.getString(R.string.item_now_coin), saveCoin)
        }
    }

    /*    현재 보유코인 호출실패    */
    override fun setCoinFailed(error: String?) {
        context?.let { Utility.instance.showToast(it, error) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        itemPresenter.detachView()
    }

}
