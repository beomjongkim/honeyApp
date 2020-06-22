package com.dmonster.darling.honey.point.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.point.presenter.CoinChargeContract
import com.dmonster.darling.honey.point.presenter.CoinChargePresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_item_charge.*
import java.util.*
import java.util.concurrent.TimeUnit

class ItemMenu02Fragment: BaseFragment(), CoinChargeContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: CoinChargeContract.Presenter

    private lateinit var layoutArrayId: Array<Int>
    private lateinit var coinArrayId: Array<Int>
    private lateinit var priceArrayId: Array<Int>
    private lateinit var layoutArraySelect: Array<RelativeLayout?>

    private var layoutArray: ArrayList<RelativeLayout>? = null
    private var coinArray: ArrayList<TextView>? = null
    private var priceArray: ArrayList<TextView>? = null
    private var id: String? = null
    private var gender: String? = null
    private var productId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_charge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
        setEventBusListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()

        layoutArrayId = arrayOf(R.id.rl_frag_item_charge_coin01, R.id.rl_frag_item_charge_coin02, R.id.rl_frag_item_charge_coin03,
                R.id.rl_frag_item_charge_coin04, R.id.rl_frag_item_charge_coin05, R.id.rl_frag_item_charge_coin06)
        coinArrayId = arrayOf(R.id.tv_frag_item_charge_coin01, R.id.tv_frag_item_charge_coin02, R.id.tv_frag_item_charge_coin03,
                R.id.tv_frag_item_charge_coin04, R.id.tv_frag_item_charge_coin05, R.id.tv_frag_item_charge_coin06)
        priceArrayId = arrayOf(R.id.tv_frag_item_charge_price01, R.id.tv_frag_item_charge_price02, R.id.tv_frag_item_charge_price03,
                R.id.tv_frag_item_charge_price04, R.id.tv_frag_item_charge_price05, R.id.tv_frag_item_charge_price06)

        layoutArray = ArrayList()
        layoutArray?.add(rl_frag_item_charge_coin01)
        layoutArray?.add(rl_frag_item_charge_coin02)
        layoutArray?.add(rl_frag_item_charge_coin03)
        layoutArray?.add(rl_frag_item_charge_coin04)
        layoutArray?.add(rl_frag_item_charge_coin05)
        layoutArray?.add(rl_frag_item_charge_coin06)

        coinArray = ArrayList()
        coinArray?.add(tv_frag_item_charge_coin01)
        coinArray?.add(tv_frag_item_charge_coin02)
        coinArray?.add(tv_frag_item_charge_coin03)
        coinArray?.add(tv_frag_item_charge_coin04)
        coinArray?.add(tv_frag_item_charge_coin05)
        coinArray?.add(tv_frag_item_charge_coin06)

        priceArray = ArrayList()
        priceArray?.add(tv_frag_item_charge_price01)
        priceArray?.add(tv_frag_item_charge_price02)
        priceArray?.add(tv_frag_item_charge_price03)
        priceArray?.add(tv_frag_item_charge_price04)
        priceArray?.add(tv_frag_item_charge_price05)
        priceArray?.add(tv_frag_item_charge_price06)

        setPress(0)

        context?.let { id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
        gender = Utility.instance.UserData().getUserGender()
        productId = AppKeyValue.instance.inAppItem01

        when(gender) {
            "F" -> {
                rl_frag_item_charge_coin05.visibility = View.GONE
                rl_frag_item_charge_coin06.visibility = View.GONE
            }
        }

        mPresenter = CoinChargePresenter()
        mPresenter.attachView(this)
    }

    private fun setListener() {
        layoutArraySelect = arrayOfNulls(6)
        for(i in layoutArraySelect.indices) {
            layoutArraySelect[i] = activity?.findViewById<RelativeLayout>(layoutArrayId[i])

            layoutArraySelect[i]?.let { it ->
                RxView.clicks(it)
                        .throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe {
                            setPress(i)
                        }
            }?.let {
                disposeBag.add(it)
            }
        }

        disposeBag.add(RxView.clicks(btn_frag_item_charge_payment)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                when {
                    rl_frag_item_charge_coin01.isSelected -> productId = AppKeyValue.instance.inAppItem01
                    rl_frag_item_charge_coin02.isSelected -> productId = AppKeyValue.instance.inAppItem02
                    rl_frag_item_charge_coin03.isSelected -> productId = AppKeyValue.instance.inAppItem03
                    rl_frag_item_charge_coin04.isSelected -> productId = AppKeyValue.instance.inAppItem04
                    rl_frag_item_charge_coin05.isSelected -> productId = AppKeyValue.instance.inAppItem05
                    rl_frag_item_charge_coin06.isSelected -> productId = AppKeyValue.instance.inAppItem06
                }
                productId?.let { it1 -> EventBus.sendEventProductId(it1) }
            })
    }

    private fun setEventBusListener() {
        EventBus.subjectChargeCoin.subscribe {
            val coin = it.coin
            val orderId = it.orderId
            val productId = it.productId

            ll_frag_item_charge_progress.visibility = View.VISIBLE
            mPresenter.setCoinCharge(id, context?.resources?.getString(R.string.item_charge_case), coin, orderId, productId)
        }.addTo(disposeBag)
    }

    private fun setPress(position: Int) {
        layoutArray?.let { it ->
            it.map {
                it.isSelected = false
            }
            it[position].isSelected = true
        }

        coinArray?.let { it ->
            it.map {
                it.isSelected = false
            }
            it[position].isSelected = true
        }

        priceArray?.let { it ->
            it.map {
                it.isSelected = false
            }
            it[position].isSelected = true
        }
    }

    /*    코인충전    */
    override fun setCoinChargeComplete(message: String?) {
        ll_frag_item_charge_progress.visibility = View.GONE
        context?.let { it ->
            message?.let { it1 ->
                Utility.instance.showAlert(it, it.resources.getString(R.string.app_name), it1, DialogInterface.OnClickListener { dialog, which ->
                    EventBus.sendEventCoin(AppKeyValue.instance.eventBusCoin)
                })
            }
        }
    }

    /*    코인충전 호출실패    */
    override fun setCoinChargeFailed(error: String?) {
        ll_frag_item_charge_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
