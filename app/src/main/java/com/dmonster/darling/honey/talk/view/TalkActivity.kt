package com.dmonster.darling.honey.talk.view

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.RewardVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.dialog.ItemTalkDialog
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.profile.view.ProfileActivity
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.talk.presenter.TalkContract
import com.dmonster.darling.honey.talk.presenter.TalkPresenter
import com.dmonster.darling.honey.talk.view.adapter.TalkAdapter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.SoftKeyboard
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.jakewharton.rxbinding2.view.RxView
import gun0912.tedbottompicker.TedBottomPicker
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_talk.*
import kotlinx.android.synthetic.main.fragment_my_act_new_member.*
import java.util.concurrent.TimeUnit

class TalkActivity : BaseActivity(), TalkContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: TalkContract.Presenter
    private lateinit var mAdapter: TalkAdapter
    private lateinit var rewardVM : RewardVM

    private var viewLayoutManager: androidx.recyclerview.widget.RecyclerView.LayoutManager? = null
    private var softkeyboard: SoftKeyboard? = null

    private var talkMessage: String? = null
    private var id: String? = null
    private var roomNo: String? = null
    private var mbNo: String? = null
    private var otherId: String? = null
    private var otherTalkId: String? = null
    private var gender: String? = null
    private var imageUri: Uri? = null
    private var moreLayoutState: Boolean = false
    private var keyboardState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk)

        init()
        setListener()
        setEventBusListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mAdapter = TalkAdapter()
        viewLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        rv_act_talk_list?.apply {
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

        val titleName = intent.getStringExtra(AppKeyValue.instance.talkTitleName)
        val titleArea = intent.getStringExtra(AppKeyValue.instance.talkTitleArea)
        val titleAge = intent.getStringExtra(AppKeyValue.instance.talkTitleAge)

        if (!TextUtils.isEmpty(titleArea) && !TextUtils.isEmpty(titleAge)) {
            tv_act_talk_title.text =
                String.format(resources.getString(R.string.talk_title_detail), titleName, titleArea, titleAge)
        } else {
            tv_act_talk_title.text = resources.getString(R.string.talk_title)
        }

        lav_act_talk_more.isEnabled = false
        et_act_talk_content.isEnabled = false
        btn_act_talk_send.isEnabled = false

        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        gender = Utility.instance.UserData().getUserGender()
        mbNo = intent.getStringExtra(AppKeyValue.instance.talkMbNo)
        roomNo = intent.getStringExtra(AppKeyValue.instance.talkRoomNo)
        otherId = intent.getStringExtra(AppKeyValue.instance.talkOtherId)
        otherTalkId = intent.getStringExtra(AppKeyValue.instance.talkOtherTalkId)

        mPresenter = TalkPresenter()
        mPresenter.attachView(this)

        if (!TextUtils.isEmpty(roomNo)) {
            ll_act_talk_progress.visibility = View.VISIBLE
            mPresenter.getTalkList(
                false,
                AppKeyValue.instance.listStartCnt,
                AppKeyValue.instance.talkLimitCnt,
                id,
                roomNo
            )
        } else {
            lav_act_talk_more.isEnabled = true
            et_act_talk_content.isEnabled = true
            btn_act_talk_send.isEnabled = true
        }

        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        softkeyboard = SoftKeyboard(cl_act_talk_layout, keyboard)

        rewardVM = RewardVM(this)
        rewardVM.adCallback = object : RewardedAdCallback() {

            override fun onRewardedAdOpened() {
                // Ad opened.
                Log.d("TalkActivity", "Ad opened.")
            }

            override fun onRewardedAdClosed() {
                // Ad closed.
                Log.d("TalkActivity", "Ad closed.")
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                ll_act_talk_progress.visibility = View.VISIBLE
                // User earned reward.
                val subscriber = object : DisposableObserver<ResultItem<String>>() {
                    override fun onComplete() {
                        ll_act_talk_progress.visibility = View.GONE
                    }

                    override fun onError(e: Throwable) {
                        ll_act_talk_progress.visibility = View.GONE
                    }

                    override fun onNext(item: ResultItem<String>) {
                        item.let { it ->
                            if (it.isSuccess) {
                                Utility.instance.showToast(this@TalkActivity, "성공적으로 이용권을 구매하였습니다.")
                            }
                        }
                        ll_act_talk_progress.visibility = View.GONE
                    }
                }
                ItemModel().buyItem(id, 1, subscriber)
                Log.d("TalkActivity", "User earned reward.")
            }

            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
                Log.d("TalkActivity", "Ad failed to display.")
            }
        }

    }

    private fun setListener() {
        rv_act_talk_list.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastCompletelyVisibleItemPosition().plus(1)
                val itemTotalCount = recyclerView.adapter?.itemCount
                keyboardState = lastVisibleItemPosition == itemTotalCount
                if (!rv_act_talk_list.canScrollVertically(-1)) {
                    mPresenter.getTalkList(
                        true,
                        itemTotalCount.toString(),
                        AppKeyValue.instance.talkLimitCnt,
                        id,
                        roomNo
                    )
                }
            }
        })

        softkeyboard?.setSoftKeyboardCallback(object : SoftKeyboard.SoftKeyboardChanged {
            override fun onSoftKeyboardShow() {
                Handler(Looper.getMainLooper()).post {
                    if (keyboardState) {
                        /*rv_act_talk_list.scrollToPosition(talkAdapter.itemCount.minus(1))*/
                        rv_act_talk_list.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                            if (bottom < oldBottom) {
                                viewLayoutManager?.smoothScrollToPosition(rv_act_talk_list, null, mAdapter.itemCount)
                            }
                        }
                    }
                }
            }

            override fun onSoftKeyboardHide() {

            }
        })

        /*    뒤로가기    */
        disposeBag.add(RxView.clicks(iv_act_talk_back)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                finish()
            })



        /*    메인으로    */
        disposeBag.add(RxView.clicks(ll_act_talk_main)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            })

        /*    나가기    */
        disposeBag.add(RxView.clicks(ll_act_talk_out)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                finish()
            })

        /*    차단    */
        disposeBag.add(RxView.clicks(ll_act_talk_block)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                Utility.instance.showTwoButtonAlert(
                    this,
                    resources.getString(R.string.app_name),
                    String.format(resources.getString(R.string.msg_block), otherTalkId),
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            ll_act_talk_progress.visibility = View.VISIBLE
                            mPresenter.setBlock(id, mbNo, AppKeyValue.instance.blockBlock)
                        }
                    })
            })

        /*    삭제    */
        disposeBag.add(RxView.clicks(ll_act_talk_delete)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                Utility.instance.showTwoButtonAlert(
                    this,
                    resources.getString(R.string.app_name),
                    resources.getString(R.string.msg_talk_delete),
                    DialogInterface.OnClickListener { dialog, which ->
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (!TextUtils.isEmpty(roomNo)) {
                                ll_act_talk_progress.visibility = View.VISIBLE
                                mPresenter.setDelete(id, roomNo)
                            } else {
                                finish()
                            }
                        }
                    })
            })

        /*    더보기    */
        disposeBag.add(RxView.clicks(lav_act_talk_more)
            //.throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                Utility.instance.hideSoftKeyboard(this)
                val layoutHeight = Utility.instance.getDip(this, R.dimen.default_margin_100dp)
                if (!moreLayoutState) {

                    lav_act_talk_more.setMinFrame(40)
                    lav_act_talk_more.setMaxFrame(100)
                    lav_act_talk_more.playAnimation()

                    val va = ValueAnimator.ofInt(0, layoutHeight)
                    va.duration = 0
                    va.addUpdateListener { it1 ->
                        val value = it1.animatedValue
                        ll_act_talk_more_layout.layoutParams.height = value.toString().toInt()
                        ll_act_talk_more_layout.requestLayout()
                    }
                    va.start()
                    moreLayoutState = true

                    ll_act_talk_more_layout.visibility = View.VISIBLE

                } else {
                    lav_act_talk_more.setMinFrame(100)
                    lav_act_talk_more.setMaxFrame(180)
                    lav_act_talk_more.playAnimation()
                    val va = ValueAnimator.ofInt(layoutHeight, 0)
                    va.duration = 0
                    va.addUpdateListener { it1 ->
                        val value = it1.animatedValue
                        ll_act_talk_more_layout.layoutParams.height = value.toString().toInt()
                        ll_act_talk_more_layout.requestLayout()
                    }
                    va.start()
                    moreLayoutState = false

                }
            })

        /*    더보기 앨범    */
        disposeBag.add(RxView.clicks(ll_act_talk_more_photo)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val tedBottomPicker = TedBottomPicker.Builder(this)
                    .setImageProvider { imageView, imageUri ->
                        Glide.with(this).load(imageUri).apply(RequestOptions().centerCrop()).into(imageView)
                    }
                    .setOnImageSelectedListener { uri ->
                        imageUri = uri
                        Utility.instance.showTwoButtonAlert(
                            this,
                            resources.getString(R.string.app_name),
                            resources.getString(R.string.msg_talk_send_image),
                            DialogInterface.OnClickListener { dialog, which ->
                                if (which == DialogInterface.BUTTON_POSITIVE) {
                                    val time = Utility.instance.getNowTimeFormat(this)
                                    val talkData = TalkData()

                                    talkData.chatType = resources.getString(R.string.talk_type_message)
                                    talkData.chatSender = id
                                    talkData.chatSendDate = time
                                    talkData.chatImg = imageUri.toString()
                                    talkData.chatReadDate = AppKeyValue.instance.talkCheckTime
                                    mAdapter.addItemData(talkData)
                                    rv_act_talk_list.scrollToPosition(mAdapter.itemCount.minus(1))

                                    rv_act_talk_list.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                                        if (bottom < oldBottom) {
                                            viewLayoutManager?.smoothScrollToPosition(
                                                rv_act_talk_list,
                                                null,
                                                mAdapter.itemCount
                                            )
                                        }
                                    }

//                                    if (gender == "F") {
//                                        mPresenter.setSendTalk(id, otherId, "", imageUri)
//                                    } else {
                                        mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdTalk)
//                                    }
                                }
                            })
                    }
                    .setPreviewMaxCount(1000)
                    .setCameraTile(R.drawable.camera_shooting)
                    .setCameraTileBackgroundResId(R.color.color_black)
                    .setGalleryTile(R.drawable.camera_album)
                    .setGalleryTileBackgroundResId(R.color.color_black)
                    .create()
                tedBottomPicker.show(supportFragmentManager)
            })

        /*    더보기 선물하기    */
        disposeBag.add(RxView.clicks(ll_act_talk_more_gift)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    //임시로 마켓 못가도록 막는 코드.
                    Utility.instance.showAlert(
                        this,
                        R.string.app_name,
                        R.string.msg_popup_notWorkingYet,
                        DialogInterface.OnClickListener { dialog, which ->

                        })

                    //기존 코드
//                    val intent = Intent(this, MarketActivity::class.java)
//                    intent.putExtra(AppKeyValue.instance.marketOtherId, otherId)
//                    startActivity(intent)
                })

        /*    보내기    */
        disposeBag.add(RxView.clicks(btn_act_talk_send)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val talkData = TalkData()
                val time = Utility.instance.getNowTimeFormat(this)
                talkMessage = et_act_talk_content.text.toString()

                if (TextUtils.isEmpty(talkMessage)) {
                    Utility.instance.showToast(this, resources.getString(R.string.msg_talk_message))
                } else {
                    talkData.chatType = resources.getString(R.string.talk_type_message)
                    talkData.chatSender = id
                    talkData.chatSendDate = time
                    talkData.chatMsg = talkMessage
                    talkData.chatReadDate = AppKeyValue.instance.talkCheckTime
                    mAdapter.addItemData(talkData)
                    rv_act_talk_list.scrollToPosition(mAdapter.itemCount.minus(1))

                    rv_act_talk_list.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                        if (bottom < oldBottom) {
                            viewLayoutManager?.smoothScrollToPosition(rv_act_talk_list, null, mAdapter.itemCount)
                        }
                    }

//                    if (gender == "F") {
//                        mPresenter.setSendTalk(id, otherId, talkMessage, null)
//                    } else {
                        mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdTalk)
