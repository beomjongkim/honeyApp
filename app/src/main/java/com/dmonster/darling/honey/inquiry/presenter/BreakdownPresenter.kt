package com.dmonster.darling.honey.inquiry.presenter

import com.dmonster.darling.honey.inquiry.data.InquiryData
import com.dmonster.darling.honey.inquiry.model.InquiryModel
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class BreakdownPresenter: BreakdownContract.Presenter {

    private lateinit var mModel: InquiryModel
    private lateinit var mView: BreakdownContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: BreakdownContract.View) {
        this.mView = view
        this.mModel = InquiryModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    문의내역    */
    override fun getInquiryList(id: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<InquiryData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setInquiryListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<InquiryData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<InquiryData>()
                            mList.addAll(it)
                            mView.setInquiryList(mList)
                        }
                    }
                    else {
                        mView.setInquiryListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestInquiryList(id, subscriber)
        subscription.add(subscriber)
    }

}
