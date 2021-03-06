package com.dmonster.darling.honey.myactivity.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.RewardVM
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.dialog.ItemLoveDialog
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.myactivity.data.TalkListData
import com.dmonster.darling.honey.myactivity.presenter.TalkListContract
import com.dmonster.darling.honey.myactivity.presenter.TalkListPresenter
import com.dmonster.darling.honey.myactivity.view.adapter.TalkListAdapter
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_my_act_talk.*
import java.util.concurrent.TimeUnit

class MyTalkFragment: BaseFragment(), TalkListContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: TalkListContract.Presenter
    private lateinit var mAdapter: TalkListAdapter
    private lateinit var rewardVM : RewardVM

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null
    private var totalNotRead = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_act_talk, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
        setEventBusListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mAdapter = TalkListAdapter(context)
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_frag_my_act_talk_list?.apply {
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

        ll_frag_my_act_talk_progress.visibility = View.VISIBLE

        id = context?.let { Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
        mPresenter = TalkListPresenter()
        mPresenter.attachView(this)

        rewardVM = RewardVM(activity as Activity)
        rewardVM.adCallback = object : RewardedAdCallback() {

            override fun onRewardedAdOpened() {
                // Ad opened.
                Log.d("ProfileActivity", "Ad opened.")
            }

            override fun onRewardedAdClosed() {
                // Ad closed.
                Log.d("ProfileActivity", "Ad closed.")
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                ll_frag_my_act_talk_progress.visibility = View.VISIBLE
                // User earned reward.
                val subscriber = object : DisposableObserver<ResultItem<String>>() {
                    override fun onComplete() {
                        ll_frag_my_act_talk_progress.visibility = View.GONE
                    }

                    override fun onError(e: Throwable) {
                        ll_frag_my_act_talk_progress.visibility = View.GONE
                    }

                    override fun onNext(item: ResultItem<String>) {
                        item.let { it ->
                            if (it.isSuccess) {
                                context?.let { it1 -> Utility.instance.showToast(it1, "성공적으로 이용권을 구매하였습니다.") }
                            }
                        }
                        ll_frag_my_act_talk_progress.visibility = View.GONE
                    }
                }
                ItemModel().buyItem(id, 1, subscriber)
                Log.d("ProfileActivity", "User earned reward.")
            }

            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
                Log.d("ProfileActivity", "Ad failed to display.")
            }
        }
    }

    private fun setListener() {
        /*    새로고침    */
        srl_frag_my_act_talk_layout.setOnRefreshListener {
            mPresenter.getTalkList(false, id, AppKeyValue.instance.listStartCnt,AppKeyValue.instance.listLimitCnt)
        }

        rv_frag_my_act_talk_list.addOnScrollListener(object: androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                srl_frag_my_act_talk_layout.isEnabled = cl_frag_my_act_talk_layout.getChildAt(0).top == 0
            }

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastCompletelyVisibleItemPosition().plus(1)
                val itemTotalCount = recyclerView.adapter?.itemCount
                if(lastVisibleItemPosition == itemTotalCount && itemTotalCount >= (AppKeyValue.instance.listLimitCnt.toInt())) {
                    mPresenter.getTalkList(true, id, lastVisibleItemPosition.toString(), AppKeyValue.instance.listLimitCnt)
                }
            }
        })

        cb_frag_my_act_all_check.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter.setAllChecked(isChecked)
        }

        /*    선택삭제    */
        disposeBag.add(RxView.clicks(tv_frag_my_act_talk_edit)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(mAdapter.data.size > 0) {
                        context?.let { it1 ->
                            mAdapter.checkArray?.size?.let { it2 ->
                                if(it2 > 0) {
                                    Utility.instance.showTwoButtonAlert(it1, it1.resources.getString(R.string.app_name), it1.resources.getString(R.string.msg_my_activity_talk_delete), DialogInterface.OnClickListener { dialog, which ->
                                        if(which == DialogInterface.BUTTON_POSITIVE) {
                                            ll_frag_my_act_talk_progress.visibility = View.VISIBLE

                                            mAdapter.checkArray?.indices?.let { it3 ->
                                                for(i in it3) {
                                                    val selectItem = mAdapter.checkArray?.get(i)
                                                    val idx = selectItem?.let { it4 -> mAdapter.data[it4].idx }
                                                    mPresenter.setTalkDelete(id, idx)
                                                }
                                            }
                                        }
                                    })
                                }
                                else {
                                    Utility.instance.showToast(it1, it1.resources.getString(R.string.msg_my_activity_select_talk))
                                }
                            }
                        }
                    }
                })

        /*    전체선택    */
        disposeBag.add(RxView.clicks(cb_frag_my_act_talk_select)
                //.throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if(cb_frag_my_act_talk_select.isChecked) {
                        mAdapter.setAllChecked(true)
                    }
                    else {
                        mAdapter.setAllChecked(false)
                    }
                })

        /*    러브카드 목록    */
        disposeBag.add(RxView.clicks(iv_frag_my_act_talk_love)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    ll_frag_my_act_talk_progress.visibility = View.VISIBLE
                    mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdLove)
                })

        /*    톡하기    */
        mAdapter.itemClick = itemClickListener()
    }

    private fun setEventBusListener() {
        EventBus.subjectTalkDelete.subscribe {
            if(it == AppKeyValue.instance.eventBusTalkDelete) {
                mPresenter.getTalkList(false, id,AppKeyValue.instance.listStartCnt, AppKeyValue.instance.listLimitCnt)
            }
        }.addTo(disposeBag)
    }

    private fun itemClickListener() = View.OnClickListener {
        val pref = context?.getSharedPreferences("Pref", Context.MODE_PRIVATE)
        if (pref != null) {
            if(pref.getBoolean(AppKeyValue.instance.hasFreePass, false)){
                val position = it.tag.toString().toInt()
                mAdapter.data[position].apply {
                    val roomNo = idx
                    val mbNo = mbNo
                    val otherId = mbId
                    val talkId = mbNick
                    val area = mbAddr1
                    val age = mbAge
                    this.notRead = "0"

                    val intent = Intent(context, TalkActivity::class.java)
                    intent.putExtra(AppKeyValue.instance.talkRoomNo, roomNo)
                    intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                    intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                    intent.putExtra(AppKeyValue.instance.talkOtherTalkId, talkId)
                    /*    상단 타이틀정보    */
                    intent.putExtra(AppKeyValue.instance.talkTitleName, talkId)
                    intent.putExtra(AppKeyValue.instance.talkTitleArea, area)
                    intent.putExtra(AppKeyValue.instance.talkTitleAge, age)
                    context?.startActivity(intent)
                }
            }else{

             context?.let { it1 ->
                    CustomPopup(it1, "이용권 구매", "이용권을 구매해서 아래 기능을 마음껏 이용해보세요!\n" +getString(R.string.msg_freepass_description), R.drawable.ic_talk_vivid, object: CustomDialogInterface {
                        override fun onConfirm(v: View) {
                                rewardVM.rewardedAd.show(activity, rewardVM.adCallBackBase)

                        }

                        override fun onCancel(v: View) {
                            val intent = Intent(it1, MainActivity::class.java)
                            intent.putExtra(AppKeyValue.instance.goToMarket, true)
                            startActivity(intent)
                        }
                    })
                }.also {
                    it?.let {
                        it.popupVM.negativeText.value = "1개월 이용권\n구매하기"
                        it.popupVM.positiveText.value = "광고 시청 후\n이용권 받기"
                        it.show()
                    }
                }
            }
        }

    }

    /*    톡하기 목록    */
    override fun setTalkList(isScroll: Boolean, data: List<TalkListData>) {
        rv_frag_my_act_talk_list.visibility = View.VISIBLE
        tv_frag_my_act_talk_content.visibility = View.GONE
        srl_frag_my_act_talk_layout.isRefreshing = false

        for(item in data){
            totalNotRead += item.notRead?.toInt() ?: 0
        }
        tv_frag_my_act_talk_total.text = "확인이 필요한 메세지  " + totalNotRead.toString()+"개"
        if(!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_frag_my_act_talk_progress.visibility = View.GONE
    }

    /*    톡하기 목록 호출실패    */
    override fun setTalkListFailed(error: String?) {
        if(mAdapter.data.size == 0) {
            rv_frag_my_act_talk_list.visibility = View.GONE
            tv_frag_my_act_talk_content.visibility = View.VISIBLE
            ll_frag_my_act_talk_progress.visibility = View.GONE
            srl_frag_my_act_talk_layout.isRefreshing = false

            /*context?.let { Utility.instance.showToast(it, error) }*/
        }
    }

    /*    톡하기 삭제    */
    override fun setTalkDeleteComplete(message: String?) {
        mAdapter.removeItem()
        ll_frag_my_act_talk_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, message) }

        cb_frag_my_act_talk_select.visibility = View.GONE
        mAdapter.setEdit(false)
    }

    /*    톡하기 삭제 호출실패    */
    override fun setTalkDeleteFailed(error: String?) {
        ll_frag_my_act_talk_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    아이템 보유 확인    */
    override fun setItemCheckComplete(result: String?) {
        ll_frag_my_act_talk_progress.visibility = View.GONE

        when(result) {
            "Y" -> {
                val intent = Intent(context, LoveActivity::class.java)
                startActivity(intent)
            }

            "N" -> {
                context?.let {
                    Utility.instance.showTwoButtonAlert(it, it.resources.getString(R.string.app_name), it.resources.getString(R.string.msg_my_activity_love), DialogInterface.OnClickListener { dialog, which ->
                        if(which == DialogInterface.BUTTON_POSITIVE) {
                            val loveDialog = ItemLoveDialog()
                            activity?.supportFragmentManager?.let { it1 -> loveDialog.show(it1, AppKeyValue.instance.tagItemLoveDlg) }
                        }
                    })
                }
            }
        }
    }

    /*    아이템 보유 확인 호출실패    */
    override fun setItemCheckFailed(error: String?) {
        ll_frag_my_act_talk_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        mPresenter.detachView()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getTalkList(false, id, AppKeyValue.instance.listStartCnt, AppKeyValue.instance.listLimitCnt)
        mAdapter.notifyDataSetChanged()
    }

}
