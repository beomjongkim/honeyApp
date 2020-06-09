package com.dmonster.darling.honey.notice.presenter

import com.dmonster.darling.honey.notice.data.NoticeData
import com.dmonster.darling.honey.notice.model.NoticeModel
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class NoticePresenter: NoticeContract.Presenter {

    private lateinit var mModel: NoticeModel
    private lateinit var mView: NoticeContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: NoticeContract.View) {
        this.mView = view
        this.mModel = NoticeModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    공지사항 목록    */
    override fun getNoticeList(id: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<NoticeData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setNoticeListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<NoticeData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<NoticeData>()
                            mList.addAll(it)
                            mView.setNoticeList(mList)
                        }
                    }
                    else {
                        mView.setNoticeListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestNoticeList(id, subscriber)
        subscription.add(subscriber)
    }

}
