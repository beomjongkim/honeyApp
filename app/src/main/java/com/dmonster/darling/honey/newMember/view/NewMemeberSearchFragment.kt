package com.dmonster.darling.honey.newMember.view

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
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
import com.dmonster.darling.honey.dialog.ItemTalkDialog
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.myactivity.data.MemberData
import com.dmonster.darling.honey.myactivity.presenter.NewMemberListContract
import com.dmonster.darling.honey.myactivity.presenter.NewMemberListPresenter
import com.dmonster.darling.honey.myactivity.view.adapter.NewMemberAdapter
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.profile.view.GoodActivity
import com.dmonster.darling.honey.profile.view.InterestActivity
import com.dmonster.darling.honey.profile.view.ProfileActivity
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.youtube.view.YoutubePlayerActivity
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_my_act_new_member.*
import java.util.concurrent.TimeUnit

class NewMemeberSearchFragment : BaseFragment(), NewMemberListContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: NewMemberListContract.Presenter
    private lateinit var mAdapter: NewMemberAdapter
    private lateinit var rewardVM: RewardVM

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null
    private var mbNo: String? = null
    private var roomNo: String? = null
    private var otherId: String? = null
    private var otherTalkId: String? = null
    private var otherArea: String? = null
    private var otherAge: String? = null
    private var gender: String? = null
    private var otherGender: String? = null

    private var customPopup: CustomPopup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_act_new_member, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mAdapter = NewMemberAdapter(context)
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_frag_my_act_new_member_list?.apply {
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

        context?.let { id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
        mbNo = Utility.instance.UserData().getUserMb()
        gender = Utility.instance.UserData().getUserGender()

        ll_frag_my_act_new_member_progress.visibility = View.VISIBLE

        mPresenter = NewMemberListPresenter()
        mPresenter.attachView(this)
        mPresenter.getSpouseArea(id, mbNo)
        mPresenter.getNewMemberList(
            false,
            AppKeyValue.instance.listStartCnt,
            AppKeyValue.instance.listLimitCnt,
            id
        )

        rewardVM = RewardVM(activity as Activity)
        rewardVM.adCallback = object : RewardedAdCallback() {

            override fun onRewardedAdOpened() {
                // Ad opened.
                Log.d("NewMemberSearchFragment", "Ad opened.")
            }

            override fun onRewardedAdClosed() {
                // Ad closed.
                Log.d("NewMemberSearchFragment", "Ad closed.")
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                ll_frag_my_act_new_member_progress.visibility = View.VISIBLE
                // User earned reward.
                val subscriber = object : DisposableObserver<ResultItem<String>>() {
                    override fun onComplete() {
                        ll_frag_my_act_new_member_progress.visibility = View.GONE
                    }

                    override fun onError(e: Throwable) {
                        ll_frag_my_act_new_member_progress.visibility = View.GONE
                    }

                    override fun onNext(item: ResultItem<String>) {
                        item.let { it ->
                            if (it.isSuccess) {
                                context?.let { it1 ->
                                    Utility.instance.showToast(
                                        it1,
                                        "성공적으로 이용권을 구매하였습니다."
                                    )
                                }
                            }
                        }
                        ll_frag_my_act_new_member_progress.visibility = View.GONE
                    }
                }
                ItemModel().buyItem(id, 1, subscriber)
                Log.d("NewMemberSearchFragment", "User earned reward.")
            }

            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
                Log.d("NewMemberSearchFragment", "Ad failed to display.")
            }
        }
    }

    private fun setListener() {
        /*    새로고침    */
        srl_frag_my_act_new_member_layout.setOnRefreshListener {
            mPresenter.getNewMemberList(
                false,
                AppKeyValue.instance.listStartCnt,
                AppKeyValue.instance.listLimitCnt,
                id
            )
        }

        rv_frag_my_act_new_member_list.addOnScrollListener(object :
            androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: androidx.recyclerview.widget.RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                srl_frag_my_act_new_member_layout.isEnabled =
                    cl_frag_my_act_new_member_layout.getChildAt(0).top == 0
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
                    mPresenter.getNewMemberList(
                        true,
                        lastVisibleItemPosition.toString(),
                        AppKeyValue.instance.listLimitCnt,
                        id
                    )
                }
            }
        })

        /*    이상형 선택 지역    */
        disposeBag.add(RxView.clicks(ll_frag_my_act_new_member_ideal_type)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                EventBus.sendEventIdealType(AppKeyValue.instance.eventBusIdealType)
            })

        /*    프로필    */
        mAdapter.itemClick = itemClickListener()

        /*    호감, 관심있어요    */
        mAdapter.itemGoodClick = itemGoodClickListener()

        /*    톡하기    */
        mAdapter.itemTalkClick = itemTalkClickListener()
    }

    private fun itemClickListener() = View.OnClickListener {
        context?.let { it1 ->
            val position = it.tag.toString().toInt()
            val mbNo = mAdapter.data[position].mbNo

            val intent = Intent(context, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(AppKeyValue.instance.profileMbNo, mbNo)
            it1.startActivity(intent)
        }
    }

    private fun itemGoodClickListener() = View.OnClickListener {
        context?.let { it1 ->
            val position = it.tag.toString().toInt()
            mAdapter.data[position].apply {
                val profileImage = mbImgThumb
                val talkId = mbNick
                val type = mbChar

                if (gender == otherGender) {
                    Utility.instance.showAlert(
                        it1,
                        it1.resources.getString(R.string.app_name),
                        it1.resources.getString(R.string.msg_profile_error_good),
                        DialogInterface.OnClickListener { dialog, which -> })
                } else {
                    val pref = context?.getSharedPreferences("Pref", Context.MODE_PRIVATE)
                    if (pref != null) {
                        if (pref.getBoolean(AppKeyValue.instance.hasFreePass, false)) {
                            val intent: Intent
                            if (gender == "F") {
                                intent = Intent(context, InterestActivity::class.java)
                            } else {
                                intent = Intent(context, GoodActivity::class.java)
                            }
                            Utility.instance.showTwoButtonAlert(
                                it1,
                                it1.getString(R.string.interest_title),
                                talkId + it1.getString(R.string.interest_talk_id),
                                object : CustomDialogInterface {
                                    override fun onConfirm(v: View) {

                                        intent.putExtra(AppKeyValue.instance.goodOtherId, otherId)
                                        intent.putExtra(
                                            AppKeyValue.instance.goodOtherProfileImage,
                                            profileImage
                                        )
                                        intent.putExtra(
                                            AppKeyValue.instance.goodOtherTalkId,
                                            talkId
                                        )
                                        intent.putExtra(AppKeyValue.instance.goodOtherType, type)
                                        startActivity(intent)
                                    }

                                    override fun onCancel(v: View) {
                                    }

                                })
                        } else {
                            setPassNeed()
                        }
                    }
                }
            }
        }
    }

    private fun itemTalkClickListener() = View.OnClickListener {
        context?.let { it1 ->
            val position = it.tag.toString().toInt()
            mAdapter.data[position].apply {
                otherId = mbId
                otherTalkId = mbNick
                otherGender = mbSex
                otherArea = mbAddr1
                otherAge = mbAge
            }

            if (gender == otherGender) {
                Utility.instance.showAlert(
                    it1,
                    it1.resources.getString(R.string.app_name),
                    it1.resources.getString(R.string.msg_profile_error_gender),
                    DialogInterface.OnClickListener { dialog, which -> })
            } else {
                ll_frag_my_act_new_member_progress.visibility = View.VISIBLE
                mPresenter.getTalkCheck(id, otherId)
            }
        }
    }

    /*    이상형 지역    */
    override fun setSpouseArea(area: String?) {
        if (TextUtils.isEmpty(area)) {
            tv_frag_my_act_new_member_area.text =
                context?.resources?.getString(R.string.my_activity_new_member_area_default)
        } else {
            tv_frag_my_act_new_member_area.text = area
        }
    }

    /*    신규회원 목록    */
    override fun setNewMemberListComplete(isScroll: Boolean, data: List<MemberData>) {
        rv_frag_my_act_new_member_list.visibility = View.VISIBLE
        tv_frag_my_act_new_member_content.visibility = View.GONE
        srl_frag_my_act_new_member_layout.isRefreshing = false

        if (!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_frag_my_act_new_member_progress.visibility = View.GONE
    }

    /*    신규회원 목록 호출실패    */
    override fun setNewMemberListFailed(error: String?) {
        if (mAdapter.data.size == 0) {
            rv_frag_my_act_new_member_list.visibility = View.GONE
            tv_frag_my_act_new_member_content.visibility = View.VISIBLE
            ll_frag_my_act_new_member_progress.visibility = View.GONE
            srl_frag_my_act_new_member_layout.isRefreshing = false

            /*context?.let { Utility.instance.showToast(it, error) }*/
        }
    }

    /*    채팅방여부 확인    */
    override fun setTalkCheck(result: String?, roomNo: String?) {
        ll_frag_my_act_new_member_progress.visibility = View.GONE

        when (result) {
            "Y" -> {
                val intent = Intent(context, TalkActivity::class.java)
                intent.putExtra(AppKeyValue.instance.talkRoomNo, roomNo)
                intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                intent.putExtra(AppKeyValue.instance.talkOtherTalkId, otherTalkId)
                /*    상단 타이틀정보    */
                intent.putExtra(AppKeyValue.instance.talkTitleName, otherTalkId)
                intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
                intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
                context?.startActivity(intent)
            }

            "N" -> {
//                if (gender == "F") {
//                    val intent = Intent(context, TalkActivity::class.java)
//                    intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
//                    intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
//                    intent.putExtra(AppKeyValue.instance.talkOtherTalkId, otherTalkId)
//                    /*    상단 타이틀정보    */
//                    intent.putExtra(AppKeyValue.instance.talkTitleName, otherTalkId)
//                    intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
//                    intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
//                    context?.startActivity(intent)
//                } else {
                ll_frag_my_act_new_member_progress.visibility = View.VISIBLE
                mPresenter.checkPass(id, AppKeyValue.instance.itemIdTalk)
//                }
            }
        }
    }

    /*    아이템 보유 확인    */
    override fun setItemCheckComplete(type: String?, result: String?) {
        ll_frag_my_act_new_member_progress.visibility = View.GONE

        context?.let {
            when (result) {
                "Y" -> {
                    customPopup = CustomPopup(
                        it,
                        "톡하기",
                        otherTalkId + resources.getString(R.string.msg_profile_talk_item_use),
                        R.drawable.ic_talk_vivid,
                        object : CustomDialogInterface {
                            override fun onCancel(v: View) {
                                customPopupDismiss()
                            }

                            override fun onConfirm(v: View) {
                                mPresenter.setItemUse(
                                    id,
                                    AppKeyValue.instance.itemIdTalk,
                                    mbNo,
                                    otherId
                                )
                                customPopupDismiss()
                            }
                        })
                    customPopup?.popupVM?.let { it1 ->
                        it1.positiveText.value = "보내기"
                        it1.negativeText.value = "취소"
                    }
                    customPopup?.show()

                }

                else -> return
            }
        }
    }

    /*    아이템 보유 확인 호출실패    */
    override fun setItemCheckFailed(error: String?) {
        ll_frag_my_act_new_member_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    아이템 사용 호출실패    */
    override fun setItemUseFailed(error: String?) {
        ll_frag_my_act_new_member_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    톡하기    */
    override fun setTalkComplete() {
        ll_frag_my_act_new_member_progress.visibility = View.GONE

        val intent = Intent(context, TalkActivity::class.java)
        intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
        intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
        intent.putExtra(AppKeyValue.instance.talkOtherTalkId, otherTalkId)
        /*    상단 타이틀정보    */
        intent.putExtra(AppKeyValue.instance.talkTitleName, otherTalkId)
        intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
        intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
        context?.startActivity(intent)
    }

    /*    톡하기 호출실패    */
    override fun setTalkFailed(error: String?) {
        ll_frag_my_act_new_member_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    override fun setPassNeed() {
        ll_frag_my_act_new_member_progress.visibility = View.GONE
        context?.let {
            val popup = CustomPopup(
                it,
                "이용권 구매",
                "이용권을 구매해서 아래 기능을 마음껏 이용해보세요!\n" + getString(R.string.msg_freepass_description),
                R.drawable.ic_talk_vivid,
                object : CustomDialogInterface {
                    override fun onConfirm(v: View) {
                        rewardVM.rewardedAd.show(activity, rewardVM.adCallBackBase)
                    }

                    override fun onCancel(v: View) {
                        val intent = Intent(it, MainActivity::class.java)
                        intent.putExtra(AppKeyValue.instance.goToMarket, true)
                        startActivity(intent)

                    }
                })
            popup.popupVM.negativeText.value = "1개월 이용권\n구매하기"
            popup.popupVM.positiveText.value = "광고 시청 후\n이용권 받기"
            popup.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        mPresenter.detachView()
    }

    fun customPopupDismiss() {
        customPopup?.dismiss()
    }

}
