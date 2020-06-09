package com.dmonster.darling.honey.inquiry.presenter

import android.net.Uri
import com.dmonster.darling.honey.inquiry.data.InquiryData
import com.dmonster.darling.honey.inquiry.model.InquiryModel
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class InquirePresenter: InquireContract.Presenter {

    private lateinit var mModel: InquiryModel
    private lateinit var mView: InquireContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: InquireContract.View) {
        this.mView = view
        this.mModel = InquiryModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    문의하기    */
    override fun getInquire(id: String?, type: String?, image: Uri?, phone: String?, content: String?) {
        val subscriber = object: DisposableObserver<ResultItem<BaseItem>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setInquireFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<BaseItem>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setInquireComplete()
                    }
                    else {
                        mView.setInquireFailed(it.message)
                    }
                }
            }
        }
        mModel.requestInquire(id, type, image, phone, content, subscriber)
        subscription.add(subscriber)
    }

}
