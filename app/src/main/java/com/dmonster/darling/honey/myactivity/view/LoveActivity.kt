package com.dmonster.darling.honey.myactivity.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.dialog.ItemTalkDialog
import com.dmonster.darling.honey.myactivity.data.MemberData
import com.dmonster.darling.honey.myactivity.presenter.LoveListContract
import com.dmonster.darling.honey.myactivity.presenter.LoveListPresenter
import com.dmonster.darling.honey.myactivity.view.adapter.LoveAdapter
import com.dmonster.darling.honey.profile.view.ProfileActivity
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_love.*

class LoveActivity : BaseActivity(), LoveListContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: LoveListContract.Presenter
    private lateinit var mAdapter: LoveAdapter

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var id: String? = null
    private var mbNo: String? = null
    private var otherId: String? = null
    private var otherTalkId: String? = null
    private var otherArea: String? = null
    private var otherAge: String? = null
    private var gender: String? = null

    private var customPopup: CustomPopup? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_love)

        init()
        setListener()
        setEventBusListener()
    }

    private fun init() {
        ctb_act_love_toolbar.setTitle(resources.getString(R.string.popup_love_title))
        disposeBag = CompositeDisposable()
        mAdapter = LoveAdapter(this)
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv_act_love_list?.apply {
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

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        mbNo = Utility.instance.UserData().getUserMb()
        gender = Utility.instance.UserData().getUserGender()

        ll_act_love_progress.visibility = View.VISIBLE

        mPresenter = LoveListPresenter()
        mPresenter.attachView(this)
        mPresenter.getLoveList(false, AppKeyValue.instance.listStartCnt, AppKeyValue.instance.listLimitCnt, id)
    }

    private fun setListener() {
        /*    새로고침    */
        srl_act_love_layout.setOnRefreshListener {
            mPresenter.getLoveList(false, AppKeyValue.instance.listStartCnt, AppKeyValue.instance.listLimitCnt, id)
        }

        rv_act_love_list.addOnScrollListener(object: androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                srl_act_love_layout.isEnabled = cl_act_love_layout.getChildAt(0).top == 0
            }

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastCompletelyVisibleItemPosition().plus(1)
                val itemTotalCount = recyclerView.adapter?.itemCount
                if(lastVisibleItemPosition == itemTotalCount && itemTotalCount >= (AppKeyValue.instance.listLimitCnt.toInt())) {
                    mPresenter.getLoveList(true, lastVisibleItemPosition.toString(), AppKeyValue.instance.listLimitCnt, id)
                }
            }
        })

        /*    프로필    */
        mAdapter.itemClick = itemClickListener()

        /*    톡하기    */
        mAdapter.itemTalkClick = itemTalkClickListener()
    }

    private fun setEventBusListener() {
        EventBus.subjectCoinCharge.subscribe {
            if(it == AppKeyValue.instance.eventBusCoinCharge) {
                finish()
            }
        }.addTo(disposeBag)
    }

    private fun itemClickListener() = View.OnClickListener {
        val position = it.tag.toString().toInt()
        val mbNo = mAdapter.data[position].mbNo

        val intent = Intent(this, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppKeyValue.instance.profileMbNo, mbNo)
        startActivityForResult(intent, AppKeyValue.instance.requestProfileItem)
    }

    private fun itemTalkClickListener() = View.OnClickListener {
        val position = it.tag.toString().toInt()
        mAdapter.data[position].apply {
            otherId = mbId
            otherTalkId = mbNick
            otherArea = mbAddr1
            otherAge = mbAge
        }

        ll_act_love_progress.visibility = View.VISIBLE
        mPresenter.getTalkCheck(id, otherId)
    }

    /*    러브카드 목록    */
    override fun setLoveListComplete(isScroll: Boolean, data: List<MemberData>) {
        rv_act_love_list.visibility = View.VISIBLE
        tv_act_love_content.visibility = View.GONE
        srl_act_love_layout.isRefreshing = false

        if(!isScroll) {
            mAdapter.data.clear()
        }
        mAdapter.data.addAll(data)
        mAdapter.notifyDataSetChanged()

        ll_act_love_progress.visibility = View.GONE
    }

    /*    러브카드 목록 호출실패    */
    override fun setLoveListFailed(error: String?) {
        if(mAdapter.data.size == 0) {
            rv_act_love_list.visibility = View.GONE
            tv_act_love_content.visibility = View.VISIBLE
            ll_act_love_progress.visibility = View.GONE
            srl_act_love_layout.isRefreshing = false

            /*Utility.instance.showToast(this, error)*/
        }
    }

    /*    채팅방여부 확인    */
    override fun setTalkCheck(result: String?, roomNo: String?) {
        ll_act_love_progress.visibility = View.GONE

        when(result) {
            "Y" -> {
                val intent = Intent(this, TalkActivity::class.java)
                intent.putExtra(AppKeyValue.instance.talkRoomNo, roomNo)
                intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                intent.putExtra(AppKeyValue.instance.talkOtherTalkId, otherTalkId)
                /*    상단 타이틀정보    */
                intent.putExtra(AppKeyValue.instance.talkTitleName, otherTalkId)
                intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
                intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
                startActivity(intent)
            }

            "N" -> {
                if(gender == "F") {
                    val intent = Intent(this, TalkActivity::class.java)
                    intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
                    intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
                    intent.putExtra(AppKeyValue.instance.talkOtherTalkId, otherTalkId)
                    /*    상단 타이틀정보    */
                    intent.putExtra(AppKeyValue.instance.talkTitleName, otherTalkId)
                    intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
                    intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
                    startActivity(intent)
                }
                else {
                    ll_act_love_progress.visibility = View.VISIBLE
                    mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdTalk)
                }
            }
        }
    }

    /*    아이템 보유 확인    */
    override fun setItemCheckComplete(result: String?) {
        ll_act_love_progress.visibility = View.GONE

        when(result) {
            "Y" -> {
                customPopup = CustomPopup(
                        this,
                        "톡하기",
                        resources.getString(R.string.msg_profile_talk_item_use),
                        R.drawable.ic_talk_vivid,
                        object : CustomDialogInterface {
                            override fun onCancel(v: View) {
                                customPopupDismiss()
                            }

                            override fun onConfirm(v: View) {
                                mPresenter.setItemUse(id, AppKeyValue.instance.itemIdTalk, mbNo, otherId)
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
                val content = String.format(resources.getString(R.string.msg_profile_item), resources.getString(R.string.msg_profile_item_talk))
                Utility.instance.showTwoButtonAlert(this, resources.getString(R.string.app_name), content, DialogInterface.OnClickListener { dialog, which ->
                    if(which == DialogInterface.BUTTON_POSITIVE) {
                        /*EventBus.sendEventItemFrag(AppKeyValue.instance.eventBusItemFragActivity)
                        finish()*/

                        val talkDialog = ItemTalkDialog()
                        talkDialog.show(supportFragmentManager, AppKeyValue.instance.tagItemTalkDlg)
                    }
                })
            }
        }
    }

    /*    아이템 보유 확인 호출실패    */
    override fun setItemCheckFailed(error: String?) {
        ll_act_love_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    아이템 사용 호출실패    */
    override fun setItemUseFailed(error: String?) {
        ll_act_love_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    톡하기    */
    override fun setTalkComplete() {
        ll_act_love_progress.visibility = View.GONE

        val intent = Intent(this, TalkActivity::class.java)
        intent.putExtra(AppKeyValue.instance.talkMbNo, mbNo)
        intent.putExtra(AppKeyValue.instance.talkOtherId, otherId)
        intent.putExtra(AppKeyValue.instance.talkOtherTalkId, otherTalkId)
        /*    상단 타이틀정보    */
        intent.putExtra(AppKeyValue.instance.talkTitleName, otherTalkId)
        intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
        intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
        startActivity(intent)
    }

    /*    톡하기 호출실패    */
    override fun setTalkFailed(error: String?) {
        ll_act_love_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK) {
            if(requestCode == AppKeyValue.instance.requestProfileItem) {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

    fun customPopupDismiss() {
        customPopup?.dismiss()
    }
}
