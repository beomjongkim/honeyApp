package com.dmonster.darling.honey.myactivity.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.dialog.ItemTalkDialog
import com.dmonster.darling.honey.myactivity.data.ProfileData
import com.dmonster.darling.honey.myactivity.presenter.ProfileListContract
import com.dmonster.darling.honey.myactivity.presenter.ProfileListPresenter
import com.dmonster.darling.honey.myactivity.view.adapter.ProfileAdapter
import com.dmonster.darling.honey.profile.view.ProfileActivity
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_my_act_profile.*
import java.util.concurrent.TimeUnit

class MyActMenu03Fragment : BaseFragment(), ProfileListContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: ProfileListContract.Presenter
    private lateinit var mAdapter: ProfileAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null
    private var mbNo: String? = null
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
        return inflater.inflate(R.layout.fragment_my_act_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mAdapter = ProfileAdapter(context)
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rv_frag_my_act_profile_list?.apply {
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

        ll_frag_my_act_profile_progress.visibility = View.VISIBLE

        mPresenter = ProfileListPresenter()
        mPresenter.attachView(this)
        mPresenter.getProfileList(
                false,
                AppKeyValue.instance.listStartCnt,
                AppKeyValue.instance.listLimitCnt,
                id,
                AppKeyValue.instance.profileListOpen
        )
    }

    private fun setListener() {
        /*    새로고침    */
        srl_frag_my_act_profile_layout.setOnRefreshListener {
            mPresenter.getProfileList(
                    false,
                    AppKeyValue.instance.listStartCnt,
                    AppKeyValue.instance.listLimitCnt,
                    id,
                    AppKeyValue.instance.profileListOpen
            )
        }

        rv_frag_my_act_profile_list.addOnScrollListener(object :
                androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                    recyclerView: androidx.recyclerview.widget.RecyclerView,
                    newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                srl_frag_my_act_profile_layout.isEnabled =
                        cl_frag_my_act_profile_layout.getChildAt(0).top == 0
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
                    mPresenter.getProfileList(
                            true,
                            lastVisibleItemPosition.toString(),
                            AppKeyValue.instance.listLimitCnt,
                            id,
                            AppKeyValue.instance.profileListOpen
                    )
                }
            }
        })

        cb_frag_my_act_profile_all_check.setOnCheckedChangeListener { buttonView, isChecked ->
            mAdapter.setAllChecked(isChecked)
        }


        /*    선택삭제    */
        disposeBag.add(RxView.clicks(tv_frag_my_act_profile_edit)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    if (mAdapter.data.size > 0) {
                        context?.let { it1 ->
                            mAdapter.checkArray?.size?.let { it2 ->
                                if (it2 > 0) {
                                    Utility.instance.showTwoButtonAlert(
                                            it1,
                                            it1.resources.getString(R.string.app_name),
                                            it1.resources.getString(R.string.msg_my_activity_profile_delete),
                                            DialogInterface.OnClickListener { dialog, which ->
                                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                                    ll_frag_my_act_profile_progress.visibility =
                                                            View.VISIBLE

                                                    mAdapter.checkArray?.indices?.let { it3 ->
                                                        for (i in it3) {
                                                            val selectItem = mAdapter.checkArray?.get(i)
                                                            val idx =
                                                                    selectItem?.let { it4 -> mAdapter.data[it4].idx }
                                                            mPresenter.setProfileDelete(id, "open", idx)
                                                        }
                                                    }
                                                }
                                            })
                                } else {
                                    Utility.instance.showToast(
                                            it1,
                                            it1.resources.getString(R.string.msg_my_activity_select_profile)
                                    )
                                }
                            }
                        }
                    }
                })

        /*    프로필    */
        mAdapter.itemClick = itemClickListener()

        /*    톡하기    */
        mAdapter.itemTalkClick = itemVisitClickListener()
    }

    private fun itemClickListener() = View.OnClickListener {
        val position = it.tag.toString().toInt()
        val mbNo = mAdapter.data[position].mbNo

        val intent = Intent(context, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppKeyValue.instance.profileMbNo, mbNo)
        context?.startActivity(intent)
    }

    private fun itemVisitClickListener() = View.OnClickListener {
        context?.let { it1 ->
            val position = it.tag.toString().toInt()
            mAdapter.data[position].apply {
                otherId = mbId
                otherTalkId = mbNick
                otherArea = mbAddr1
                otherAge = mbAge
                otherGender = mbSex
            }

            if (gender == otherGender) {
                Utility.instance.showAlert(
                        it1,
                        it1.resources.getString(R.string.app_name),
                        it1.resources.getString(R.string.msg_profile_error_gender),
                        DialogInterface.OnClickListener { dialog, which -> })
            } else {
                ll_frag_my_act_profile_progress.visibility = View.VISIBLE
                mPresenter.getTalkCheck(id, otherId)
            }
        }
    }

    /*    프로필 열람 목록    */
    override fun setProfileListComplete(isScroll: Boolean, data: List<ProfileData>) {
        rv_frag_my_act_profile_list.visibility = View.VISIBLE
        tv_frag_my_act_profile_content.visibility = View.GONE
        srl_frag_my_act_profile_layout.isRefreshing = false

        if (!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_frag_my_act_profile_progress.visibility = View.GONE
    }

    /*    프로필 열람 목록 호출실패    */
    override fun setProfileListFailed(error: String?) {
        if (mAdapter.data.size == 0) {
            rv_frag_my_act_profile_list.visibility = View.GONE
            tv_frag_my_act_profile_content.visibility = View.VISIBLE
            ll_frag_my_act_profile_progress.visibility = View.GONE
            srl_frag_my_act_profile_layout.isRefreshing = false

            /*context?.let { Utility.instance.showToast(it, error) }*/
        }
    }

    /*    프로필 열람 회원 삭제    */
    override fun setProfileDeleteComplete(message: String?) {
        mAdapter.removeItem()
        ll_frag_my_act_profile_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, message) }
    }

    /*    프로필 열람 회원 삭제 호출실패    */
    override fun setProfileDeleteFailed(error: String?) {
        ll_frag_my_act_profile_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    채팅방여부 확인    */
    override fun setTalkCheck(result: String?, roomNo: String?) {
        ll_frag_my_act_profile_progress.visibility = View.GONE

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
                if (gender == "F") {
                    val intent = Intent(context, TalkActivity::class.java)
                    intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                    intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                    intent.putExtra(AppKeyValue.instance.talkOtherTalkId, otherTalkId)
                    /*    상단 타이틀정보    */
                    intent.putExtra(AppKeyValue.instance.talkTitleName, otherTalkId)
                    intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
                    intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
                    context?.startActivity(intent)
                } else {
                    ll_frag_my_act_profile_progress.visibility = View.VISIBLE
                    mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdTalk)
                }
            }
        }
    }

    fun customPopupDismiss() {
        customPopup?.dismiss()
    }

    /*    아이템 보유 확인    */
    override fun setItemCheckComplete(result: String?) {
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
                                    ll_frag_my_act_profile_progress.visibility = View.VISIBLE
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

                "N" -> {
                    val content = String.format(
                            it.resources.getString(R.string.msg_profile_item),
                            it.resources.getString(R.string.msg_profile_item_talk)
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
        ll_frag_my_act_profile_progress.visibility = View.GONE
    }

    /*    아이템 보유 확인 호출실패    */
    override fun setItemCheckFailed(error: String?) {
        ll_frag_my_act_profile_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    아이템 사용 호출실패    */
    override fun setItemUseFailed(error: String?) {
        ll_frag_my_act_profile_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    /*    톡하기    */
    override fun setTalkComplete() {
        ll_frag_my_act_profile_progress.visibility = View.GONE

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
        ll_frag_my_act_profile_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
