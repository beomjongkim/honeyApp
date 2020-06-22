package com.dmonster.darling.honey.dialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.TextView
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.point.data.CoinData
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_item_talk.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class ItemTalkDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable

    private lateinit var layoutArrayId: Array<Int>
    private lateinit var priceArrayId: Array<Int>
    private lateinit var layoutArraySelect: Array<RelativeLayout?>

    private var layoutArray: ArrayList<RelativeLayout>? = null
    private var titleArray: ArrayList<TextView>? = null
    private var priceArray: ArrayList<TextView>? = null

    private var item01Count: Int = 1
    private var item02Count: Int = 0
    private var item03Count: Int = 0
    private var item04Count: Int = 0
    private var item05Count: Int = 0
    private var item01Price: Int = 5900
    private var item02Price: Int = 19200
    private var item03Price: Int = 36500
    private var item04Price: Int = 45900
    private var item05Price: Int = 79000

    private var id: String? = null
    private var itemId: String? = null
    private var itemCoin: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        isCancelable = false
        return inflater.inflate(R.layout.dialog_item_talk, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()

        layoutArrayId = arrayOf(R.id.rl_dlg_item_talk_item01, R.id.rl_dlg_item_talk_item02, R.id.rl_dlg_item_talk_item03, R.id.rl_dlg_item_talk_item04, R.id.rl_dlg_item_talk_item05)
        priceArrayId = arrayOf(R.id.tv_dlg_item_talk_price01, R.id.tv_dlg_item_talk_price02, R.id.tv_dlg_item_talk_price03, R.id.tv_dlg_item_talk_price04, R.id.tv_dlg_item_talk_price05)

        layoutArray = ArrayList()
        layoutArray?.add(rl_dlg_item_talk_item01)
        layoutArray?.add(rl_dlg_item_talk_item02)
        layoutArray?.add(rl_dlg_item_talk_item03)
        layoutArray?.add(rl_dlg_item_talk_item04)
        layoutArray?.add(rl_dlg_item_talk_item05)

        titleArray = ArrayList()
        titleArray?.add(tv_dlg_item_talk_title01)
        titleArray?.add(tv_dlg_item_talk_title02)
        titleArray?.add(tv_dlg_item_talk_title03)
        titleArray?.add(tv_dlg_item_talk_title04)
        titleArray?.add(tv_dlg_item_talk_title05)

        priceArray = ArrayList()
        priceArray?.add(tv_dlg_item_talk_price01)
        priceArray?.add(tv_dlg_item_talk_price02)
        priceArray?.add(tv_dlg_item_talk_price03)
        priceArray?.add(tv_dlg_item_talk_price04)
        priceArray?.add(tv_dlg_item_talk_price05)

        tv_dlg_item_talk_coin.text = context?.let { String.format(it.resources.getString(R.string.won), item01Price) }

        id = context?.let { Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
        itemId = AppKeyValue.instance.itemIdTalk
        itemCoin = AppKeyValue.instance.itemCoinTalk

        setNowCoin(id)
    }

    private fun setListener() {
        layoutArraySelect = arrayOfNulls(5)
        for(i in layoutArraySelect.indices) {
            layoutArraySelect[i] = dialog?.findViewById<RelativeLayout>(layoutArrayId[i])

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

        /*    1일권 구매수량 Down    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item01_count_down)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item01Count > 0) {
                        item01Count = item01Count.minus(1)
                        tv_dlg_item_talk_item01_count.text = item01Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    1일권 구매수량 Up    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item01_count_up)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item01Count < 99) {
                        item01Count = item01Count.plus(1)
                        tv_dlg_item_talk_item01_count.text = item01Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    5일권 구매수량 Down    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item02_count_down)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item02Count > 0) {
                        item02Count = item02Count.minus(1)
                        tv_dlg_item_talk_item02_count.text = item02Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    5일권 구매수량 Up    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item02_count_up)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item02Count < 99) {
                        item02Count = item02Count.plus(1)
                        tv_dlg_item_talk_item02_count.text = item02Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    10일권 구매수량 Down    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item03_count_down)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item03Count > 0) {
                        item03Count = item03Count.minus(1)
                        tv_dlg_item_talk_item03_count.text = item03Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    10일권 구매수량 Up    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item03_count_up)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item03Count < 99) {
                        item03Count = item03Count.plus(1)
                        tv_dlg_item_talk_item03_count.text = item03Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    15일권 구매수량 Down    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item04_count_down)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item04Count > 0) {
                        item04Count = item04Count.minus(1)
                        tv_dlg_item_talk_item04_count.text = item04Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    15일권 구매수량 Up    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item04_count_up)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item04Count < 99) {
                        item04Count = item04Count.plus(1)
                        tv_dlg_item_talk_item04_count.text = item04Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    30일권 구매수량 Down    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item05_count_down)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item05Count > 0) {
                        item05Count = item05Count.minus(1)
                        tv_dlg_item_talk_item05_count.text = item05Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    30일권 구매수량 Up    */
        disposeBag.add(RxView.clicks(iv_dlg_item_talk_item05_count_up)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(item05Count < 99) {
                        item05Count = item05Count.plus(1)
                        tv_dlg_item_talk_item05_count.text = item05Count.toString()
                        tv_dlg_item_talk_coin.text = context?.let { it1 -> String.format(it1.resources.getString(R.string.won), coinSum()) }
                    }
                })

        /*    취소    */
        disposeBag.add(RxView.clicks(tv_dlg_item_talk_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(tv_dlg_item_talk_enter)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dialog?.context?.let { it1 ->
                        when(tv_dlg_item_talk_enter.text.toString()) {
                            it1.resources.getString(R.string.popup_profile_enter) -> {
                                Utility.instance.showTwoButtonAlert(it1, it1.resources.getString(R.string.app_name), it1.resources.getString(R.string.msg_popup_buy), DialogInterface.OnClickListener { dialog, which ->
                                    if(which == DialogInterface.BUTTON_POSITIVE) {
                                        val count = String.format(it1.resources.getString(R.string.popup_talk_coin), item01Count, item02Count, item03Count, item04Count, item05Count)
                                        setItemTalk(id, itemId, itemCoin, count)
                                    }
                                })
                            }

                            it1.resources.getString(R.string.popup_profile_charge) -> {
                                EventBus.sendEventCoinCharge(AppKeyValue.instance.eventBusCoinCharge)
                                dismiss()
                            }
                        }
                    }
                })
    }

    private fun setPress(position: Int) {
        layoutArray?.let { it ->
            it.map {
                it.isSelected = false
            }
            it[position].isSelected = true
        }

        titleArray?.let { it ->
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

        val nowCoin = tv_dlg_item_talk_now_coin.text.toString()
        val reNowCoin = nowCoin.replace(resources.getString(R.string.popup_item_replace_coin), "").replace(resources.getString(R.string.popup_item_replace_comma), "").toInt()
        var lackCoin = 0

        when(position) {
            0 -> {
                item01Count = 1
                item02Count = 0
                item03Count = 0
                item04Count = 0
                item05Count = 0
                lackCoin = reNowCoin - 5900
            }

            1 -> {
                item01Count = 0
                item02Count = 1
                item03Count = 0
                item04Count = 0
                item05Count = 0
                lackCoin = reNowCoin - 19200
            }

            2 -> {
                item01Count = 0
                item02Count = 0
                item03Count = 1
                item04Count = 0
                item05Count = 0
                lackCoin = reNowCoin - 36500
            }

            3 -> {
                item01Count = 0
                item02Count = 0
                item03Count = 0
                item04Count = 1
                item05Count = 0
                lackCoin = reNowCoin - 45900
            }

            4 -> {
                item01Count = 0
                item02Count = 0
                item03Count = 0
                item04Count = 0
                item05Count = 1
                lackCoin = reNowCoin - 79000
            }
        }

        dialog?.context?.let {
            if(lackCoin < 0) {
                val reLackCoin = Utility.instance.priceFormat(Math.abs(lackCoin).toString())
                tv_dlg_item_talk_lack_coin.text = String.format(it.resources.getString(R.string.popup_item_sum_coin), reLackCoin)

                tv_dlg_item_talk_lack_coin_title.text = it.resources.getString(R.string.popup_item_lack_coin)
                tv_dlg_item_talk_enter.text = it.resources.getString(R.string.popup_profile_charge)
            }
            else {
                val afterCoin = Utility.instance.priceFormat(Math.abs(lackCoin).toString())
                tv_dlg_item_talk_lack_coin.text = String.format(it.resources.getString(R.string.popup_item_sum_coin), afterCoin)

                tv_dlg_item_talk_lack_coin_title.text = it.resources.getString(R.string.popup_item_after_coin)
                tv_dlg_item_talk_enter.text = it.resources.getString(R.string.popup_profile_enter)
            }
        }
    }

    private fun coinSum(): Int {
        val item01Coin = item01Price * item01Count
        val item02Coin = item02Price * item02Count
        val item03Coin = item03Price * item03Count
        val item04Coin = item04Price * item04Count
        val item05Coin = item05Price * item05Count

        return item01Coin.plus(item02Coin).plus(item03Coin).plus(item04Coin).plus(item05Coin)
    }

    /*    현재 보유코인    */
    private fun setNowCoin(id: String?) {
        val subscriber = object: DisposableObserver<ResultItem<CoinData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                dialog?.context?.let { Utility.instance.showToast(it, e.message) }
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<CoinData>) {
                item.let { it ->
                    dialog?.context?.let { it1 ->
                        if(it.isSuccess) {
                            val nowCoin = String.format(it1.resources.getString(R.string.popup_item_sum_coin), it.item?.cntCoin)
                            tv_dlg_item_talk_now_coin.text = nowCoin
                            setPress(0)
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestPossessionCoin(ServerApi.instance.possessionCoinMethod, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    /*    톡하기이용권 아이템 구매    */
    private fun setItemTalk(id: String?, itemId: String?, coin: String?, count: String?) {
        val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                dialog?.context?.let { Utility.instance.showToast(it, e.message) }
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<BaseItem>) {
                item.let { it ->
                    dialog?.context?.let { it1 ->
                        if(it.isSuccess) {
                            Utility.instance.showAlert(it1, it1.resources.getString(R.string.app_name), it1.resources.getString(R.string.msg_popup_buy_complete), DialogInterface.OnClickListener { dialog, which ->
                                EventBus.sendEventCoin(AppKeyValue.instance.eventBusCoin)
                                dismiss()
                            })
                        }
                        else {
                            Utility.instance.showToast(it1, it.message)
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestBuyItem(ServerApi.instance.buyItemMethod, id, itemId, coin, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
    }

}