//                    }
                    et_act_talk_content.text = null
                }
            })

        /*    프로필    */
        mAdapter.itemClick = itemClickListener()
    }

    private fun setEventBusListener() {
        EventBus.subjectTalk.subscribe {
            runOnUiThread {
                if (it.chatRoomNo == roomNo) {
                    for(item in mAdapter.data){
                        item.chatReadDate = AppKeyValue.instance.keyYes
                    }
                    mAdapter.notifyDataSetChanged()
                    mAdapter.addItemData(it)
                    rv_act_talk_list.scrollToPosition(mAdapter.itemCount.minus(1))

                    rv_act_talk_list.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                        if (bottom < oldBottom) {
                            viewLayoutManager?.smoothScrollToPosition(rv_act_talk_list, null, mAdapter.itemCount)
                        }
                    }
                }
            }
        }.addTo(disposeBag)

        EventBus.subjectCoinCharge.subscribe {
            if (it == AppKeyValue.instance.eventBusCoinCharge) {
                finish()
            }
        }.addTo(disposeBag)
    }

    private fun itemClickListener() = View.OnClickListener {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(AppKeyValue.instance.profileMbNo, mbNo)
        startActivity(intent)
    }

    /*    채팅내역    */
    override fun setTalkListComplete(isScroll: Boolean, data: ArrayList<TalkData>) {
        if (!isScroll) {

            mAdapter.data.clear()
            mAdapter.data.addAll(data)

            rv_act_talk_list.scrollToPosition(mAdapter.itemCount.minus(1))
            /*rv_act_talk_list.smoothScrollToPosition(talkAdapter.itemCount.minus(1))*/
            rv_act_talk_list.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                if (bottom < oldBottom) {
                    viewLayoutManager?.smoothScrollToPosition(rv_act_talk_list, null, mAdapter.itemCount)
                }
            }
        } else {
            mAdapter.data.addAll(0, data)
            rv_act_talk_list.scrollToPosition(data.size.plus(2))
        }
        mAdapter.notifyDataSetChanged()

        if (data[0].chatSender == "admin") {
            ll_act_talk_main.isEnabled = false
            ll_act_talk_out.isEnabled = false
            ll_act_talk_block.isEnabled = false
            ll_act_talk_delete.isEnabled = false
            lav_act_talk_more.isEnabled = false
            et_act_talk_content.isEnabled = false
            btn_act_talk_send.isEnabled = false
            ll_act_talk_progress.visibility = View.GONE
        } else {
            var count = 1
            if (data.size > 5) {
                count = 5
            } else if (data.size > 10) {
                count = 10
            }

            if (count > 1) {
                for (i in count downTo 1) {
                    val chatImg = data[data.size.minus(i)].chatImg
                    if (!TextUtils.isEmpty(chatImg)) {
                        if (!isScroll) {
                            Handler().postDelayed({
                                rv_act_talk_list.scrollToPosition(mAdapter.itemCount.minus(1))
                                rv_act_talk_list.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                                    if (bottom < oldBottom) {
                                        viewLayoutManager?.smoothScrollToPosition(
                                            rv_act_talk_list,
                                            null,
                                            mAdapter.itemCount
                                        )
                                    }
                                }

                                ll_act_talk_progress.visibility = View.GONE
                                lav_act_talk_more.isEnabled = true
                                et_act_talk_content.isEnabled = true
                                btn_act_talk_send.isEnabled = true
                            }, 1000)
                            return
                        }
                    }
                }
            }
            ll_act_talk_progress.visibility = View.GONE
            lav_act_talk_more.isEnabled = true
            et_act_talk_content.isEnabled = true
            btn_act_talk_send.isEnabled = true
        }
    }

    /*    채팅내역 호출실패    */
    override fun setTalkListFailed(error: String?) {
        ll_act_talk_progress.visibility = View.GONE
    }

    /*    아이템 보유 확인    */
    override fun setItemCheckComplete(result: String?) {
        when (result) {
            "Y" -> {
                mPresenter.setSendTalk(id, otherId, talkMessage, imageUri)
            }

            "N" -> {
                mAdapter.removeItem(mAdapter.itemCount.minus(1))
                this.let {
                    val popup = CustomPopup(this, "이용권 구매", "이용권을 구매해서 아래 기능을 마음껏 이용해보세요!\n" +getString(R.string.msg_freepass_description), R.drawable.ic_talk_vivid, object: CustomDialogInterface{
                        override fun onConfirm(v: View) {
                            if (rewardVM.rewardedAd.isLoaded) {
                                rewardVM.rewardedAd.show(it, rewardVM.adCallback)
                            }
                        }

                        override fun onCancel(v: View) {
                            val intent = Intent(it,MainActivity::class.java)
                            intent.putExtra(AppKeyValue.instance.goToMarket, true)
                            startActivity(intent)
                        }
                    })
                    popup.popupVM.negativeText.value = "1개월 이용권\n구매하기"
                    popup.popupVM.positiveText.value = "광고 시청 후\n이용권 받기"
                    popup.show()
                }
            }
        }
    }

    /*    아이템 보유 확인 호출실패    */
    override fun setItemCheckFailed(error: String?) {
        Utility.instance.showToast(this, error)
    }

    /*    채팅 메세지 전송    */
    override fun setSendTalkComplete(time: String?, message: String?, talkImage: String?) {
        et_act_talk_content.text = null
        talkMessage = null
        imageUri = null
        rv_act_talk_list.scrollToPosition(mAdapter.itemCount.minus(1))
    }

    /*    채팅 메세지 전송 호출실패    */
    override fun setSendTalkFailed(error: String?) {
        mAdapter.removeItem(mAdapter.itemCount.minus(1))
        Utility.instance.showToast(this, error)
    }

    /*    회원 차단    */
    override fun setBlockComplete(message: String?) {
        ll_act_talk_progress.visibility = View.GONE
        message?.let {
            Utility.instance.showAlert(
                this,
                resources.getString(R.string.app_name),
                it,
                DialogInterface.OnClickListener { dialog, which -> })
        }
    }

    /*    회원차단 호출실패    */
    override fun setBlockFailed(error: String?) {
        ll_act_talk_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    채팅방 삭제    */
    override fun setDeleteComplete() {
        ll_act_talk_progress.visibility = View.GONE
        Utility.instance.showToast(this, resources.getString(R.string.msg_talk_delete_complete))
        EventBus.sendEventTalkDelete(AppKeyValue.instance.eventBusTalkDelete)
        finish()
    }

    /*    채팅방 삭제 호출실패    */
    override fun setDeleteFailed(error: String?) {
        ll_act_talk_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    즐겨찾기    */
    override fun setBookmark(message: String?) {
        Utility.instance.showToast(this, message)
    }

    override fun onBackPressed() {
        if (moreLayoutState) {
            val layoutHeight = Utility.instance.getDip(this, R.dimen.default_margin_100dp)
            val va = ValueAnimator.ofInt(layoutHeight, 0)
            va.duration = 0
            va.addUpdateListener { it1 ->
                val value = it1.animatedValue
                ll_act_talk_more_layout.layoutParams.height = value.toString().toInt()
                ll_act_talk_more_layout.requestLayout()
            }
            va.start()
            moreLayoutState = false

            lav_act_talk_more.setImageResource(R.drawable.talk_more_open_img)
            lav_act_talk_more.visibility = View.VISIBLE
        } else {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
        Utility.instance.hideSoftKeyboard(this)
        softkeyboard?.unRegisterSoftKeyboardCallback()
    }

}
