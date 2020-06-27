package com.dmonster.darling.honey.profile.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.ads.viewmodel.BannerVM
import com.dmonster.darling.honey.ads.viewmodel.RewardVM
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.databinding.ActivityImageDetailBinding
import com.dmonster.darling.honey.dialog.ItemTalkDialog
import com.dmonster.darling.honey.dialog.NotifyDialog
import com.dmonster.darling.honey.main.view.MainActivity
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.profile.presenter.ImageDetailContract
import com.dmonster.darling.honey.profile.presenter.ImageDetailPresenter
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_image_detail.*
import kotlinx.android.synthetic.main.activity_profile.*
import uk.co.senab.photoview.PhotoViewAttacher
import java.util.concurrent.TimeUnit

class ImageDetailActivity : BaseActivity(), ImageDetailContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: ImageDetailContract.Presenter
    private lateinit var attacher: PhotoViewAttacher
    lateinit var binding: ActivityImageDetailBinding
    private lateinit var rewardVM : RewardVM
    private var id: String? = null
    private var otherId: String? = null
    private var talkId: String? = null
    private var otherArea: String? = null
    private var otherAge: String? = null
    private var imageArray: ArrayList<String>? = null
    private var imagePosition: Int? = null
    private var mbNo: String? = null
    private var roomNo: String? = null

    private var talkCheck: Boolean = false
    private var itemCheck: Boolean = true

    private var customPopup: CustomPopup? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_image_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_detail)

        init()
        setViewModel()
        setListener()
        setEventBusListener()
    }


    private fun init() {
        id = Utility.instance.getPref(this, AppKeyValue.instance.savePrefID)
        otherId = intent.getStringExtra(AppKeyValue.instance.profileDetailOtherId)
        talkId = intent.getStringExtra(AppKeyValue.instance.profileDetailTalkId)
        otherArea = intent.getStringExtra(AppKeyValue.instance.profileDetailOtherArea)
        otherAge = intent.getStringExtra(AppKeyValue.instance.profileDetailOtherAge)
        imageArray = intent.getStringArrayListExtra(AppKeyValue.instance.profileDetailImage)
        imagePosition = intent.getIntExtra(AppKeyValue.instance.profileDetailImagePosition, 0)
        mbNo = intent.getStringExtra(AppKeyValue.instance.profileMbNo)
        itemCheck = intent.getBooleanExtra(AppKeyValue.instance.profileDetailItemCheck, false)

        talkId?.let { ctb_act_image_detail.setTitle(it) }
        disposeBag = CompositeDisposable()
        attacher = PhotoViewAttacher(iv_act_image_detail_picture)
        attacher.update()

        imagePosition?.let { setPress(it) }

        mPresenter = ImageDetailPresenter()
        mPresenter.attachView(this)
        mPresenter.getTalkCheck(id, otherId)
    }

    private fun setListener() {
        /*    이전 이미지    */
        disposeBag.add(RxView.clicks(iv_act_image_detail_before)
            //.throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (itemCheck) {
                    var position = imagePosition?.minus(1)
                    position?.let { it1 ->
                        if (it1 < 0) {
                            position = imageArray?.size?.minus(1)
                        }
                    }
                    position?.let { it1 -> setPress(it1) }
                }
            })

        /*    다음 이미지    */
        disposeBag.add(RxView.clicks(iv_act_image_detail_next)
            //.throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (itemCheck) {
                    var position = imagePosition?.plus(1)
                    if (position == imageArray?.size) {
                        position = 0
                    }
                    position?.let { it1 -> setPress(it1) }
                }
            })

        /*    신고하기    */
        disposeBag.add(RxView.clicks(btn_act_image_detail_notify)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                val notifyDialog = NotifyDialog()
                notifyDialog.setMbNumber(mbNo)
                notifyDialog.show(supportFragmentManager, AppKeyValue.instance.tagNotifyDlg)
            })

        /*    톡하기    */
        disposeBag.add(RxView.clicks(btn_act_image_detail_talk)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
                if (talkCheck) {
                    val intent = Intent(this, TalkActivity::class.java)
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
                    ll_act_image_detail_progress.visibility = View.VISIBLE
                    mPresenter.getItemCheck(id, AppKeyValue.instance.itemIdTalk)
                }
            })
    }

    private fun setEventBusListener() {
        EventBus.subjectCoinCharge.subscribe {
            if (it == AppKeyValue.instance.eventBusCoinCharge) {
                finish()
            }
        }.addTo(disposeBag)
    }

    private fun setPress(position: Int) {
        imagePosition = position
        imageArray?.let { it ->
            imagePosition?.let { it1 ->
                Glide.with(this).load(it[it1]).into(iv_act_image_detail_picture)
            }
        }
    }

    private fun setViewModel() {
        binding.bannerVM =
            BannerVM(Utility.instance.getPref(this, AppKeyValue.instance.savePrefID), lifecycle)
        binding.lifecycleOwner = this
        rewardVM = RewardVM(this)
        rewardVM.adCallback = object : RewardedAdCallback() {

            override fun onRewardedAdOpened() {
                // Ad opened.
                Log.d(tag, "Ad opened.")
            }

            override fun onRewardedAdClosed() {
                // Ad closed.
                Log.d(tag, "Ad closed.")
            }

            override fun onUserEarnedReward(@NonNull reward: RewardItem) {
                ll_act_image_detail_progress.visibility = View.VISIBLE
                // User earned reward.
                val subscriber = object : DisposableObserver<ResultItem<String>>() {
                    override fun onComplete() {
                        ll_act_image_detail_progress.visibility = View.GONE
                    }

                    override fun onError(e: Throwable) {
                        ll_act_image_detail_progress.visibility = View.GONE
                    }

                    override fun onNext(item: ResultItem<String>) {
                        item.let { it ->
                            if (it.isSuccess) {
                                Utility.instance.showToast(this@ImageDetailActivity, "성공적으로 이용권을 구매하였습니다.")
                            }
                        }
                        ll_act_image_detail_progress.visibility = View.GONE
                    }
                }
                ItemModel().buyItem(id, 1, subscriber)
                Log.d(tag, "User earned reward.")
            }

            override fun onRewardedAdFailedToShow(errorCode: Int) {
                // Ad failed to display.
                Log.d(tag, "Ad failed to display.")
            }
        }
    }

    /*    채팅방여부 확인    */
    override fun setTalkCheck(result: String?, roomNo: String?) {
        when (result) {
            "Y" -> {
                talkCheck = true
                this.roomNo = roomNo
            }
            "N" -> talkCheck = false
        }
    }

    /*    톡하기    */
    override fun setTalkComplete() {
        ll_act_image_detail_progress.visibility = View.GONE
        val intent = Intent(this, TalkActivity::class.java)
        /*    상단 타이틀정보    */
        intent.putExtra(AppKeyValue.instance.talkTitleName, talkId)
        intent.putExtra(AppKeyValue.instance.talkTitleArea, otherArea)
        intent.putExtra(AppKeyValue.instance.talkTitleAge, otherAge)
        startActivity(intent)
    }

    /*    톡하기 호출실패    */
    override fun setTalkFailed(error: String?) {
        ll_act_image_detail_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    /*    아이템 사용    */
    override fun setItemUseComplete(type: String?, result: String?) {
        ll_act_image_detail_progress.visibility = View.GONE
        when (result) {
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
                                ll_act_image_detail_progress.visibility = View.VISIBLE
                                mPresenter.setItemUse(v.context, id, AppKeyValue.instance.itemIdTalk, mbNo, otherId)
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
                val popup = CustomPopup(this, "이용권 구매", "이용권을 구매해서 아래 기능을 마음껏 이용해보세요!\n" +getString(R.string.msg_freepass_description), R.drawable.ic_talk_vivid, object: CustomDialogInterface{
                    override fun onConfirm(v: View) {
                        if (rewardVM.rewardedAd.isLoaded) {
                            rewardVM.rewardedAd.show(this@ImageDetailActivity, rewardVM.adCallback)
                        }
                    }

                    override fun onCancel(v: View) {
                        val intent = Intent(this@ImageDetailActivity, MainActivity::class.java)
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

    /*    아이템 사용 호출실패    */
    override fun setItemUseFailed(error: String?) {
        ll_act_image_detail_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    private fun customPopupDismiss(){
        customPopup?.dismiss()
        ll_act_image_detail_progress.visibility = View.GONE
    }
    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
