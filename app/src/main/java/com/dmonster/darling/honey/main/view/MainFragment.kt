package com.dmonster.darling.honey.main.view

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.databinding.FragmentMainBinding
import com.dmonster.darling.honey.dialog.*
import com.dmonster.darling.honey.main.data.MainListData
import com.dmonster.darling.honey.main.presenter.MainListContract
import com.dmonster.darling.honey.main.presenter.MainListPresenter
import com.dmonster.darling.honey.main.view.adapter.MainAdapter
import com.dmonster.darling.honey.main.viewmodel.FilterVM
import com.dmonster.darling.honey.newMember.view.NewMemberFragment
import com.dmonster.darling.honey.notice.data.NoticeData
import com.dmonster.darling.honey.notice.view.NoticeActivity
import com.dmonster.darling.honey.profile.view.ProfileActivity
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment(), MainListContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: MainListContract.Presenter
    private lateinit var mAdapter: MainAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var profileState: Boolean = true
    private var dormantState: Boolean = false
    private var searchArea: String? = null
    private var searchGender: String? = AppKeyValue.instance.keyYes
    private var searchAge: String? = null
    private var id: String? = null
    private var talkId: String? = null
    private var gender: String? = null
    private var mbNo: String? = null
    private var otherId: String? = null
    private var otherArea: String? = null
    private var otherAge: String? = null
    private var timerTask: TimerTask? = null

    private var isInit: Boolean = true
    private var customPopup: CustomPopup? = null
    lateinit var binding : FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_main,
                container,
                false
            )
//        binding.bannerVM = RollingBannerVM()
        context?.let {
            binding.filterVM =
                object : FilterVM(Utility.instance.getPref(it, AppKeyValue.instance.savePrefID)) {
                    override fun profileDialog() {
                        setProfileDialog()
                    }

                    override fun dormantDialog() {
                        setDormantDialog()
                    }
                }
        }
        binding.lifecycleOwner = this
//        return inflater.inflate(R.layout.fragment_main, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mAdapter = MainAdapter()
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_frag_main_list?.apply {
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


//        ll_frag_main_progress.visibility = View.VISIBLE

        context?.let { id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
        gender = Utility.instance.UserData().getUserGender()

        mPresenter = MainListPresenter()
        mPresenter.attachView(this)
        mPresenter.setRefresh(id, AppKeyValue.instance.itemIdRefresh)
//        mPresenter.getNoticeList(id)

        //처음 들어오면 자동올리기
        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
        when {
            profileState -> {
                ll_frag_main_progress.visibility = View.VISIBLE
                mPresenter.setRefreshList(id)
            }
        }
    }

    fun customPopupDismiss() {
        customPopup?.dismiss()
    }

    private fun setListener() {
        /*    새로고침    */
//        srl_frag_main_layout.setOnRefreshListener {
//            mPresenter.getMainList(
//                    false,
//                    AppKeyValue.instance.listStartCnt,
//                    AppKeyValue.instance.listLimitCnt,
//                    id,
//                    searchArea,
//                    searchGender,
//                    searchAge
//            )
//        }

        /*    공지사항    */
        disposeBag.add(RxView.clicks(ll_frag_main_notice)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(context, NoticeActivity::class.java)
                startActivity(intent)
            })

        /*    내 프로필 위로 올리기    */
        disposeBag.add(RxView.clicks(btn_frag_main_refresh)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                activity?.let { it1 ->
                    dormantState =
                        Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
                    profileState =
                        Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
                    when {
                        dormantState -> setDormantDialog()
                        profileState -> {
                            ll_frag_main_progress.visibility = View.VISIBLE
                            mPresenter.setRefreshList(id)
                        }
                        else -> setProfileDialog()
                    }
                }
            })

        /*    내 프로필 위로 올리기    */
