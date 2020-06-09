package com.dmonster.darling.honey.myactivity.presenter

import com.dmonster.darling.honey.myactivity.data.TalkListData
import com.dmonster.darling.honey.myactivity.model.MyActModel
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class TalkListPresenter: TalkListContract.Presenter {

    private lateinit var mModel: MyActModel
    private lateinit var mView: TalkListContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: TalkListContract.View) {
        this.mView = view
        this.mModel = MyActModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    톡하기 목록    */
    override fun getTalkList(isScroll: Boolean, id: String?, limitCnt: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<TalkListData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setTalkListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<TalkListData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<TalkListData>()
                            mList.addAll(it)
                            mView.setTalkList(isScroll, mList)
                        }
                    }
                    else {
                        mView.setTalkListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMyTalkList(id, limitCnt, subscriber)
        subscription.add(subscriber)
    }

    /*    톡하기 삭제    */
    override fun setTalkDelete(id: String?, idx: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setTalkListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setTalkDeleteComplete(it.message)
                    }
                    else {
                        mView.setTalkDeleteFailed(it.message)
                    }
                }
            }
        }
        mModel.requestMyTalkDelete(id, idx, subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 보유 확인    */
    override fun getItemCheck(id: String?, itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemCheckFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    mView.setItemCheckComplete(it.item)
                }
            }
        }
        mModel.requestItemCheck(id, itemId, subscriber)
        subscription.add(subscriber)
    }

}
