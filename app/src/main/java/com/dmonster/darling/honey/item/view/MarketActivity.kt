package com.dmonster.darling.honey.item.view

import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.item.data.MarketListData
import com.dmonster.darling.honey.item.presenter.MarketListContract
import com.dmonster.darling.honey.item.presenter.MarketListPresenter
import com.dmonster.darling.honey.item.view.adapter.MarketAdapter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.Utility
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_market.*

class MarketActivity : BaseActivity(), MarketListContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: MarketListContract.Presenter
    private lateinit var mAdapter: MarketAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null
    private var otherId: String? = null
    private var giftMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        init()
        setListener()
    }

    private fun init() {
        ctb_act_market_toolbar.setTitle(resources.getString(R.string.market_title))
        disposeBag = CompositeDisposable()
        mAdapter = MarketAdapter()
        viewLayoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        rv_act_market_list?.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = mAdapter

            addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    view.layoutParams.width = -1
                }
            })
        }

        ll_act_market_progress.visibility = View.VISIBLE

        otherId = intent.getStringExtra(AppKeyValue.instance.marketOtherId)
        if(!TextUtils.isEmpty(otherId)) {
            giftMode = true
        }

        mPresenter = MarketListPresenter()
        mPresenter.attachView(this)

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        mPresenter.getMarketList(false, id)
        mPresenter.getCoin(id)

        EventBus.subjectCoin.subscribe {
            if(it == AppKeyValue.instance.eventBusCoin) {
                mPresenter.getCoin(id)
            }
        }.addTo(disposeBag)
    }

    private fun setListener() {
        /*    새로고침    */
        srl_act_market_layout.setOnRefreshListener {
            mPresenter.getMarketList(false, id)
        }

        rv_act_market_list.addOnScrollListener(object: androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                srl_act_market_layout.isEnabled = cl_act_market_layout.getChildAt(0).top == 0
            }

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastCompletelyVisibleItemPosition().plus(1)
                val itemTotalCount = recyclerView.adapter?.itemCount
                if(lastVisibleItemPosition == itemTotalCount && itemTotalCount >= (AppKeyValue.instance.listLimitCnt.toInt())) {

                }
            }
        })

        /*    마켓아이템 구매    */
        mAdapter.itemClick = itemClickListener()
    }

    private fun itemClickListener() = View.OnClickListener {
        val position = it.tag.toString().toInt()
        val productId = mAdapter.data[position].idx

        if(giftMode) {
            Utility.instance.showTwoButtonAlert(this, resources.getString(R.string.app_name), resources.getString(R.string.msg_market_gift), DialogInterface.OnClickListener { dialog, which ->
                if(which == DialogInterface.BUTTON_POSITIVE) {
                    ll_act_market_progress.visibility = View.VISIBLE
                    mPresenter.setMarketItemBuy(id, productId, "1", resources.getString(R.string.market_key_gift), otherId)
                }
            })
        }
        else {
            Utility.instance.showTwoButtonAlert(this, resources.getString(R.string.app_name), resources.getString(R.string.msg_market_item), DialogInterface.OnClickListener { dialog, which ->
                if(which == DialogInterface.BUTTON_POSITIVE) {
                    ll_act_market_progress.visibility = View.VISIBLE
                    mPresenter.setMarketItemBuy(id, productId, "1", resources.getString(R.string.market_key_item), "")
                }
            })
        }
    }

    /*    현재 보유코인    */
    override fun setCoinComplete(coin: String?) {
        tv_act_market_now_coin.text = String.format(resources.getString(R.string.item_now_coin), coin)
    }

    /*    현재 보유코인 호출실패    */
    override fun setCoinFailed(error: String?) {
        Utility.instance.showToast(this, error)
    }

    /*    마켓 상품목록    */
    override fun setMarketListComplete(isScroll: Boolean, data: ArrayList<MarketListData>) {
        rv_act_market_list.visibility = View.VISIBLE
        tv_act_market_content.visibility = View.GONE
        srl_act_market_layout.isRefreshing = false

        if(!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_act_market_progress.visibility = View.GONE
    }

    /*    마켓 상품목록 호출실패    */
    override fun setMarketListFailed(error: String?) {
        if(mAdapter.data.size == 0) {
            rv_act_market_list.visibility = View.GONE
            tv_act_market_content.visibility = View.VISIBLE
            ll_act_market_progress.visibility = View.GONE
            srl_act_market_layout.isRefreshing = false

            /*Utility.instance.showToast(this, error)*/
        }
    }

    /*    마켓 상품구매    */
    override fun setMarketItemBuyComplete() {
        ll_act_market_progress.visibility = View.GONE
        Utility.instance.showAlert(this, resources.getString(R.string.app_name), resources.getString(R.string.msg_market_complete), DialogInterface.OnClickListener { dialog, which ->
            EventBus.sendEventCoin(AppKeyValue.instance.eventBusCoin)
        })
    }

    /*    마켓 상품구매 호출실패    */
    override fun setMarketItemBuyFailed(error: String?) {
        ll_act_market_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
