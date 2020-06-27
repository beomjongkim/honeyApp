package com.dmonster.darling.honey.block.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.block.data.BlockData
import com.dmonster.darling.honey.block.presenter.BlockContract
import com.dmonster.darling.honey.block.presenter.BlockPresenter
import com.dmonster.darling.honey.block.view.adapter.BlockAdapter
import com.dmonster.darling.honey.databinding.ActivityAlarmBinding
import com.dmonster.darling.honey.databinding.ActivityBlockBinding
import com.dmonster.darling.honey.profile.view.ProfileActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_block.*
import java.util.concurrent.TimeUnit

class BlockActivity : BaseActivity(), BlockContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: BlockContract.Presenter
    private lateinit var mAdapter: BlockAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_block)
        val binding : ActivityBlockBinding = DataBindingUtil.setContentView(this,R.layout.activity_block)
        binding.bannerVM =
            BannerVM(Utility.instance.getPref(this, AppKeyValue.instance.savePrefID), lifecycle)
        binding.lifecycleOwner = this
        init()
        setListener()
    }

    private fun init() {
        ctb_act_block_toolbar.setTitle(resources.getString(R.string.main_more_menu_block))
        disposeBag = CompositeDisposable()
        mAdapter = BlockAdapter()
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv_act_block_list?.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = mAdapter

            addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: androidx.recyclerview.widget.RecyclerView,
                    state: androidx.recyclerview.widget.RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    view.layoutParams.width = -1
                }
            })
        }

        ll_act_block_progress.visibility = View.VISIBLE

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        mPresenter = BlockPresenter()
        mPresenter.attachView(this)
        mPresenter.getBlockList(false, id, AppKeyValue.instance.listLimitCnt)
    }

    private fun setListener() {
        /*    새로고침    */
        srl_act_block_layout.setOnRefreshListener {
            mPresenter.getBlockList(false, id, AppKeyValue.instance.listLimitCnt)
        }

        rv_act_block_list.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                srl_act_block_layout.isEnabled = cl_act_block_layout.getChildAt(0).top == 0
            }

            override fun onScrolled(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                dx: Int,
                dy: Int
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                        .plus(1)
                val itemTotalCount = recyclerView.adapter?.itemCount
                if (lastVisibleItemPosition == itemTotalCount && itemTotalCount >= (AppKeyValue.instance.listLimitCnt.toInt())) {
                    mPresenter.getBlockList(true, id, AppKeyValue.instance.listLimitCnt)
                }
            }
        })

        cb_act_block_edit.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
                mAdapter.setAllChecked(true)
            else
                mAdapter.setAllChecked(false)
        }

        /*    편집하기    */
        disposeBag.add(RxView.clicks(tv_act_block_edit)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (mAdapter.data.size > 0) {
                    mAdapter.checkArray?.size?.let { it1 ->
                        if (it1 > 0) {
                            Utility.instance.showTwoButtonAlert(
                                this,
                                resources.getString(R.string.app_name),
                                resources.getString(R.string.msg_block_clear),
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (which == DialogInterface.BUTTON_POSITIVE) {
                                        ll_act_block_progress.visibility = View.VISIBLE

                                        mAdapter.checkArray?.indices?.let { it2 ->
                                            for (i in it2) {
                                                val selectItem = mAdapter.checkArray?.get(i)
                                                val mbNo =
                                                    selectItem?.let { mAdapter.data[it].mbNo }
                                                mPresenter.setBlockClear(
                                                    id,
                                                    mbNo,
                                                    AppKeyValue.instance.blockUnblock
                                                )
                                            }
                                        }
                                    }
                                })
                        } else {
                            Utility.instance.showToast(
                                this,
                                resources.getString(R.string.msg_block_select)
                            )
                        }
                    }
                }
            })

        /*    전체선택    */
        disposeBag.add(RxView.clicks(cb_act_block_select)
            //.throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (cb_act_block_select.isChecked) {
                    mAdapter.setAllChecked(true)
                } else {
                    mAdapter.setAllChecked(false)
                }
            })

        /*    프로필    */
        /*blockAdapter.itemClick = itemClickListener()*/
    }

    private fun itemClickListener() = View.OnClickListener {
        val position = it.tag.toString().toInt()
        val mbNo = mAdapter.data[position].mbNo

        val intent = Intent(this, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppKeyValue.instance.profileMbNo, mbNo)
        startActivity(intent)
    }

    /*    차단회원목록    */
    override fun setBlockList(isScroll: Boolean, data: List<BlockData>) {
        rv_act_block_list.visibility = View.VISIBLE
        tv_act_block_content.visibility = View.GONE
        srl_act_block_layout.isRefreshing = false

        if (!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_act_block_progress.visibility = View.GONE
    }

    /*    차단회원목록 호출실패    */
    override fun setBlockListFailed(error: String?) {
        if (mAdapter.data.size == 0) {
            rv_act_block_list.visibility = View.GONE
            tv_act_block_content.visibility = View.VISIBLE
            ll_act_block_progress.visibility = View.GONE
            srl_act_block_layout.isRefreshing = false

            /*Utility.instance.showToast(this, error)*/
        }
    }

    /*    차단회원 차단해제    */
    override fun setBlockClearComplete(message: String?) {
        mAdapter.removeItem()
        ll_act_block_progress.visibility = View.GONE
        Utility.instance.showToast(this, message)
    }

    /*    차단회원 차단해제 호출실패    */
    override fun setBlockClearFailed(error: String?) {
        ll_act_block_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
