package com.dmonster.darling.honey.profile.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseActivity
import com.dmonster.darling.honey.customview.CustomDialogInterface
import com.dmonster.darling.honey.customview.CustomPopup
import com.dmonster.darling.honey.dialog.ItemTalkDialog
import com.dmonster.darling.honey.dialog.NotifyDialog
import com.dmonster.darling.honey.profile.presenter.ImageDetailContract
import com.dmonster.darling.honey.profile.presenter.ImageDetailPresenter
import com.dmonster.darling.honey.talk.view.TalkActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_image_detail.*
import uk.co.senab.photoview.PhotoViewAttacher
import java.util.concurrent.TimeUnit

class ImageDetailActivity : BaseActivity(), ImageDetailContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: ImageDetailContract.Presenter
    private lateinit var attacher: PhotoViewAttacher

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
        setContentView(R.layout.activity_image_detail)

        init()
        setListener()
        setEventBusListener()
    }

    fun customPopupDismiss() {
        customPopup?.dismiss()
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
        itemCheck = intent.getBooleanExtra(AppKeyValue.instance.profileDetailItemCheck, true)

        talkId?.let { ctb_act_image_detail_toolbar.setTitle(it) }
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
                Utility.instance.showTwoButtonAlert(this, resources.getString(R.string.app_name), String.format(resources.getString(R.string.msg_profile_item), resources.getString(R.string.msg_profile_item_talk)), DialogInterface.OnClickListener { dialog, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        val talkDialog = ItemTalkDialog()
                        talkDialog.show(supportFragmentManager, AppKeyValue.instance.tagItemTalkDlg)
                    }
                })
            }
        }
    }

    /*    아이템 사용 호출실패    */
    override fun setItemUseFailed(error: String?) {
        ll_act_image_detail_progress.visibility = View.GONE
        Utility.instance.showToast(this, error)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