//        disposeBag.add(RxView.clicks(tv_frag_main_refresh)
//                .throttleFirst(1, TimeUnit.SECONDS)
//                .subscribe {
//                    activity?.let { it1 ->
//                        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
//                        profileState = Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
//                        when {
//                            dormantState -> setDormantDialog()
//                            profileState -> {
//                                ll_frag_main_progress.visibility = View.VISIBLE
//                                mPresenter.setRefreshList(id)
//                            }
//                            else -> setProfileDialog()
//                        }
//                    }
//                })

        /*    프로필    */
        mAdapter.itemClick = itemClickListener()

        /*    톡하기    */
        mAdapter.talkClick = talkClickListener()
    }

    private fun itemClickListener() = View.OnClickListener {
        context?.let { it1 ->
            dormantState =
                Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
            profileState =
                Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
            val myMbNo = Utility.instance.UserData().getUserMb()

            when {
                dormantState -> setDormantDialog()
                profileState -> {
                    val position = it.tag.toString().toInt()
                    val mbNo = mAdapter.data[position].mbNo
                    val itemUse = mAdapter.data[position].itemUseProfile

                    if (myMbNo == mbNo) {
                        EventBus.sendEventProfile(AppKeyValue.instance.eventBusProfile)
                    } else {
                        val intent = Intent(it1, ProfileActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra(AppKeyValue.instance.profileMbNo, mbNo)
                        it1.startActivity(intent)
                    }
                }
                else -> setProfileDialog()
            }
        }
    }

    private fun talkClickListener() = View.OnClickListener {
        context?.let { it1 ->
            dormantState =
                Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
            profileState =
                Utility.instance.UserData().getUserProfile() == AppKeyValue.instance.keyYes
            val myMbNo = Utility.instance.UserData().getUserMb()

            when {
                dormantState -> setDormantDialog()
                profileState -> {
                    val position = it.tag.toString().toInt()
                    var otherGender: String?
                    mAdapter.data[position].apply {
                        otherGender = mbSex
                        this@MainFragment.mbNo = mbNo
                        otherId = mbId
                        talkId = mbNick
                        otherArea = mbAddr1
                        otherAge = mbAge
                    }

                    when {
                        myMbNo == mbNo -> EventBus.sendEventProfile(AppKeyValue.instance.eventBusProfile)
                        gender == otherGender -> Utility.instance.showAlert(
                            it1,
                            it1.resources.getString(R.string.app_name),
                            it1.resources.getString(R.string.msg_profile_error_gender),
                            DialogInterface.OnClickListener { dialog, which -> })
                        else -> {
                            ll_frag_main_progress.visibility = View.VISIBLE
                            mPresenter.getTalkCheck(id, otherId)
                        }
                    }
                }
                else -> setProfileDialog()
            }
        }
    }

    /*    회원목록    */
    override fun setMainList(isScroll: Boolean, data: List<MainListData>) {
        rv_frag_main_list.visibility = View.VISIBLE
        tv_frag_main_content.visibility = View.GONE
        srl_frag_main_layout.isRefreshing = false

        if (!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_frag_main_progress.visibility = View.GONE

        dormantState = Utility.instance.UserData().getUserDormant() == AppKeyValue.instance.keyYes
        if (dormantState) {
            setDormantDialog()
        }
    }

    /*    회원목록 호출실패    */
    override fun setMainListFailed(isScroll: Boolean, error: String?) {
        if (!isScroll || mAdapter.data.size == 0) {
            rv_frag_main_list.visibility = View.GONE
            tv_frag_main_content.visibility = View.VISIBLE
            ll_frag_main_progress.visibility = View.GONE
            srl_frag_main_layout.isRefreshing = false

            /*context?.let { Utility.instance.showToast(it, error) }*/
        }
    }

    /*    자동갱신 여부 확인    */
    override fun getRefresh(result: String?) {
        when (result) {
            "Y" -> {//자동 갱신 아이템 사용 없이 항상 프로필 올리기 기능으로 보이게끔 작업.
                tv_frag_main_refresh.visibility = View.GONE
                btn_frag_main_refresh.visibility = View.VISIBLE
            }

            "N" -> {
                tv_frag_main_refresh.visibility = View.GONE
                btn_frag_main_refresh.visibility = View.VISIBLE
            }
        }
    }

    /*    위로 올리기    */
    override fun setRefreshListComplete() {
        mPresenter.getMainList(
            false,
            AppKeyValue.instance.listStartCnt,
            AppKeyValue.instance.listLimitCnt,
            id,
            searchArea,
            searchGender,
            searchAge
        )
        context?.let {
            if (!isInit) {
                Utility.instance.showTwoButtonAlert(
                    it,
                    it.resources.getString(R.string.main_jumpup_complete_title),
                    it.resources.getString(R.string.main_jumpup_complete),
                    "신규회원", "안함",
                    DialogInterface.OnClickListener { dialog, which ->
                        when(which){
                            Dialog.BUTTON_POSITIVE->{
                                EventBus.sendEventIdealType(AppKeyValue.instance.eventBusIdealType)

                            }
                        }
                    })
            }
            isInit = false
        }
    }

    /*    위로 올리기 호출실패    */
    override fun setRefreshListFailed(error: String?) {
        ll_frag_main_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    공지사항 목록    */
    override fun setNoticeList(data: List<NoticeData>) {
//        if(data.isNotEmpty()) {
        if (false) {//임시로 세팅하지 않기
            ll_frag_main_notice.visibility = View.VISIBLE

            val noticeArray: ArrayList<String> = ArrayList()
            for (i in data.indices) {
                data[i].wrSubject?.let { noticeArray.add(it) }
            }

            val handler01 = Handler()
            val handler02 = Handler()
            var count = 0
            timerTask = object : TimerTask() {
                override fun run() {
                    count = count.plus(1)
                    if (count >= noticeArray.size) {
                        count = 0
                    }

                    val animation01 = AnimationUtils.loadAnimation(context, R.anim.anim_slide_top01)
                    val animation02 = AnimationUtils.loadAnimation(context, R.anim.anim_slide_top02)

                    val updater = Runnable {
                        if (tv_frag_main_notice != null) {
                            tv_frag_main_notice.text = noticeArray[count]
                            tv_frag_main_notice.startAnimation(animation01)
                        }
                    }
                    handler01.post(updater)

                    handler02.postDelayed({
                        if (tv_frag_main_notice != null) {
                            tv_frag_main_notice.startAnimation(animation02)
                        }
                    }, 5500)
                }
            }
            val timer = Timer()
            timer.schedule(timerTask, 0, 6000)
        }
    }

    /*    공지사항 목록 호출실패    */
    override fun setNoticeListFailed(error: String?) {
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    채팅방 여부 확인    */
    override fun setTalkCheck(result: String?, roomNo: String?) {
        ll_frag_main_progress.visibility = View.GONE
        when (result) {
            "Y" -> {
                val intent = Intent(context, TalkActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra(AppKeyValue.instance.talkRoomNo, roomNo)
                intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                intent.putExtra(AppKeyValue.instance.talkOtherTalkId, talkId)
                /*    상단 타이틀정보    */
                intent.putExtra(AppKeyValue.instance.talkTitleName, talkId)
                intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
                intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
                startActivity(intent)
            }

            "N" -> {
                if (gender == "F") {
                    val intent = Intent(context, TalkActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra(AppKeyValue.instance.talkRoomNo, roomNo)
                    intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                    intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                    intent.putExtra(AppKeyValue.instance.talkOtherTalkId, talkId)
                    /*    상단 타이틀정보    */
                    intent.putExtra(AppKeyValue.instance.talkTitleName, talkId)
                    intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
                    intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
                    startActivity(intent)
                } else {
                    ll_frag_main_progress.visibility = View.VISIBLE
                    mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdTalk)
                }
            }
        }
    }

    /*    아이템 사용    */
    override fun setItemUseComplete(type: String?, result: String?) {
        ll_frag_main_progress.visibility = View.GONE
        context?.let {
            when (result) {
                "Y" -> {

                    customPopup = CustomPopup(
                        it,
                        "톡하기",
                        resources.getString(R.string.msg_profile_talk_item_use),
                        R.drawable.ic_talk_vivid,
                        object : CustomDialogInterface {
                            override fun onCancel(v: View) {
                                customPopupDismiss()
                            }

                            override fun onConfirm(v: View) {
                                ll_frag_main_progress.visibility = View.VISIBLE
                                mPresenter.setItemUse(
                                    it,
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

                "N" -> {
                    val content = String.format(
                        resources.getString(R.string.msg_profile_item),
                        resources.getString(R.string.msg_profile_item_talk)
                    )
                    Utility.instance.showTwoButtonAlert(
                        it,
                        it.resources.getString(R.string.app_name),
                        content,
                        DialogInterface.OnClickListener { dialog, which ->
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                val talkDialog = ItemTalkDialog()
                                activity?.supportFragmentManager?.let { it1 ->
                                    talkDialog.show(
                                        it1,
                                        AppKeyValue.instance.tagItemTalkDlg
                                    )
                                }
                            }
                        })
                }
                else -> return
            }
        }
    }

    /*    아이템 사용 호출실패    */
    override fun setItemUseFailed(error: String?) {
        ll_frag_main_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    톡하기    */
    override fun setTalkComplete() {
        ll_frag_main_progress.visibility = View.GONE

        val intent = Intent(context, TalkActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
        intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
        intent.putExtra(AppKeyValue.instance.talkOtherTalkId, talkId)
        /*    상단 타이틀정보    */
        intent.putExtra(AppKeyValue.instance.talkTitleName, talkId)
        intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
        intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
        startActivity(intent)
    }

    /*    톡하기 호출실패    */
    override fun setTalkFailed(error: String?) {
        ll_frag_main_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    회원정보 입력    */
    private fun setProfileDialog() {
        var mbNick = "회원"
        for (data in mAdapter.data) {
            if (data.mbId == id) {
                mbNick = data.mbNick.toString()
                break
            }
        }

        context?.let {
            Utility.instance.showTwoButtonAlert(
                it,
                it.resources.getString(R.string.popup_title_set_profile),
                mbNick + it.resources.getString(R.string.msg_error_profile),
                it.resources.getString(R.string.dialog_register),
                it.resources.getString(R.string.dialog_no),
                DialogInterface.OnClickListener { dialog, which ->
                    EventBus.sendEventProfile(AppKeyValue.instance.eventBusProfile)
                })
        }
    }

    /*    휴면계정    */
    private fun setDormantDialog() {
        val dormantClearDialog = DormantClearDialog()
        activity?.supportFragmentManager?.let {
            dormantClearDialog.show(
                it,
                AppKeyValue.instance.tagDormantClearDlg
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        mPresenter.detachView()
        timerTask?.cancel()
    }
}
