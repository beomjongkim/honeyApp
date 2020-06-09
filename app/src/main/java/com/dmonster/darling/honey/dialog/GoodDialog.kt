package com.dmonster.darling.honey.dialog

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.profile.view.GoodActivity
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.ServerApi
import com.dmonster.darling.honey.util.Utility
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.RetrofitProtocol
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_good.*
import java.util.concurrent.TimeUnit

class GoodDialog: androidx.fragment.app.DialogFragment() {

    private lateinit var disposeBag: CompositeDisposable

    private var otherId: String? = null
    private var otherProfileImage: String? = null
    private var otherTalkId: String? = null
    private var otherType: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        isCancelable = false
        return inflater.inflate(R.layout.dialog_good, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        init()
        setListener()
    }

    private fun init() {
        disposeBag = CompositeDisposable()
    }

    private fun setListener() {
        /*    취소    */
        disposeBag.add(RxView.clicks(btn_dialog_good_cancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dismiss()
                })

        /*    확인    */
        disposeBag.add(RxView.clicks(btn_dialog_good_confirm)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe {
                    dialog?.context?.let {
                        val id = Utility.instance.getPref(it, AppKeyValue.instance.savePrefID)
                        getItemCheck(id, AppKeyValue.instance.itemIdGood)
                    }
                })
    }

    /*    호감있어요 아이템 보유확인    */
    private fun getItemCheck(id: String?, itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                dialog?.context?.let { Utility.instance.showToast(it, e.message) }
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    if(it.item == "Y") {
                        val intent = Intent(context, GoodActivity::class.java)
                        intent.putExtra(AppKeyValue.instance.goodOtherId, otherId)
                        intent.putExtra(AppKeyValue.instance.goodOtherProfileImage, otherProfileImage)
                        intent.putExtra(AppKeyValue.instance.goodOtherTalkId, otherTalkId)
                        intent.putExtra(AppKeyValue.instance.goodOtherType, otherType)
                        startActivity(intent)
                        dismiss()
                    }
                    else {
                        dialog?.context?.let { it1 ->
                            val content = String.format(resources.getString(R.string.msg_profile_item), resources.getString(R.string.profile_good))
                            Utility.instance.showTwoButtonAlert(it1, resources.getString(R.string.app_name), content, DialogInterface.OnClickListener { dialog, which ->
                                if(which == DialogInterface.BUTTON_POSITIVE) {
                                    val goodDialog = ItemGoodDialog()
                                    activity?.supportFragmentManager?.let { it2 -> goodDialog.show(it2, AppKeyValue.instance.tagItemGoodDlg) }
                                    dismiss()
                                }
                                else {
                                    dismiss()
                                }
                            })
                        }
                    }
                }
            }
        }

        RetrofitProtocol().retrofit.requestMyItemCheck(ServerApi.instance.myItemCheckMethod, id, itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(subscriber)
    }

    fun setOtherInfo(otherId: String?, otherProfileImage: String?, otherTalkId: String?, otherType: String?) {
        this.otherId = otherId
        this.otherProfileImage = otherProfileImage
        this.otherTalkId = otherTalkId
        this.otherType = otherType
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposeBag.clear()
    }

}
