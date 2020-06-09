package com.dmonster.darling.honey.inquiry.view

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.base.BaseFragment
import com.dmonster.darling.honey.customview.CustomArrayDialog
import com.dmonster.darling.honey.inquiry.presenter.InquireContract
import com.dmonster.darling.honey.inquiry.presenter.InquirePresenter
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.common.EventBus
import com.jakewharton.rxbinding2.view.RxView
import gun0912.tedbottompicker.TedBottomPicker
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_inquiry_inquire.*
import java.util.concurrent.TimeUnit

class InquiryMenu01Fragment : BaseFragment(), InquireContract.View {

    private lateinit var disposeBag: CompositeDisposable
    private lateinit var mPresenter: InquireContract.Presenter

    private var id: String? = null
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inquiry_inquire, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
        mPresenter = InquirePresenter()
        mPresenter.attachView(this)



        id = context?.let { Utility.instance.getPref(it, AppKeyValue.instance.savePrefID) }
    }

    private fun setInquiryType(text: String?) {
        tv_frag_inquiry_inquire_kind?.text = text
    }

    private fun setListener() {
        /*    문의종류 선택    */
        disposeBag.add(
            RxView.clicks(rl_frag_inquiry_inquire_kind)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    activity?.let { it1 ->
                        val inquiryArray = it1.resources.getStringArray(R.array.inquiry_array)

                        inquiryArray.let { it2 ->
                            val inquiryDialog =
                                object : CustomArrayDialog(
                                    it1,
                                    getString(R.string.dialog_title_inquiry),
                                    getString(R.string.dialog_subtitle_inquiry),
                                    it2
                                ) {
                                    override fun onConfirm() {
                                        setInquiryType(it2[dialogVM.selectedIndex])
                                    }
                                }

                            inquiryDialog.setCanceledOnTouchOutside(false)
                            inquiryDialog.show()
                        }

                    }
                })

        /*    이미지 등록    */
        disposeBag.add(RxView.clicks(rl_frag_inquiry_inquire_image)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe
            {
                val tedBottomPicker = context?.let { it1 ->
                    TedBottomPicker.Builder(it1)
                        .setImageProvider { imageView, imageUri ->
                            Glide.with(this).load(imageUri).apply(RequestOptions().centerCrop())
                                .into(imageView)
                        }
                        .setOnImageSelectedListener { uri ->
                            imageUri = uri
                            tv_frag_inquiry_inquire_image.text =
                                it1.resources.getString(R.string.inquiry_inquire_image_file_select)
                        }
                        .setPreviewMaxCount(1000)
                        .setCameraTile(R.drawable.camera_shooting)
                        .setCameraTileBackgroundResId(R.color.color_black)
                        .setGalleryTile(R.drawable.camera_album)
                        .setGalleryTileBackgroundResId(R.color.color_black)
                        .create()
                }
                tedBottomPicker?.show(activity?.supportFragmentManager)
            })

        /*    문의하기    */
        disposeBag.add(RxView.clicks(btn_frag_inquiry_inquire)
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe
            {
                val type = tv_frag_inquiry_inquire_kind.text.toString()
                val phone = et_frag_inquiry_inquire_phone.text.toString()
                val content = et_frag_inquiry_inquire_content.text.toString()

                activity?.let { it1 ->
                    when {
                        type == it1.resources?.getString(R.string.inquiry_inquire_kind) -> {
                            Utility.instance.showToast(
                                it1,
                                it1.resources?.getString(R.string.msg_inquiry_inquire_kind)
                            )
                        }

                        TextUtils.isEmpty(content) -> {
                            Utility.instance.showToast(
                                it1,
                                it1.resources?.getString(R.string.msg_inquiry_inquire_content)
                            )
                        }

                        TextUtils.isEmpty(phone) -> {
                            Utility.instance.showToast(
                                it1,
                                it1.resources?.getString(R.string.msg_inquiry_inquire_phone)
                            )
                        }

                        else -> {
                            Utility.instance.hideSoftKeyboard(it1)
                            ll_frag_inquiry_inquire_progress.visibility = View.VISIBLE
                            mPresenter.getInquire(id, type, imageUri, phone, content)
                        }
                    }
                }
            })
    }

    /*    문의하기    */
    override fun setInquireComplete() {
        ll_frag_inquiry_inquire_progress.visibility = View.GONE
        context?.let {
            Utility.instance.showAlert(
                it,
                it.resources.getString(R.string.app_name),
                it.resources.getString(R.string.msg_inquiry_inquire_complete),
                DialogInterface.OnClickListener { dialog, which ->
                    tv_frag_inquiry_inquire_kind.text =
                        it.resources.getString(R.string.inquiry_inquire_kind)
                    et_frag_inquiry_inquire_content.text = null
                    et_frag_inquiry_inquire_phone.text = null
                    tv_frag_inquiry_inquire_image.text =
                        it.resources.getString(R.string.inquiry_inquire_image_file)
                    imageUri = null
                    EventBus.sendEventInquiry(AppKeyValue.instance.eventBusInquiry)
                })
        }
    }

    /*    문의하기 호출실패    */
    override fun setInquireFailed(error: String?) {
        ll_frag_inquiry_inquire_progress.visibility = View.GONE
        context?.let { Utility.instance.showToast(it, error) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
        mPresenter.detachView()
    }

}
