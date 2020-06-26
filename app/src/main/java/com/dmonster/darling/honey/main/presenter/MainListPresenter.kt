package com.dmonster.darling.honey.main.presenter

import android.content.Context
import android.net.Uri
import com.dmonster.darling.honey.main.data.MainListData
import com.dmonster.darling.honey.main.model.MainListModel
import com.dmonster.darling.honey.notice.data.NoticeData
import com.dmonster.darling.honey.point.data.CheckFreePassData
import com.dmonster.darling.honey.point.model.ItemModel
import com.dmonster.darling.honey.talk.data.TalkData
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.retrofit.BaseItem
import com.dmonster.darling.honey.util.retrofit.ResultItem
import com.dmonster.darling.honey.util.retrofit.ResultListItem
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

class MainListPresenter: MainListContract.Presenter {

    private lateinit var mModel: MainListModel
    private lateinit var mView: MainListContract.View
    private lateinit var subscription: CompositeDisposable

    override fun attachView(view: MainListContract.View) {
        this.mView = view
        this.mModel = MainListModel()
        this.subscription = CompositeDisposable()
    }

    override fun detachView() {
        subscription.clear()
    }

    /*    회원목록    */
    override fun getMainList(isScroll: Boolean, startCnt: String?, limitCnt: String?, id: String?, area: String?, gender: String?, age: String?) {
        val subscriber = object: DisposableObserver<ResultListItem<MainListData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setMainListFailed(isScroll, e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultListItem<MainListData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        it.items?.let {
                            val mList = ArrayList<MainListData>()
                            mList.addAll(it)
                            mView.setMainList(isScroll, mList)
                        }
                    }
                    else {
                        mView.setMainListFailed(isScroll, it.message)
                    }
                }
            }
        }
        mModel.requestMainList(startCnt, limitCnt, id, area, gender, age, subscriber)
        subscription.add(subscriber)
    }

    /*    자동갱신 여부 확인    */
    override fun setRefresh(id: String?, itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    mView.getRefresh(it.item)
                }
            }
        }
        mModel.requestItemCheck(id, itemId, subscriber)
        subscription.add(subscriber)
    }

    /*    위로 올리기    */
    override fun setRefreshList(id: String?) {
        val subscriber = object: DisposableObserver<BaseItem>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setRefreshListFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: BaseItem) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setRefreshListComplete()
                    }
                    else {
                        mView.setRefreshListFailed(it.message)
                    }
                }
            }
        }
        mModel.requestRefreshList(id, subscriber)
        subscription.add(subscriber)
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

    /*    채팅방 여부 확인    */
    override fun getTalkCheck(id: String?, otherId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<ArrayList<String>>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<ArrayList<String>>) {
                item.let { it ->
                    it.item?.let {
                        mView.setTalkCheck(it[0], it[1])
                    }
                }
            }
        }
        mModel.requestTalkCheck(id, otherId, subscriber)
        subscription.add(subscriber)
    }

    /*    아이템 보유 확인    */
    override fun getItemCheck(id: String?, itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemUseFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let { it ->
                    mView.setItemUseComplete(itemId, it.item)
                }
            }
        }
        mModel.requestItemCheck(id, itemId, subscriber)
        subscription.add(subscriber)
    }

    override fun checkPass(id: String?,itemId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<CheckFreePassData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setPassNeed()
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<CheckFreePassData>) {
                item.let { it ->
                    if(it.isSuccess)
                        mView.setItemUseComplete(itemId,"Y")
                    else
                        mView.setPassNeed()
                }
            }
        }
        ItemModel().check_own_freepass(id,  subscriber)
        subscription.add(subscriber)
    }


    /*    아이템 사용    */
    override fun setItemUse(context: Context, id: String?, itemId: String?, mbNo: String?, otherId: String?) {
        val subscriber = object: DisposableObserver<ResultItem<String>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setItemUseFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<String>) {
                item.let {
                    if(it.isSuccess) {
                        getTalk(id, otherId, "", null)
                    }
                    else {
                        mView.setItemUseFailed(it.message)
                    }
                }
            }
        }
        mModel.requestItemUse(id, itemId, mbNo, subscriber)
        subscription.add(subscriber)
    }

    /*    톡하기    */
    private fun getTalk(id: String?, otherId: String?, message: String?, talkImage: Uri?) {
        val subscriber = object: DisposableObserver<ResultItem<TalkData>>() {
            override fun onComplete() {

            }

            override fun onError(e: Throwable) {
                mView.setTalkFailed(e.message)
                e.printStackTrace()
            }

            override fun onNext(item: ResultItem<TalkData>) {
                item.let { it ->
                    if(it.isSuccess) {
                        mView.setTalkComplete()
                    }
                    else {
                        mView.setTalkFailed(it.message)
                    }
                }
            }
        }
        mModel.requestTalk(id, otherId, message, talkImage, subscriber)
        subscription.add(subscriber)
    }

}
