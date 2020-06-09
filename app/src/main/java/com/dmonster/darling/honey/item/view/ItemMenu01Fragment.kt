package com.dmonster.darling.honey.item.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.dialog.*
import com.dmonster.darling.honey.item.data.ItemData
import com.dmonster.darling.honey.item.data.ItemUseData
import com.dmonster.darling.honey.item.presenter.ItemAdminContract
import com.dmonster.darling.honey.item.presenter.ItemAdminPresenter
import com.dmonster.darling.honey.item.view.adapter.ItemAdapter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_item_admin.*
import java.util.concurrent.TimeUnit

class ItemMenu01Fragment: BaseFragment(), ItemAdminContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: ItemAdminContract.Presenter
    private lateinit var mAdapter: ItemAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null
    private var gender: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_item_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
        setEventBusListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mAdapter = ItemAdapter(AppKeyValue.instance.itemTypeAdmin)
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_frag_item_admin_use_list?.apply {
            setHasFixedSize(true)
            layoutManager = viewLayoutManager
            adapter = mAdapter

            addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    view.layoutParams.width = -1
                }
            })

            isNestedScrollingEnabled = false
        }

        mPresenter = ItemAdminPresenter()
        mPresenter.attachView(this)

        ll_frag_item_admin_progress.visibility = View.VISIBLE

        context?.let { id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
        gender = Utility.instance.UserData().getUserGender()

        when(gender) {
            "F" -> {
                rl_frag_item_admin_profile.visibility = View.GONE
                rl_frag_item_admin_good.visibility = View.GONE
                rl_frag_item_admin_talk.visibility = View.GONE
            }
        }

        mPresenter.getItemList(id)
        mPresenter.getItemUseList(id)
    }

    private fun setListener() {
        /*    프로필 이용권 구매하기    */
        disposeBag.add(RxView.clicks(tv_frag_item_admin_profile_buy)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val profileDialog = ItemProfileDialog()
                    activity?.supportFragmentManager?.let { it1 -> profileDialog.show(it1, AppKeyValue.instance.tagItemProfileDlg) }
                })

        /*    호감있어요 구매하기    */
        disposeBag.add(RxView.clicks(tv_frag_item_admin_good_buy)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val goodDialog = ItemGoodDialog()
                    activity?.supportFragmentManager?.let { it1 -> goodDialog.show(it1, AppKeyValue.instance.tagItemGoodDlg) }
                })

        /*    톡하기 이용권 구매하기    */
        disposeBag.add(RxView.clicks(tv_frag_item_admin_talk_buy)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val talkDialog = ItemTalkDialog()
                    activity?.supportFragmentManager?.let { it1 -> talkDialog.show(it1, AppKeyValue.instance.tagItemTalkDlg) }
                })

        /*    러브카드 구매하기    */
        disposeBag.add(RxView.clicks(tv_frag_item_admin_love_buy)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val loveDialog = ItemLoveDialog()
                    activity?.supportFragmentManager?.let { it1 -> loveDialog.show(it1, AppKeyValue.instance.tagItemLoveDlg) }
                })

        /*    자동갱신 구매하기    */
        disposeBag.add(RxView.clicks(tv_frag_item_admin_refresh_buy)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val refreshDialog = ItemRefreshDialog()
                    activity?.supportFragmentManager?.let { it1 -> refreshDialog.show(it1, AppKeyValue.instance.tagItemRefreshDlg) }
                })

        /*    이용내역 더보기    */
        disposeBag.add(RxView.clicks(tv_frag_item_admin_use_more)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    val intent = Intent(context, ItemUseActivity::class.java)
                    startActivity(intent)
                })
    }

    private fun setEventBusListener() {
        EventBus.subjectCoin.subscribe {
            if(it == AppKeyValue.instance.eventBusCoin) {
                mPresenter.getItemList(id)
                /*itemPresenter.getItemUseList(id)*/
            }
        }.addTo(disposeBag)
    }

    /*    보유아이템 목록    */
    override fun setItemListComplete(data: ArrayList<ItemData>) {
        val profile = data[1].itLimitCnt
        val good = data[2].itLimitCnt
        val talk = data[0].itLimitCnt
        val talkTime = data[0].itTermHour
        val love = data[3].itLimitCnt
        val refresh = data[4].itLimitCnt

        context?.let {
            if(TextUtils.isEmpty(profile)) {
                tv_frag_item_admin_profile.text = it.resources.getString(R.string.item_admin_default01)
            }
            else {
                tv_frag_item_admin_profile.text = profile
            }

            if(TextUtils.isEmpty(good)) {
                tv_frag_item_admin_good.text = it.resources.getString(R.string.item_admin_default01)
            }
            else {
                tv_frag_item_admin_good.text = good
            }

            if(TextUtils.isEmpty(talk)) {
                tv_frag_item_admin_talk.text = it.resources.getString(R.string.item_admin_default02)
            }
            else {
                tv_frag_item_admin_talk.text = String.format(it.resources.getString(R.string.item_admin_talk_unit), talk, talkTime)
            }

            if(TextUtils.isEmpty(love)) {
                tv_frag_item_admin_love.text = it.resources.getString(R.string.item_admin_default02)
            }
            else {
                tv_frag_item_admin_love.text = love
            }

            if(TextUtils.isEmpty(refresh)) {
                tv_frag_item_admin_refresh.text = it.resources.getString(R.string.item_admin_default02)
            }
            else {
                tv_frag_item_admin_refresh.text = refresh
            }

            ll_frag_item_admin_progress.visibility = View.GONE
        }
    }

    /*    보유아이템 목록 호출실패    */
    override fun setItemListFailed(error: String?) {
        ll_frag_item_admin_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    아이템 사용내역    */
    override fun setItemUseListComplete(data: ArrayList<ItemUseData>) {
        mAdapter.data.clear()
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()
    }

    /*    아이템 사용내역 호출실패    */
    override fun setItemUseListFailed(error: String?) {
        if(mAdapter.data.size > 0) {
            context?.let { Utility.instance.showToast(it, error) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
