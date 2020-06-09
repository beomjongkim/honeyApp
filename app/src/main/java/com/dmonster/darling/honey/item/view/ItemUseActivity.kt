package com.dmonster.darling.honey.item.view

import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.item.data.ItemUseData
import com.dmonster.darling.honey.item.presenter.ItemUseContract
import com.dmonster.darling.honey.item.presenter.ItemUsePresenter
import com.dmonster.darling.honey.item.view.adapter.ItemAdapter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_item_use.*
import kotlinx.android.synthetic.main.activity_market.*

class ItemUseActivity : BaseActivity(), ItemUseContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: ItemUseContract.Presenter
    private lateinit var mAdapter: ItemAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_use)

        init()
        setListener()
    }

    private fun init() {
        ctb_act_item_use_toolbar.setTitle(resources.getString(R.string.item_use_title))
        disposeBag = CompositeDisposable()
        mAdapter = ItemAdapter(AppKeyValue.instance.itemTypeMore)
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv_act_item_use_list?.apply {
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

        ll_act_item_use_progress.visibility = View.VISIBLE

        mPresenter = ItemUsePresenter()
        mPresenter.attachView(this)

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        mPresenter.getItemUseList(false, id)
    }

    private fun setListener() {
        /*    새로고침    */
        srl_act_item_use_layout.setOnRefreshListener {
            mPresenter.getItemUseList(false, id)
        }

        rv_act_item_use_list.addOnScrollListener(object: androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                srl_act_item_use_layout.isEnabled = cl_act_item_use_layout.getChildAt(0).top == 0
            }

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastCompletelyVisibleItemPosition().plus(1)
                val itemTotalCount = recyclerView.adapter?.itemCount
                if(lastVisibleItemPosition == itemTotalCount && itemTotalCount >= (AppKeyValue.instance.listLimitCnt.toInt())) {

                }
            }
        })
    }

    /*    아이템 사용내역    */
    override fun setItemUseListComplete(isScroll: Boolean, data: ArrayList<ItemUseData>) {
        rv_act_item_use_list.visibility = View.VISIBLE
        tv_act_item_use_content.visibility = View.GONE
        srl_act_item_use_layout.isRefreshing = false

        if(!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_act_item_use_progress.visibility = View.GONE
    }

    /*    아이템 사용내역 호출실패    */
    override fun setItemUseListFailed(error: String?) {
        if(mAdapter.data.size == 0) {
            rv_act_item_use_list.visibility = View.GONE
            tv_act_item_use_content.visibility = View.VISIBLE
            ll_act_item_use_progress.visibility = View.GONE
            srl_act_item_use_layout.isRefreshing = false

            /*Utility.instance.showToast(this, error)*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
